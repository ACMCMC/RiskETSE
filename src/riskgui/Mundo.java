package riskgui;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeType;
import risk.Mapa;
import risk.Pais;
import risk.riskexception.ExcepcionGeo;

public class Mundo {

    private Properties props;
    private Pane pathsPaises;
    private Pane labelsNombresPane;
    private StackPane stackPane;
    private Set<Pais> paises;
    private HashMap<Pais, SVGPath> svgPaths;
    private HashMap<Pais, Label> labelsNombres;

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
    }

    private void crearYAnadirSVGPath(String nombrePais, String path) {
        SVGPath svgpath = new SVGPath();
            svgpath.setContent(path);
            svgpath.fillProperty().set(Color.RED);
            svgpath.setStroke(Color.BLACK);
            svgpath.setStrokeWidth(4);
            svgpath.setStrokeType(StrokeType.CENTERED);
            svgpath.setOnMouseEntered(new EventHandler<Event>(){

                @Override
                public void handle(Event event) {
                    svgpath.fillProperty().set(Color.ROYALBLUE);
                }
                
            });
            svgpath.setOnMouseExited(new EventHandler<Event>(){

                @Override
                public void handle(Event event) {
                    svgpath.fillProperty().set(Color.RED);
                }
                
            });
            svgpath.setPickOnBounds(false);
            try {
                Pais pais = Mapa.getMapa().getPais(nombrePais);
                Label labelNombre = new Label(pais.getNombreHumano());
                svgPaths.put(pais, svgpath);
                labelsNombres.put(pais, labelNombre);
                pathsPaises.getChildren().add(svgpath);
                labelsNombresPane.getChildren().add(labelNombre);

                labelNombre.layoutXProperty().set(svgpath.getLayoutBounds().getMinX());
                labelNombre.layoutYProperty().set(svgpath.getLayoutBounds().getMinY());
                labelNombre.setMaxWidth(svgpath.getLayoutBounds().getMaxX() - svgpath.getLayoutBounds().getMinX());
                labelNombre.setMaxHeight(svgpath.getLayoutBounds().getMaxY() - svgpath.getLayoutBounds().getMinY());
                labelNombre.alignmentProperty().set(Pos.CENTER);
                labelNombre.setBackground(new Background(new BackgroundFill(Color.ROSYBROWN, CornerRadii.EMPTY, Insets.EMPTY)));
            } catch (ExcepcionGeo e) {
                System.err.println(e.toString());
                System.err.println(nombrePais);
            }
    }

    private void procesarPaths() {
        pathsPaises = new Pane();
        labelsNombresPane = new Pane();
        labelsNombresPane.setMouseTransparent(true);
        svgPaths = new HashMap<>();
        labelsNombres = new HashMap<>();
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
