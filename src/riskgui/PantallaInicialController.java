package riskgui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PantallaInicialController {
    @FXML
    private VBox vBox;
    @FXML
    private HBox hBox;
    @FXML
    private Button bJugar;
    @FXML
    private Button bEditar;
    @FXML
    private ImageView iRisk;

    public void initialize() {
        
    }

    public void jugar() {
        Main.goToCreacionJugadores();
    }

    public void editar() {
        Main.goToEditarMapa();
    }
}
