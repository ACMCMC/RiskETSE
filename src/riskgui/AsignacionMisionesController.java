package riskgui;

import java.util.Comparator;
import java.util.NoSuchElementException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import risk.Jugador;
import risk.Partida;
import risk.cartasmision.CartaMision;
import risk.cartasmision.CartaMisionFactory;
import risk.riskexception.ExcepcionMision;
import risk.riskexception.ExcepcionRISK;

public class AsignacionMisionesController {
    @FXML
    private ListView<Jugador> listaJugadores;
    @FXML
    private ListView<Class<? extends CartaMision>> listaMisiones;
    @FXML
    private Button bSiguiente;

    public void initialize() {
        ObservableList<Jugador> listaJ = FXCollections.observableArrayList(Partida.getPartida().getJugadores());
        listaJugadores.setItems(listaJ);
        listaJugadores.setCellFactory(new Callback<ListView<Jugador>,ListCell<Jugador>>(){
            @Override
            public ListCell<Jugador> call(ListView<Jugador> param) {
                ListCell<Jugador> celda = new ListCell<Jugador>() {
                    @Override
                    protected void updateItem(Jugador item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item!=null) {
                            setText(item.getNombre());
                        }
                    }
                };
                return celda;
            }
        });
        
        listaMisiones.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Class<? extends CartaMision>> listaM = FXCollections
        .observableArrayList(CartaMisionFactory.getAll());
        SortedList<Class<? extends CartaMision>> listaMOrdenada = new SortedList<>(listaM);
        listaMOrdenada.setComparator(Comparator.comparing((Class<? extends CartaMision> c) -> c.getSimpleName()));
        listaMisiones.setItems(listaMOrdenada);
        listaMisiones.setCellFactory(new Callback<ListView<Class<? extends CartaMision>>, ListCell<Class<? extends CartaMision>>>(){
            @Override
            public ListCell<Class<? extends CartaMision>> call(ListView<Class<? extends CartaMision>> param) {
                ListCell<Class<? extends CartaMision>> celda = new ListCell<Class<? extends CartaMision>>() {
                    @Override
                    protected void updateItem(Class<? extends CartaMision> item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item!=null) {
                            setText(item.getSimpleName());
                        }
                    }
                };
                return celda;
            }
        });

        listaJugadores.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Jugador>() {
            @Override
            public void changed(ObservableValue<? extends Jugador> observable, Jugador oldValue, Jugador newValue) {
                handleClickOnJugador(newValue);
            }
        });
        listaMisiones.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Class<? extends CartaMision>>() {
                    @Override
                    public void changed(ObservableValue<? extends Class<? extends CartaMision>> observable,
                            Class<? extends CartaMision> oldValue, Class<? extends CartaMision> newValue) {
                        handleClickOnMision(newValue, oldValue);
                    }
                });
    }

    public void handleClickOnJugador(Jugador j) {
        try {
            listaMisiones.getSelectionModel().select(j.getCartaMision().getClass());
        } catch (NoSuchElementException e) {
            listaMisiones.getSelectionModel().clearSelection();
        }
    }

    public void handleClickOnMision(Class<? extends CartaMision> claseCartaMision,
            Class<? extends CartaMision> oldValue) {
        Jugador jSeleccionado = listaJugadores.getSelectionModel().getSelectedItem();
        if (jSeleccionado != null) {
            Class<? extends CartaMision> misionJug;
            try {
                misionJug = jSeleccionado.getCartaMision().getClass();
            } catch (NoSuchElementException e) {
                misionJug = null;
            }
            if (claseCartaMision!=null && !claseCartaMision.equals(misionJug)) {
                try {
                    CartaMision m = CartaMisionFactory.build(claseCartaMision.getSimpleName(), jSeleccionado);
                    Partida.getPartida().asignarMisionAJugador(m, jSeleccionado);
                } catch (ExcepcionRISK e) {
                    Alert alerta = new Alert(AlertType.WARNING);
                    alerta.setTitle("Error al asignar la misión");
                    alerta.setHeaderText(null);
                    alerta.setContentText(e.getMessage());
                    alerta.show();
                    listaMisiones.getSelectionModel().select(oldValue);
                }
            }
        }
        if (Partida.getPartida().areMisionesAsignadas()) {
            bSiguiente.setDisable(false);
        }
    }
}
