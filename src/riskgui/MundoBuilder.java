package riskgui;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import risk.Mapa;
import risk.Pais;
import risk.Partida;
import risk.cartasmision.PaisEvent;
import risk.cartasmision.PaisEventSubscriber;
import risk.riskexception.ExcepcionGeo;
import risk.riskexception.ExcepcionRISK;

import java.io.InputStreamReader;

public class MundoBuilder {

    private static final DataFormat dataFormatPais = new DataFormat("risk.pais");
    private Properties props;
    private Set<Pais> paises;
    private HashMap<Pais, SVGPath> svgPaths;
    private HashMap<Pais, Label> labelsNombres;
    private HashMap<Pais, Label> labelsNumEjercitos;
    private Set<PaisEventSubscriber> setSubscribersSVGPaths;

    private static final Blend blendSVGPaths = new Blend();
    {
        InnerShadow is = new InnerShadow();
        is.setOffsetX(0);
        is.setOffsetY(0);
        is.setChoke(0.7f);
        is.setRadius(20.0f);
        is.blurTypeProperty().set(BlurType.THREE_PASS_BOX);
        is.setColor(Color.BLACK);

        DropShadow ds = new DropShadow();
        ds.setOffsetX(0);
        ds.setOffsetY(0);
        ds.setHeight(1.0f);
        ds.setRadius(20.0f);
        ds.blurTypeProperty().set(BlurType.THREE_PASS_BOX);
        ds.setColor(Color.BLACK);
        
        blendSVGPaths.setTopInput(is);
        //blendSVGPaths.setBottomInput(ds);
        blendSVGPaths.setMode(BlendMode.SRC_OVER);
        blendSVGPaths.setOpacity(0.35f);
    }

