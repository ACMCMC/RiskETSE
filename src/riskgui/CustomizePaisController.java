package riskgui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import risk.Continente;
import risk.Mapa;
import risk.Pais;
import risk.Partida;
import risk.Risk;
import risk.RiskColor;

public class CustomizePaisController {
    @FXML
    private ChoiceBox<Continente> cbContinente;
    @FXML
    private TextField tfCodigo;
    @FXML
    private TextField tfNombre;
    @FXML
    private ChoiceBox<RiskColor> cbColorContinente;
    @FXML
    private TextField tfCodigoContinente;
    @FXML
    private TextField tfNombreContinente;

    Pais pais;

    public void initialize() {
    }

    private void registrarListeners() {
        tfNombre.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                pais.setNombreHumano(newValue);
            }
        });
        tfCodigo.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                pais.setCodigo(newValue);
            }
        });
        cbContinente.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Continente>() {
            @Override
            public void changed(ObservableValue<? extends Continente> observable, Continente oldValue,
                    Continente newValue) {
                pais.setContinente(newValue);
                actualizarDatosContinente();
            }
        });

        // Tab del continente
        tfNombreContinente.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                pais.getContinente().setNombreHumano(newValue);
            }
        });
        tfCodigoContinente.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                pais.getContinente().setCodigo(newValue);
            }
        });
        cbColorContinente.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RiskColor>() {
            @Override
            public void changed(ObservableValue<? extends RiskColor> observable, RiskColor oldValue,
                    RiskColor newValue) {
                        if (newValue!=null) {
                            pais.getContinente().setColor(newValue);
                        }
            }
        });
    }

    public void setPais(Pais p) {
        this.pais = p;
        actualizarDatos();
        actualizarDatosContinente();
        registrarListeners();
    }

    public void actualizarDatos() {
        tfCodigo.setText(pais.getCodigo());
        tfNombre.setText(pais.getNombreHumano());
        ObservableList<Continente> listaContinentes = FXCollections
        .observableArrayList(Mapa.getMapa().getContinentes());
        cbContinente.setItems(listaContinentes);
        cbContinente.getSelectionModel().select(pais.getContinente());
    }
    
    public void actualizarDatosContinente() {
        tfCodigoContinente.setText(pais.getContinente().getCodigo());
        tfNombreContinente.setText(pais.getContinente().getNombreHumano());
        Set<RiskColor> colores = new HashSet<>(Arrays.asList(RiskColor.class.getEnumConstants()));
        colores.remove(RiskColor.INDEFINIDO);
        ObservableList<RiskColor> listaColores = FXCollections.observableArrayList(colores);
        cbColorContinente.setItems(listaColores);
        cbColorContinente.getSelectionModel().select(pais.getContinente().getColor());
    }
}
