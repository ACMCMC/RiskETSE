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
import javafx.stage.Modality;
import javafx.stage.Stage;
import risk.Mapa;
import risk.Partida;

public class EdicionMapaController {
    @FXML
    private Pane panelMapa;

    private Mundo mundo;

    public void initialize() {
        mundo = new MundoBuilder().setDefaultEnterExitHandlers().setActionClick((p) -> {
            return new EventHandler<Event>(){

				@Override
				public void handle(Event event) {
                    Stage customizePaisStage = new CustomizePaisStage(p);
                    customizePaisStage.initOwner(Main.getStage());
                    customizePaisStage.initModality(Modality.APPLICATION_MODAL);
                    customizePaisStage.show();
				}
                
            };
        }).get();

        panelMapa.getChildren().add(mundo.getWorldStackPane());
    }

    public void siguiente() {
        Main.goToPantallaInicial();
    }

}
