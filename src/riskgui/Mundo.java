package riskgui;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import risk.Mapa;
import risk.Pais;
import risk.riskexception.ExcepcionGeo;

public class Mundo {

    private Properties props;
    Map<Pais, SVGPath> paths;
    Map<Pais, Group> numeros;
    Map<Pais, Label> labels;
    Set<Pais> paises;
    private StackPane stackPane;

    public Mundo() {
        cargarMundo();
        procesarPaths();
        anadirEtiquetas();
        anadirNumeros();
        crearStackPane();
    }

    public StackPane getWorldStackPane() {
        return this.stackPane;
    }

    private void crearStackPane() {
        stackPane = new StackPane();
        Group capaPaises = new Group();
        Group capaLabels = new Group();
        Group capaNumeros = new Group();
        capaPaises.getChildren().addAll(paths.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList()));
        capaLabels.getChildren().addAll(labels.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList()));
        capaNumeros.getChildren().addAll(numeros.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList()));
        stackPane.getChildren().add(capaPaises);
        stackPane.getChildren().add(capaLabels);
        stackPane.getChildren().add(capaNumeros);
    }

    private void procesarPaths() {
        paths = new HashMap<>();
        paises = new HashSet<>();
        for (Entry<Object, Object> entrada : props.entrySet()) {
            SVGPath svgpath = new SVGPath();
            svgpath.setContent(entrada.getValue().toString());
            Pais pais;
            try {
                pais = Mapa.getMapa().getPais(entrada.getKey().toString());
                paths.put(pais, svgpath);
                paises.add(pais);
            } catch (ExcepcionGeo e) {
                System.err.println(e.toString());
            }
        }
    }

    private void anadirEtiquetas() {
        labels = new HashMap<>();
        for (Entry<Pais, SVGPath> entrada : paths.entrySet()) {
            Label etiqueta = new Label(entrada.getKey().getNombreHumano());
            Point2D bds = entrada.getValue().localToScene(0,0);
            etiqueta.layoutYProperty().set(bds.getY());
            labels.put(entrada.getKey(), etiqueta);
        }
    }
    
    private void anadirNumeros() {
        numeros = new HashMap<>();
        for (Entry<Pais, SVGPath> entrada : paths.entrySet()) {
            Label num = new Label(Integer.toString(entrada.getKey().getNumEjercitos()));
            Group etiqueta = new Group();
            etiqueta.getChildren().add(num);
            numeros.put(entrada.getKey(), etiqueta);
        }
    }

    private void cargarMundo() {
        props = new Properties();
        try (InputStream streamProps = Thread.currentThread().getContextClassLoader().getResourceAsStream("riskgui/paises.properties")) {
            props.load(streamProps);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
