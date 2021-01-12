package riskgui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.CanvasBuilder;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeType;
import risk.Mapa;
import risk.Pais;
import risk.Partida;
import risk.cartasmision.PaisEvent;
import risk.cartasmision.PaisEventSubscriber;
import risk.ejercito.Ejercito;
import risk.riskexception.ExcepcionGeo;
import risk.riskexception.ExcepcionJugador;
import risk.riskexception.ExcepcionRISK;

public class Mundo {

    private static final String pathToVideoFondo = "resources/videoFondo.mp4";
    private Properties props;
    private Pane pathsPaises;
    private Pane labelsNombresPane;
    private Pane labelsNumEjercitosPane;
    private StackPane stackPane;
    private Set<Pais> paises;
    private HashMap<Pais, SVGPath> svgPaths;
    private HashMap<Pais, Label> labelsNombres;
    private HashMap<Pais, Label> labelsNumEjercitos;
    private MediaView fondo;

    private static final DataFormat dataFormatPais = new DataFormat("risk.pais");

    public Mundo() {
        cargarMundo();
        procesarPaths();
        crearStackPane();
    }

    public StackPane getWorldStackPane() {
        return this.stackPane;
    }

    private void crearStackPane() {
        stackPane = new StackPane();
        stackPane.getChildren().add(pathsPaises);
        stackPane.getChildren().add(labelsNombresPane);
        stackPane.getChildren().add(labelsNumEjercitosPane);

        crearFondo();
        stackPane.getChildren().add(fondo);
        
        ajustarTamanoStackPane();
    }

    private void crearFondo() {
        try {
            Media videoFondo = new Media(new File(pathToVideoFondo).toURI().toString());
            MediaPlayer reproductor = new MediaPlayer(videoFondo);
            reproductor.setAutoPlay(true);
            reproductor.setCycleCount(MediaPlayer.INDEFINITE);
            fondo = new MediaView(reproductor);
        } catch (MediaException e) {
            fondo = new MediaView();
        }
    }
    
    private void ajustarTamanoStackPane() {
        double widthMax;
        double heightMax;
        widthMax = svgPaths.entrySet().stream().map(e -> e.getValue()).mapToDouble(svg -> svg.getLayoutBounds().getMaxX()).max().orElse(0);
        heightMax = svgPaths.entrySet().stream().map(e -> e.getValue()).mapToDouble(svg -> svg.getLayoutBounds().getMaxY()).max().orElse(0);
        stackPane.setMinWidth(widthMax);
        stackPane.setMaxWidth(widthMax);
        stackPane.setPrefWidth(widthMax);
        stackPane.setMinHeight(heightMax);
        stackPane.setMaxHeight(heightMax);
        stackPane.setPrefHeight(heightMax);
    }

