package riskgui;

import javafx.beans.value.ObservableStringValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;

public class PartidaController {
    @FXML
    private Button bRearmar;
    @FXML
    private Button bAtacar;
    @FXML
    private Button bAsignarCarta;
    @FXML
    private Button bCambiarCartas;
    @FXML
    private ToolBar toolBar;
    @FXML
    private Pane panelMapa;

    public void initialize() {
        Mundo mundo = new MundoBuilder().setActionClick((p) -> {
            return new EventHandler<Event>(){

				@Override
				public void handle(Event event) {
				}
                
            };
        }).get();

        panelMapa.getChildren().add(mundo.getWorldStackPane());
    }


    
}
