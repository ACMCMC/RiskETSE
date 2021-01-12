package riskgui;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Properties;
import java.util.Set;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
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
