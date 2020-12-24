package riskgui;

import javafx.beans.value.ObservableStringValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

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
    private ListView<String> listaJugadores;

    public void initialize() {
        bRearmar.setOnMouseClicked(new EventHandler<Event>(){

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				bRearmar.setText("Rearmado");
			}
            
        });

        listaJugadores.getItems().add("Jugador 1");
        listaJugadores.getItems().add("Jugador 2");
    }


    
}
