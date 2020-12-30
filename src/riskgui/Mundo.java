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
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import risk.Mapa;
import risk.Pais;
import risk.riskexception.ExcepcionGeo;

public class Mundo {

    private Properties props;
    Set<PaisNode> paises;
    private StackPane stackPane;

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
        stackPane.getChildren().addAll(paises.stream().map(PaisNode::getPane).collect(Collectors.toSet()));
    }

    private void procesarPaths() {
        paises = new HashSet<>();
        for (Entry<Object, Object> entrada : props.entrySet()) {
            SVGPath svgpath = new SVGPath();
            svgpath.setContent(entrada.getValue().toString());
            svgpath.fillProperty().set(Paint.valueOf("red"));
            svgpath.setOnMouseEntered(new EventHandler<Event>(){

                @Override
                public void handle(Event event) {
                    svgpath.fillProperty().set(Color.CHOCOLATE);
                }
                
            });
            svgpath.setOnMouseExited(new EventHandler<Event>(){

                @Override
                public void handle(Event event) {
                    svgpath.fillProperty().set(Color.ROYALBLUE);
                }
                
            });
            svgpath.setPickOnBounds(false);
            Pais pais;
            PaisNode paisNode;
            try {
                pais = Mapa.getMapa().getPais(entrada.getKey().toString());
                paisNode = new PaisNode(pais, svgpath);
                paises.add(paisNode);
            } catch (ExcepcionGeo e) {
                System.err.println(e.toString());
                System.err.println(entrada.getKey());
            }
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
