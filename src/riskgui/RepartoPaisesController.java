package riskgui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import risk.Mapa;
import risk.Partida;
import risk.riskexception.ExcepcionJugador;
import risk.riskexception.ExcepcionRISK;
import risk.riskexception.RiskExceptionEnum;

public class RepartoPaisesController {
    @FXML
    private Pane panelMapa;
    @FXML
    private VBox vBox;
    @FXML
    private Button bSiguiente;
    @FXML
    private Button bCargarDesdeArchivo;
    @FXML
    private Text tAyuda;

    private Mundo mundo;

    public void initialize() {
        mundo = new MundoBuilder().setActionClick((p) -> {
            return new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    bCargarDesdeArchivo.setDisable(true);
                    if (p.getJugador() == null && Partida.getPartida().getJugadorActual().hasEjercitosSinRepartir()) {
                        p.conquistar(Partida.getPartida().getJugadorActual());
                        try {
                            Partida.getPartida().repartirEjercitos(1, p);
                        } catch (ExcepcionJugador e) {
                            new Alert(AlertType.INFORMATION, e.getMessage(), ButtonType.CLOSE).show();
                        }
                    } /*
                    else if (Partida.getPartida().getJugadorActual().equals(p.getJugador())) {
                        try {
                            Partida.getPartida().repartirEjercitos(1, p);
                        } catch (ExcepcionJugador e) {
                            Alert alerta = new Alert(AlertType.INFORMATION, e.getMessage(), ButtonType.CLOSE);
                            alerta.setTitle("No se puede hacer el reparto");
                            alerta.setHeaderText(null);
                            alerta.show();
                        }
                    }
                    */ else {
                        Alert alerta = new Alert(AlertType.INFORMATION);
                        alerta.setTitle("No se puede hacer la asignación");
                        alerta.setHeaderText(null);
                        alerta.setResult(ButtonType.CLOSE);
                        if (!Partida.getPartida().getJugadorActual().hasEjercitosSinRepartir()) {
                            alerta.setContentText(RiskExceptionEnum.EJERCITOS_NO_DISPONIBLES.get().getMessage());
                        } else {
                            alerta.setContentText(RiskExceptionEnum.PAIS_YA_ASIGNADO.get().getMessage());
                        }
                        alerta.show();
                    }
                }

            };
        }).get();

        panelMapa.getChildren().add(mundo.getWorldStackPane());

        actualizarTextoAyuda();
    }

    public void siguiente() {
        if (Mapa.getMapa().arePaisesAsignados()) {
            if (Partida.getPartida().areEjercitosRepartidos()) {
                Partida.getPartida().siguienteTurno();
                Main.goToPartida();
            } else {
                Partida.getPartida().siguienteTurnoDeReparto();
                Main.goToRepartoEjercitos();
            }
        } else {
            Partida.getPartida().siguienteTurnoDeReparto();
            actualizarTextoAyuda();
        }
    }
    
    private void actualizarTextoAyuda() {
        tAyuda.setText("Haz clic sobre un país para asignárselo a " + Partida.getPartida().getJugadorActual().getNombre() + ".");
    }

    public void avanzar() {

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
                String[] partes;

                while ((linea = inputReader.readLine()) != null) {
                    partes = linea.split(";");
                    try {
                        String nombrePais = partes[1];
                        String nombreJugador = partes[0];
                        Mapa.getMapa().asignarPaisAJugadorInicialmente(nombrePais, nombreJugador);
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