    MundoBuilder() {
        svgPaths = new HashMap<>();
        labelsNombres = new HashMap<>();
        labelsNumEjercitos = new HashMap<>();
        paises = new HashSet<>();

        setSubscribersSVGPaths = new HashSet<>();

        cargarMundo();
        procesarPaths();
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

    private void procesarPaths() {
        for (Entry<Object, Object> entrada : props.entrySet()) {
            crearYAnadirSVGPath(entrada.getKey().toString(), entrada.getValue().toString());
        }
        setSubscribersSVGPaths.forEach(s -> Mapa.getMapa().getPaisEventPublisher().subscribe(s));
    }

    private void setAparienciaSVGPath(SVGPath svgPath, String nombrePais) {
        svgPath.setStroke(Color.BLACK);
        try {
            Pais pais = Mapa.getMapa().getPais(nombrePais);
            svgPath.fillProperty().set(pais.getContinente().getColor().getFxColor());
            prepararChangeListenersSVGPath(svgPath, pais);
        } catch (ExcepcionRISK e) {
            svgPath.fillProperty().set(Color.BLACK);
        }
        svgPath.setStrokeWidth(2);
        svgPath.setStrokeType(StrokeType.CENTERED);

        svgPath.setEffect(blendSVGPaths);

        svgPath.setCursor(Cursor.HAND);

    }

    private void prepararChangeListenersSVGPath(SVGPath svgPath, Pais p) {
        PaisEventSubscriber paisEventSubscriber = new PaisEventSubscriber() {
            @Override
            public void update(PaisEvent evento) {
                if (p.equals(evento.getPaisDespues())) {
                    svgPath.fillProperty().set(p.getContinente().getColor().getFxColor());
                }
            }
        };
        setSubscribersSVGPaths.add(paisEventSubscriber);
    };
    
    private Label anadirLabelNombre(Pais pais, SVGPath svgPath) {
        Label labelNombre = new Label(pais.getCodigo());
        labelsNombres.put(pais, labelNombre);
        
        labelNombre.layoutXProperty()
        .set((svgPath.getLayoutBounds().getMinX()
        + (svgPath.getLayoutBounds().getMaxX() - svgPath.getLayoutBounds().getMinX()) / 2
        - labelNombre.getWidth() / 2) - labelNombre.getText().length() * 4);
        labelNombre.layoutYProperty()
        .set(svgPath.getLayoutBounds().getMinY()
        + (svgPath.getLayoutBounds().getMaxY() - svgPath.getLayoutBounds().getMinY()) / 2
        - labelNombre.getHeight() / 2 - 25);
        labelNombre.setMaxWidth(svgPath.getLayoutBounds().getMaxX() - svgPath.getLayoutBounds().getMinX());
        labelNombre.setMaxHeight(svgPath.getLayoutBounds().getMaxY() - svgPath.getLayoutBounds().getMinY());
        labelNombre.setAlignment(Pos.CENTER);
        labelNombre.setTextFill(Color.BLACK);
        labelNombre.setFont(Font.font("sans-serif", FontWeight.BOLD, 12));
        labelNombre.setPadding(new Insets(3, 5, 3, 5));
        labelNombre.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        PaisEventSubscriber paisEventSubscriber = new PaisEventSubscriber() {
            @Override
            public void update(PaisEvent evento) {
                if (evento.getPaisDespues().equals(pais)) {
                    if (!evento.getPaisDespues().getCodigo().equals(labelNombre.getText())) {
                        labelNombre.setText(evento.getPaisDespues().getCodigo());
                    }
                }
            }
        };
        Mapa.getMapa().getPaisEventPublisher().subscribe(paisEventSubscriber);

        return labelNombre;
    }

    private Label anadirLabelNumEjercitos(Pais pais, SVGPath svgPath) {
        Label labelNumEjercitos = new Label(Integer.toString(pais.getNumEjercitos()));
        labelsNumEjercitos.put(pais, labelNumEjercitos);

        labelNumEjercitos.layoutXProperty()
                .set((svgPath.getLayoutBounds().getMinX()
                        + (svgPath.getLayoutBounds().getMaxX() - svgPath.getLayoutBounds().getMinX()) / 2
                        - labelNumEjercitos.getWidth() / 2) - pais.getCodigo().length() * 4);
        labelNumEjercitos.layoutYProperty()
                .set(svgPath.getLayoutBounds().getMinY()
                        + (svgPath.getLayoutBounds().getMaxY() - svgPath.getLayoutBounds().getMinY()) / 2
                        - labelNumEjercitos.getHeight() / 2 + 0);
        labelNumEjercitos.setMaxWidth(svgPath.getLayoutBounds().getMaxX() - svgPath.getLayoutBounds().getMinX());
        labelNumEjercitos.setMaxHeight(svgPath.getLayoutBounds().getMaxY() - svgPath.getLayoutBounds().getMinY());
        labelNumEjercitos.setAlignment(Pos.CENTER);
        labelNumEjercitos.setPadding(new Insets(5));
        labelNumEjercitos.setFont(Font.font("sans-serif", FontWeight.BOLD, 12));
        
        PaisEventSubscriber paisEventSubscriber = new PaisEventSubscriber() {
            
            @Override
            public void update(PaisEvent evento) {
                if (evento.getPaisDespues().equals(pais)) {
                    if (pais.getJugador()==null) {
                        labelNumEjercitos.setVisible(false);
                    } else {
                        labelNumEjercitos.setVisible(true);
                        labelNumEjercitos.setText(Integer.toString(pais.getNumEjercitos()));
                        labelNumEjercitos
                        .setBackground(new Background(new BackgroundFill(pais.getJugador().getColor().getFxColor(),
                        new CornerRadii(0, true), Insets.EMPTY)));
                        if (pais.getJugador().getColor().getFxColor().equals(risk.RiskColor.AMARILLO.getFxColor())) {
                            labelNumEjercitos.setTextFill(Color.BLACK);
                        } else {
                            labelNumEjercitos.setTextFill(Color.WHITE);
                        }
                    }
                }
            }

        };

        PaisEvent evento = new PaisEvent();
        evento.setPaisDespues(pais);

        paisEventSubscriber.update(evento);

        Mapa.getMapa().getPaisEventPublisher().subscribe(paisEventSubscriber);

        return labelNumEjercitos;
    }

    private void crearYAnadirSVGPath(String nombrePais, String path) {
        SVGPath svgPath = new SVGPath();
        svgPath.setContent(path);
        setAparienciaSVGPath(svgPath, nombrePais);

        try {
            Pais pais = Mapa.getMapa().getPais(nombrePais);
            svgPaths.put(pais, svgPath);
            paises.add(pais);

            anadirLabelNombre(pais, svgPath);
            anadirLabelNumEjercitos(pais, svgPath);

        } catch (ExcepcionGeo e) {
            System.err.println(e.toString());
            System.err.println(nombrePais);
        }
    }

    public Mundo get() {

        Mundo mundo = new Mundo(svgPaths.values(), labelsNombres.values(), labelsNumEjercitos.values());
        return mundo;
    }

    public MundoBuilder setDefaultEnterExitHandlers() {
        EventHandler<Event> enterHandler;
        EventHandler<Event> exitHandler;
        for (Entry<Pais, SVGPath> e : svgPaths.entrySet()) {
            enterHandler = new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    e.getValue().toFront();
                    e.getValue().fillProperty().set(Color.ORANGE);
                }
            };
            exitHandler = new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    Color c = e.getKey().getContinente().getColor().getFxColor();
                    e.getValue().fillProperty().set(c);
                }
            };
            prepararEnterExitHandlersSVGPath(e.getKey(), e.getValue(), enterHandler, exitHandler);
        }

        return this;
    }

    private void prepararEnterExitHandlersSVGPath(Pais pais, SVGPath svgPath, EventHandler<Event> enterHandler, EventHandler<Event> exitHandler) {
        svgPath.setOnMouseEntered(enterHandler);
        svgPath.setOnMouseExited(exitHandler);
        svgPath.setPickOnBounds(false);
    }

    public MundoBuilder dragNDropRearmar() {
        for (Entry<Pais, SVGPath> e : svgPaths.entrySet()) {
            SVGPath svgPath = e.getValue();
            Pais pais = e.getKey();
            
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

    return this;
    }


    private MundoBuilder dragNDropRepartirEjercitos() {
        for (Entry<Pais, SVGPath> e : svgPaths.entrySet()) {
            SVGPath svgPath = e.getValue();
            Pais pais = e.getKey();
            
        svgPath.setOnDragDetected(null);

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
                    try {
                        Partida.getPartida().repartirEjercitos(1, pais);
                    } catch (ExcepcionRISK e) {
                        System.err.println(e.toString());
                    }
                event.setDropCompleted(true);
            }

        });
    }

    return this;
    }

    public MundoBuilder setActionClick(Function<Pais, EventHandler<Event>> f) {

        for (Entry<Pais, SVGPath> e : svgPaths.entrySet()) {
            EventHandler<Event> eh = f.apply(e.getKey());
            e.getValue().setOnMouseClicked(eh);
        }

        return this;
    }
}
