package riskgui;

import java.io.File;
import java.util.Collection;
import java.util.Properties;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.SVGPath;

public class Mundo {

    private static final String pathToVideoFondo = "resources/videoFondo.mp4";
    private Properties props;
    private Pane pathsPaises;
    private Pane labelsNombresPane;
    private Pane labelsNumEjercitosPane;
    private StackPane stackPane;
    private MediaView fondo;
    
    public Mundo(Collection<SVGPath> svgPaths, Collection<Label> nombresPaises, Collection<Label> numsEjercitos) {
        pathsPaises = new Pane();
        pathsPaises.getChildren().addAll(svgPaths);

        labelsNombresPane = new Pane();
labelsNombresPane.getChildren().addAll(nombresPaises);

        labelsNumEjercitosPane = new Pane();
        labelsNumEjercitosPane.getChildren().addAll(numsEjercitos);

        labelsNombresPane.setMouseTransparent(true);
        labelsNumEjercitosPane.setMouseTransparent(true);

        crearStackPane();
        ajustarTamanoStackPane(svgPaths);
    }

    public StackPane getWorldStackPane() {
        return this.stackPane;
    }

    private void crearStackPane() {
        stackPane = new StackPane();
        stackPane.getChildren().add(pathsPaises);
        stackPane.getChildren().add(labelsNombresPane);
        stackPane.getChildren().add(labelsNumEjercitosPane);
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

    private void ajustarTamanoStackPane(Collection<SVGPath> svgPaths) {
        double widthMax;
        double heightMax;
        widthMax = svgPaths.stream()
                .mapToDouble(svg -> svg.getLayoutBounds().getMaxX()).max().orElse(0);
        heightMax = svgPaths.stream()
                .mapToDouble(svg -> svg.getLayoutBounds().getMaxY()).max().orElse(0);
        stackPane.setMinWidth(widthMax);
        stackPane.setMaxWidth(widthMax);
        stackPane.setPrefWidth(widthMax);
        stackPane.setMinHeight(heightMax);
        stackPane.setMaxHeight(heightMax);
        stackPane.setPrefHeight(heightMax);
    }

}
