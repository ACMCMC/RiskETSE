package riskgui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    public void volverAtras() {
        Main.goToPantallaInicial();
    }

    public void jugar() {
        Main.goToCreacionJugadores();
    }

    public void cargarArchivo() {
        Stage cargarArchivoStage = new CargarArchivoStage();
        cargarArchivoStage.initOwner(Main.getStage());
        cargarArchivoStage.initModality(Modality.APPLICATION_MODAL);
        cargarArchivoStage.show();
    }

}
