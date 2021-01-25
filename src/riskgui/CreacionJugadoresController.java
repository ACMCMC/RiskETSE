package riskgui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
import javafx.stage.FileChooser;
import risk.RiskColor;
import risk.Jugador;
import risk.Partida;
import risk.riskexception.ExcepcionGeo;
import risk.riskexception.ExcepcionJugador;
import risk.riskexception.ExcepcionRISK;
import risk.riskexception.RiskExceptionEnum;

public class CreacionJugadoresController {
    @FXML
    private Button bAnadir;
    @FXML
    private Button bSiguiente;
    @FXML
    private TextField campoNombreJugador;
    @FXML
    private ChoiceBox<risk.RiskColor> choiceBoxColores;
    @FXML
    private ProgressBar barraProgreso;
    @FXML
    private Button bCargarDesdeArchivo;

    public void initialize() {
        Set<RiskColor> colores = new HashSet<>(Arrays.asList(RiskColor.class.getEnumConstants()));
        colores.remove(RiskColor.INDEFINIDO);
        ObservableList<RiskColor> listaColores = FXCollections.observableArrayList(colores);
        choiceBoxColores.setItems(listaColores);
        choiceBoxColores.getSelectionModel().selectFirst();
        bSiguiente.setVisible(false);
    }

    public void anadirJug() {
        RiskColor color = choiceBoxColores.getValue();
        String nombre = campoNombreJugador.getText();
        if (color != null && !nombre.equals("")) {
            try {
                Partida.getPartida().addJugador(new Jugador(nombre, color));
                barraProgreso.setProgress(((double) Partida.getPartida().getJugadores().size()) / ((double) 6));
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
        if (Partida.getPartida().getJugadores().size() == 6) {
            bAnadir.setDisable(true);
        }
        bSiguiente.setVisible(Partida.getPartida().areJugadoresCreados());
        
        bCargarDesdeArchivo.setDisable(true);
    }

    public void siguiente() {
        Partida.getPartida().asignarEjercitosSinRepartir();
        Main.goToAsignacionMisiones();
    }

    public void cargarDesdeArchivo() {
        File file;
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(Main.getStage());

        if (file != null) {

            try {
                BufferedReader inputReader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

                String linea;
                String[] partesLinea;

                while ((linea = inputReader.readLine()) != null) {
                    partesLinea = linea.split(";");
                    try {
                        RiskColor color = RiskColor.getColorByString(partesLinea[1]);
                        Partida.getPartida().addJugador(new Jugador(partesLinea[0], color));
                    } catch (ExcepcionRISK e) {
                        Alert alerta = new Alert(AlertType.ERROR);
                        alerta.setTitle("Formato incorrecto");
                        alerta.setHeaderText(null);
                        alerta.setContentText(e.getMessage());
                        alerta.showAndWait();
                    }
                }

                inputReader.close();
            } catch (FileNotFoundException fileNotFoundException) {
                Alert alerta = new Alert(AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText(null);
                alerta.setContentText(RiskExceptionEnum.ARCHIVO_NO_EXISTE.get().getMessage());
                alerta.showAndWait();
            } catch (IOException e) {
                Alert alerta = new Alert(AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText(null);
                alerta.setContentText(RiskExceptionEnum.NO_SE_HA_PODIDO_LEER.get().getMessage());
                alerta.showAndWait();
            } catch (ArrayIndexOutOfBoundsException e) {
                Alert alerta = new Alert(AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText(null);
                alerta.setContentText(RiskExceptionEnum.FORMATO_ARCHIVO_INCORRECTO.get().getMessage());
                alerta.showAndWait();
            }

            siguiente();
        }
    }
}
