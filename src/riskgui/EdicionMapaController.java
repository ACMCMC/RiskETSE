package riskgui;

import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import risk.Mapa;
import risk.Partida;

public class EdicionMapaController {
    @FXML
    private Pane panelMapa;
    @FXML
    private ToolBar toolbar;
    @FXML
    private VBox vBox;
    @FXML
    private Button bSiguiente;

    private Mundo mundo;

    public void initialize() {
        mundo = new MundoBuilder().setActionClick((p) -> {
            return new EventHandler<Event>(){

				@Override
				public void handle(Event event) {
                    if (p.getJugador()==null) {
                        p.conquistar(Partida.getPartida().getJugadorActual());
                    }
				}
                
            };
        }).get();

        panelMapa.getChildren().add(mundo.getWorldStackPane());
    }

    public void siguiente() {
        Partida.getPartida().siguienteTurnoDeReparto();
        if (Mapa.getMapa().arePaisesAsignados()) {
            System.out.println("dasdasdas");
        }
    }

    public void avanzar() {

    }
}
