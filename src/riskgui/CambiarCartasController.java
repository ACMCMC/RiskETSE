package riskgui;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import javafx.util.Callback;
import risk.Partida;
import risk.cartas.CambioCartas;
import risk.cartas.Carta;
import risk.riskexception.ExcepcionCarta;

public class CambiarCartasController {
    @FXML
    private ListView<Carta> listaCartas;
    @FXML
    private Button bCambiar;

    private Stage stage;

    public void initialize() {
        listaCartas.setCellFactory(new Callback<ListView<Carta>, ListCell<Carta>>(){
            @Override
            public ListCell<Carta> call(ListView<Carta> param) {
                ListCell<Carta> celda = new ListCell<Carta>() {
                    @Override
                    protected void updateItem(Carta item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item!=null) {
                            setText(item.getNombre());
                        }
                    }
                };
                return celda;
            }
        });
        listaCartas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listaCartas.setItems(
                FXCollections.observableArrayList(Partida.getPartida().getJugadorActual().getCartasEquipamiento()));
        listaCartas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Carta>() {

            @Override
            public void changed(ObservableValue<? extends Carta> observable, Carta oldValue, Carta newValue) {
                if (listaCartas.getSelectionModel().getSelectedItems().size()==3) {
                    bCambiar.setDisable(false);
                } else {
                    bCambiar.setDisable(true);
                }
            }
            
        });
    }

    public void cambiar() {
        try {
            Iterator<Carta> itCarta = listaCartas.getSelectionModel().getSelectedItems().iterator();
            CambioCartas cambio = new CambioCartas(itCarta.next(), itCarta.next(), itCarta.next(), Partida.getPartida().getJugadorActual());

            Partida.getPartida().getJugadorActual().cambiarCartasEquipamiento(cambio);
    
            Alert alerta = new Alert(AlertType.INFORMATION);
                alerta.setTitle("Cartas cambiadas");
                alerta.setHeaderText(null);
                alerta.setContentText("Se ha realizado el cambio de cartas correctamente.");
                alerta.showAndWait();
        } catch (ExcepcionCarta | NoSuchElementException e) {
            Alert alerta = new Alert(AlertType.ERROR);
                alerta.setTitle("Error cambiando cartas");
                alerta.setHeaderText(null);
                alerta.setContentText(e.getMessage());
                alerta.showAndWait();
        }
        stage.close();
    }

    public void cambioOptimo() {
        try {
            CambioCartas cambio = Partida.getPartida().getJugadorActual().realizarCambioOptimoDeCartasDeEquipamiento();

            Alert alerta = new Alert(AlertType.INFORMATION);
                alerta.setTitle("Error cambiando cartas");
                alerta.setHeaderText(null);
                alerta.setContentText("Se han cambiado: " + cambio.getCarta1().getNombre() + ", " + cambio.getCarta2().getNombre() + " y " + cambio.getCarta3().getNombre() + ".");
                alerta.showAndWait();
        } catch (ExcepcionCarta e) {
            Alert alerta = new Alert(AlertType.ERROR);
                alerta.setTitle("Error cambiando cartas");
                alerta.setHeaderText(null);
                alerta.setContentText(e.getMessage());
                alerta.showAndWait();
        }
        stage.close();
    }

    public void cancelar() {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
