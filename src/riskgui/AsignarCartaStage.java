package riskgui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AsignarCartaStage extends Stage {
    public AsignarCartaStage() {
        super();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("asignarCarta.fxml"));
            Parent root = fxmlLoader.load();

            fxmlLoader.<AsignarCartaController>getController().setStage(this);

            Scene escena = new Scene(root);

            this.setScene(escena);
    
            this.setTitle("Asignar carta");
        } catch (IOException e) {
        }
    }
}
