package riskgui;

import java.net.URL;
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
import risk.Mapa;
import risk.Partida;
import risk.riskexception.ExcepcionJugador;

public class RepartoPaisesController {
    @FXML
    private Pane panelMapa;
    @FXML
    private VBox vBox;
    @FXML
    private Button bSiguiente;

    private Mundo mundo;

    public void initialize() {
        mundo = new MundoBuilder().setActionClick((p) -> {
            return new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    if (p.getJugador() == null && Partida.getPartida().getJugadorActual().getNumEjercitosRearme() > 0) {
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
                        Alert alerta = new Alert(AlertType.INFORMATION, "El país ya pertenece a un jugador",
                                ButtonType.CLOSE);
                        alerta.setTitle("No se puede hacer la asignación");
                        alerta.setHeaderText(null);
                        alerta.show();
                    }
                }

            };
        }).get();

        panelMapa.getChildren().add(mundo.getWorldStackPane());
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
        }
    }

    public void avanzar() {

    }
}
