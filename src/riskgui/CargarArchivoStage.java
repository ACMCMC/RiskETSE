package riskgui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import risk.Pais;

public class CargarArchivoStage extends Stage {
    public CargarArchivoStage() {
        super();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cargarArchivo.fxml"));
            Parent root = fxmlLoader.load();

            Scene escena = new Scene(root);

            this.setScene(escena);
    
            this.setTitle("Cargar archivo de nombres");
        } catch (IOException e) {
        }
    }
}
