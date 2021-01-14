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
import javafx.stage.Modality;
import javafx.stage.Stage;
import risk.Dado;
import risk.riskexception.ExcepcionPropia;

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
            return new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                }

            };
        }).get();

        panelMapa.getChildren().add(mundo.getWorldStackPane());
    }

    public void accionPrueba() {
        try {
            Stage cargarArchivoStage = new DadoStage(new Dado(6));
            cargarArchivoStage.initOwner(Main.getStage());
            cargarArchivoStage.initModality(Modality.APPLICATION_MODAL);
            cargarArchivoStage.show();
            Stage cargarArchivoStage2 = new DadoStage(new Dado(5));
            cargarArchivoStage2.initOwner(Main.getStage());
            cargarArchivoStage2.initModality(Modality.APPLICATION_MODAL);
            cargarArchivoStage2.show();
            Stage cargarArchivoStage3 = new DadoStage(new Dado(3));
            cargarArchivoStage3.initOwner(Main.getStage());
            cargarArchivoStage3.initModality(Modality.APPLICATION_MODAL);
            cargarArchivoStage3.show();
        } catch (ExcepcionPropia e) {
            e.printStackTrace();
        }
    }
    
}
