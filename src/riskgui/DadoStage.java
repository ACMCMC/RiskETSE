package riskgui;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import risk.Dado;
import risk.Pais;

public class DadoStage extends Stage {

    private final static int ESCALA = 15;
    private final Dado dado;

    public DadoStage(Dado dado) {
        super();

        this.dado = dado;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dado.fxml"));
            MeshView meshView = fxmlLoader.load();

            meshView.setScaleX(ESCALA);
            meshView.setScaleY(ESCALA);
            meshView.setScaleZ(ESCALA);

            meshView.setMaterial(new PhongMaterial(Color.WHITESMOKE));
            meshView.setDrawMode(DrawMode.FILL);

            
            Group grupo = new Group(meshView);

            AnchorPane anchorPane = new AnchorPane();
            anchorPane.getChildren().add(grupo);
            anchorPane.setBackground(Background.EMPTY);
            
            Scene escena = new Scene(anchorPane, Main.getStage().getWidth(), Main.getStage().getHeight());
            
            PerspectiveCamera camara = new PerspectiveCamera(false);
            camara.setTranslateX(0);
            camara.setTranslateY(0);
            camara.setTranslateZ(-100);
            camara.setFieldOfView(30);
            camara.setNearClip(0);

            PointLight light = new PointLight(Color.WHITE);
            light.setTranslateX(escena.getWidth()/2);
            light.setTranslateY(escena.getHeight()/2);
            light.setTranslateZ(-300);
            
            grupo.getChildren().add(light);
            escena.setCamera(camara);

            
            this.setScene(escena);
            this.initStyle(StageStyle.TRANSPARENT);
            escena.setFill(Color.TRANSPARENT);
            
            RotateTransition rotateTransition = new RotateTransition(Duration.millis(1000));
            rotateTransition.setNode(meshView);
            rotateTransition.setAxis(getEje());
            rotateTransition.setInterpolator(Interpolator.SPLINE(0.3, 0.1, 0.9, 0.45));
            rotateTransition.setFromAngle(0);
            rotateTransition.setToAngle(getAngulo());
            
            meshView.setTranslateX(escena.getWidth()/2 -250 +Math.random()*500);
            meshView.setTranslateY(escena.getHeight()/2 -250 +Math.random()*500);

            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000), meshView);
            scaleTransition.setFromX(ESCALA);
            scaleTransition.setFromY(ESCALA);
            scaleTransition.setFromZ(ESCALA);
            scaleTransition.setToX(ESCALA/5);
            scaleTransition.setToY(ESCALA/5);
            scaleTransition.setToZ(ESCALA/5);
            scaleTransition.setInterpolator(Interpolator.SPLINE(0.3, 0.1, 0.85, 0.50));

            FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(1), this.getScene().getRoot());
            fadeOutTransition.setInterpolator(Interpolator.EASE_BOTH);
            fadeOutTransition.setFromValue(1.0);
            fadeOutTransition.setToValue(0.0);

            ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, rotateTransition);

            SequentialTransition sequentialTransition = new SequentialTransition();
            sequentialTransition.getChildren().add(parallelTransition);
            sequentialTransition.getChildren().add(new PauseTransition(Duration.millis(3000)));
            sequentialTransition.getChildren().add(fadeOutTransition);

            sequentialTransition.play();
            
            sequentialTransition.setOnFinished(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                    DadoStage.this.close();
                }
            });
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Point3D getEje() {
        if (dado.getValor()==6 || dado.getValor()==1) {
            return Rotate.X_AXIS;
        } else {
            return Rotate.Y_AXIS;
        }
    }

    private int getAngulo() {
        switch (dado.getValor()) {
            case 1:
            return 270;
            case 2:
            return 270;
            case 3:
            return 180;
            case 4:
            return 360;
            case 5:
            return 90;
            case 6:
            return 90;
            default: 
            return 0;
        }
    }
}
