package riskgui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import risk.Pais;

public class CustomizePaisStage extends Stage {
    public CustomizePaisStage(Pais p) {
        super();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("customizePais.fxml"));
            Parent root = fxmlLoader.load();

            Scene escena = new Scene(root);

            fxmlLoader.<CustomizePaisController>getController().setPais(p);

            this.setScene(escena);
    
            this.setTitle("Editar país");
        } catch (IOException e) {
        }
    }
}