    private void prepararDragNDrop(Pais pais, SVGPath svgPath) {
        svgPath.setOnDragDetected(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (pais.getNumEjercitos() > 1) {
                    Dragboard dragboard = svgPath.startDragAndDrop(TransferMode.MOVE);

                    Map<DataFormat, Object> mapa = new HashMap<DataFormat, Object>();
                    mapa.put(dataFormatPais, pais.getCodigo());

                    dragboard.setContent(mapa);

                    URL url = getClass().getResource("resources/soldado.png");

                    Image imagenSoldado = new Image(url.toExternalForm(), 50, 50, true, true);
                    dragboard.setDragView(imagenSoldado);
                }

                event.consume();
            }
        });
        svgPath.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                if (!event.getGestureSource().equals(svgPath)) {
                    event.acceptTransferModes(TransferMode.ANY);
                }
                event.consume();
            }
        });
        svgPath.setOnDragDropped(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                String paisOrigen = (String) dragboard.getContent(dataFormatPais);
                if (paisOrigen != null) {
                    try {
                        Partida.getPartida().rearmar(Mapa.getMapa().getPais(paisOrigen), pais, 1);
                    } catch (ExcepcionRISK e) {
                        System.err.println(e.toString());
                    }
                }
                event.setDropCompleted(true);
            }

        });
    }

    private void prepararHandlersSVGPath(Pais pais, SVGPath svgPath) {
        svgPath.setOnMouseEntered(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                svgPath.fillProperty().set(Color.ORANGE);
            }
        });
        svgPath.setOnMouseExited(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Color c = pais.getContinente().getColor().getFxColor();
                svgPath.fillProperty().set(c);
            }
        });
        svgPath.setPickOnBounds(false);
        prepararDragNDrop(pais, svgPath);
    }

    private void setAparienciaSVGPath(SVGPath svgPath, String nombrePais) {
        svgPath.setStroke(Color.BLACK);
        try {
            Color c = Mapa.getMapa().getPais(nombrePais).getContinente().getColor().getFxColor();
            svgPath.fillProperty().set(c);
        } catch (ExcepcionRISK e) {
            svgPath.fillProperty().set(Color.BLACK);
        }
        svgPath.setStrokeWidth(2);
        svgPath.setStrokeType(StrokeType.CENTERED);
    }

    private void anadirLabelNombre(Pais pais, SVGPath svgPath) {
        Label labelNombre = new Label(pais.getNombreHumano());
        labelsNombres.put(pais, labelNombre);
        labelsNombresPane.getChildren().add(labelNombre);

        labelNombre.layoutXProperty()
                .set(svgPath.getLayoutBounds().getMinX()
                        + (svgPath.getLayoutBounds().getMaxX() - svgPath.getLayoutBounds().getMinX()) / 2
                        - labelNombre.getWidth() / 2);
        labelNombre.layoutYProperty()
                .set(svgPath.getLayoutBounds().getMinY()
                        + (svgPath.getLayoutBounds().getMaxY() - svgPath.getLayoutBounds().getMinY()) / 2
                        - labelNombre.getHeight() / 2);
        labelNombre.setMaxWidth(svgPath.getLayoutBounds().getMaxX() - svgPath.getLayoutBounds().getMinX());
        labelNombre.setMaxHeight(svgPath.getLayoutBounds().getMaxY() - svgPath.getLayoutBounds().getMinY());
        labelNombre.setAlignment(Pos.CENTER);
        labelNombre.setBackground(new Background(new BackgroundFill(Color.ROSYBROWN, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void anadirLabelNumEjercitos(Pais pais, SVGPath svgPath) {
        Label labelNumEjercitos = new Label(Integer.toString(pais.getNumEjercitos()));
        labelsNumEjercitos.put(pais, labelNumEjercitos);
        labelsNumEjercitosPane.getChildren().add(labelNumEjercitos);

        labelNumEjercitos.layoutXProperty()
                .set(svgPath.getLayoutBounds().getMinX()
                        + (svgPath.getLayoutBounds().getMaxX() - svgPath.getLayoutBounds().getMinX()) / 2
                        - labelNumEjercitos.getWidth() / 2);
        labelNumEjercitos.layoutYProperty()
                .set(svgPath.getLayoutBounds().getMinY()
                        + (svgPath.getLayoutBounds().getMaxY() - svgPath.getLayoutBounds().getMinY()) / 2
                        - labelNumEjercitos.getHeight() / 2 + 30);
        labelNumEjercitos.setMaxWidth(svgPath.getLayoutBounds().getMaxX() - svgPath.getLayoutBounds().getMinX());
        labelNumEjercitos.setMaxHeight(svgPath.getLayoutBounds().getMaxY() - svgPath.getLayoutBounds().getMinY());
        labelNumEjercitos.setAlignment(Pos.CENTER);
        labelNumEjercitos.setBackground(new Background(new BackgroundFill(Color.ROSYBROWN, CornerRadii.EMPTY, Insets.EMPTY)));

        PaisEventSubscriber paisEventSubscriber = new PaisEventSubscriber() {

            @Override
            public void update(PaisEvent evento) {
                if (evento.getPaisDespues().equals(pais)) {
                    labelNumEjercitos.setText(Integer.toString(pais.getNumEjercitos()));
                }
            }

                    };

                    Mapa.getMapa().getPaisEventPublisher().subscribe(paisEventSubscriber);
    }

    private void crearYAnadirSVGPath(String nombrePais, String path) {
        SVGPath svgPath = new SVGPath();
        svgPath.setContent(path);
        setAparienciaSVGPath(svgPath, nombrePais);

        try {
            Pais pais = Mapa.getMapa().getPais(nombrePais);
            prepararHandlersSVGPath(pais, svgPath);
            svgPaths.put(pais, svgPath);
            pathsPaises.getChildren().add(svgPath);

            anadirLabelNombre(pais, svgPath);
            anadirLabelNumEjercitos(pais, svgPath);
            
        } catch (ExcepcionGeo e) {
            System.err.println(e.toString());
            System.err.println(nombrePais);
        }
    }

    private void procesarPaths() {
        pathsPaises = new Pane();
        labelsNombresPane = new Pane();
        labelsNumEjercitosPane = new Pane();
        svgPaths = new HashMap<>();
        labelsNombres = new HashMap<>();
        labelsNumEjercitos = new HashMap<>();

        labelsNombresPane.setMouseTransparent(true);
        labelsNumEjercitosPane.setMouseTransparent(true);

        for (Entry<Object, Object> entrada : props.entrySet()) {
            crearYAnadirSVGPath(entrada.getKey().toString(), entrada.getValue().toString());
        }
    }

    private void cargarMundo() {
        props = new Properties();
        try (Reader streamProps = new InputStreamReader(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("riskgui/paises.properties"),
                StandardCharsets.ISO_8859_1)) {
            props.load(streamProps);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
