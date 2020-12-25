package riskgui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import risk.Color;
import risk.Jugador;
import risk.Partida;
import risk.riskexception.ExcepcionGeo;
import risk.riskexception.ExcepcionJugador;

public class CreacionJugadoresController {
    @FXML
    private Button bAnadir;
    @FXML
    private Button bSiguiente;
    @FXML
    private TextField campoNombreJugador;
    @FXML
    private ChoiceBox<risk.Color> choiceBoxColores;
    @FXML
    private ProgressBar barraProgreso;

    public void initialize() {
        Set<Color> colores = new HashSet<>(Arrays.asList(Color.class.getEnumConstants()));
        colores.remove(Color.INDEFINIDO);
        ObservableList<Color> listaColores = FXCollections.observableArrayList(colores);
        choiceBoxColores.setItems(listaColores);
        bSiguiente.setVisible(false);
    }

    public void anadirJug() {
        Color color = choiceBoxColores.getValue();
        String nombre = campoNombreJugador.getText();
        if (color != null && !nombre.equals("")) {
            try {
                Partida.getPartida().addJugador(new Jugador(nombre, color));
                barraProgreso.setProgress(((double)Partida.getPartida().getJugadores().size())/((double)6));
                choiceBoxColores.getSelectionModel().selectNext();
            } catch (ExcepcionJugador e) {
                Alert alerta = new Alert(AlertType.ERROR);
                alerta.setTitle("Error al crear el jugador");
                alerta.setHeaderText(null);
                alerta.setContentText(e.getMessage());
                alerta.show();
            } catch (ExcepcionGeo e) {
                e.printStackTrace();
            }
        }
        if (Partida.getPartida().getJugadores().size()==6) {
            bAnadir.setDisable(true);
        }
        bSiguiente.setVisible(Partida.getPartida().areJugadoresCreados());
    }

    public void siguiente() {
        Main.goToAsignacionMisiones();
    }
}
