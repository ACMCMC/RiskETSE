package riskgui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import risk.Mapa;
import risk.Partida;
import risk.cartasmision.PaisEvent;
import risk.cartasmision.PaisEventSubscriber;
import risk.riskexception.ExcepcionGeo;
import risk.riskexception.ExcepcionJugador;

public class RepartoEjercitosController {
    @FXML
    private Pane panelMapa;
    @FXML
    private VBox vBox;
    @FXML
    private Button bRepartirAuto;

    private Mundo mundo;

    public void initialize() {
        mundo = new MundoBuilder().setActionClick((p) -> {
            return new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    if (p.getJugador() != null) {
                        try {
                            p.getJugador().asignarEjercitosAPais(1, p);
                            bRepartirAuto.setDisable(true);
                        } catch (ExcepcionJugador e) {
                            Alert alerta = new Alert(AlertType.INFORMATION, e.getMessage(), ButtonType.CLOSE);
                            alerta.setTitle("No se puede hacer el reparto");
                            alerta.setHeaderText(null);
                            alerta.show();
                        }
                    }
                }

            };
        }).get();

        panelMapa.getChildren().add(mundo.getWorldStackPane());

        /*
         * imgSoldado.setCursor(Cursor.HAND); imgSoldado.setOnDragDetected(new
         * EventHandler<Event>() {
         * 
         * @Override public void handle(Event event) { Dragboard dragboard =
         * imgSoldado.startDragAndDrop(TransferMode.MOVE); Map<DataFormat, Object> mapa
         * = new HashMap<DataFormat, Object>(); dragboard.setContent(mapa);
         * 
         * URL url = getClass().getResource("resources/soldado.png");
         * 
         * Image imagenSoldado = new Image(url.toExternalForm(), 50, 50, true, true);
         * dragboard.setDragView(imagenSoldado);
         * 
         * event.consume(); } });
         */

        Mapa.getMapa().getPaisEventPublisher().subscribe(new PaisEventSubscriber() {

            @Override
            public void update(PaisEvent evento) {
                if (Partida.getPartida().areEjercitosRepartidos()) {
                    Mapa.getMapa().getPaisEventPublisher().unsubscribe(this);
                    Main.goToPartida();
                }
            }

        });

    }

    public void repartirAuto() {
        try {
            Partida.getPartida().repartirEjercitos();
        } catch (ExcepcionGeo e) {
        }
    }
}
