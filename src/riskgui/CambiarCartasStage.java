package riskgui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CambiarCartasStage extends Stage {
    public CambiarCartasStage() {
        super();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cambiarCartas.fxml"));
            Parent root = fxmlLoader.load();

            fxmlLoader.<CambiarCartasController>getController().setStage(this);

            Scene escena = new Scene(root);

            this.setScene(escena);
    
            this.setTitle("Cambiar cartas");
        } catch (IOException e) {
        }
    }
}
