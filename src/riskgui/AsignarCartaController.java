package riskgui;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import risk.Mapa;
import risk.Pais;
import risk.Partida;
import risk.cartas.Antiaerea;
import risk.cartas.CambioCartas;
import risk.cartas.CambioCartasFactory;
import risk.cartas.Carta;
import risk.cartas.CartaEquipamientoFactory;
import risk.cartas.DeCaballo;
import risk.cartas.DeCamello;
import risk.cartas.DeCampanha;
import risk.cartas.Fusilero;
import risk.cartas.Granadero;
import risk.cartasmision.CartaMision;
import risk.riskexception.ExcepcionCarta;
import risk.riskexception.ExcepcionRISK;

public class AsignarCartaController {
    @FXML
    private ListView<Pais> listaPaises;
    @FXML
    private ListView<Class<? extends Carta>> listaTiposCarta;
    @FXML
    private Button bCambiar;

    private Stage stage;

    public void initialize() {
        listaPaises.setItems(FXCollections.observableArrayList(Mapa.getMapa().getPaises()));
        listaPaises.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pais>() {
            @Override
            public void changed(ObservableValue<? extends Pais> observable, Pais oldValue, Pais newValue) {
                actualizarBotonAsignar();
            }
        });
        List<Class<? extends Carta>> listaCartas = new ArrayList<>();
        listaCartas.add(Granadero.class);
        listaCartas.add(Fusilero.class);
        listaCartas.add(DeCaballo.class);
        listaCartas.add(DeCamello.class);
        listaCartas.add(Antiaerea.class);
        listaCartas.add(DeCampanha.class);
        listaTiposCarta.setCellFactory(new Callback<ListView<Class<? extends Carta>>,ListCell<Class<? extends Carta>>>(){
            @Override
            public ListCell<Class<? extends Carta>> call(ListView<Class<? extends Carta>> param) {
                ListCell<Class<? extends Carta>> celda = new ListCell<Class<? extends Carta>>() {
                    @Override
                    protected void updateItem(Class<? extends Carta> item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item!=null) {
                            setText(item.getSimpleName());
                        }
                    }
                };
                return celda;
            }
        });
        listaTiposCarta.setItems(FXCollections.observableArrayList(listaCartas));
        listaTiposCarta.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Class<? extends Carta>>() {
                    @Override
                    public void changed(ObservableValue<? extends Class<? extends Carta>> observable,
                            Class<? extends Carta> oldValue, Class<? extends Carta> newValue) {
                        actualizarBotonAsignar();
                    }
                });
    }

    public void actualizarBotonAsignar() {
        if (listaPaises.getSelectionModel().isEmpty()) {
            bCambiar.setDisable(true);
        } else if (listaTiposCarta.getSelectionModel().isEmpty()) {
            bCambiar.setDisable(true);
        } else {
            bCambiar.setDisable(false);
        }
    }

    public void asignarCarta() {
        try {
            Partida.getPartida().getJugadorActual().addCartaEquipamiento(
                    CartaEquipamientoFactory.get(listaTiposCarta.getSelectionModel().getSelectedItem().getSimpleName()
                            + "&" + listaPaises.getSelectionModel().getSelectedItem().getCodigo(), Mapa.getMapa()));
            stage.close();
        } catch (ExcepcionRISK e) {
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error asignando carta");
            alerta.setHeaderText(null);
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }
    }

    public void cancelar() {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
