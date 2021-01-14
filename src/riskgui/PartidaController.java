package riskgui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import risk.Dado;
import risk.Mapa;
import risk.Pais;
import risk.Partida;
import risk.cartasmision.PaisEvent;
import risk.cartasmision.PaisEventSubscriber;
import risk.riskexception.ExcepcionJugador;
import risk.riskexception.ExcepcionRISK;

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
    private Button bSiguienteTurno;
    @FXML
    private ToolBar toolBar;
    @FXML
    private Pane panelMapa;
    @FXML
    private Text tJugActual;
    @FXML
    private Text tAyuda;

    private Estado estado;
    private MundoBuilder mundoBuilder;
    private PaisEventSubscriber paisEventSubscriber;

    private enum Estado {
        JUGANDO_REPARTIENDO_EJERCITOS, JUGANDO_ATACANDO, JUGANDO_REARMANDO
    }

    public void initialize() {
        mundoBuilder = new MundoBuilder();

        panelMapa.getChildren().add(mundoBuilder.get().getWorldStackPane());

        paisEventSubscriber = new PaisEventSubscriber() {
            @Override
            public void update(PaisEvent evento) {
                procesarEstado();
            }
        };
        Mapa.getMapa().getPaisEventPublisher().subscribe(paisEventSubscriber);

        siguienteTurno();
    }

    public void procesarEstado() {
        if (estado.equals(Estado.JUGANDO_ATACANDO)) {
            bRearmar.setDisable(false);
            bAtacar.setDisable(true);
            bAsignarCarta.setDisable(true);
            bSiguienteTurno.setDisable(false);
            if (Partida.getPartida().getTurnoActual().hasJugadorConquistadoPais()) {
                bAsignarCarta.setDisable(false);
            } else {
                bAsignarCarta.setDisable(true);
            }
            
            tAyuda.setText("Arrastra el ratón sobre un país enemigo para atacarlo.");
        } else if (estado.equals(Estado.JUGANDO_REPARTIENDO_EJERCITOS)) {
            bRearmar.setDisable(true);
            bSiguienteTurno.setDisable(true);
            bAsignarCarta.setDisable(true);
            
            if (!Partida.getPartida().getJugadorActual().hasEjercitosSinRepartir()) {
                if (Partida.getPartida().getJugadorActual().getCartasEquipamiento().size()>=3) {
                    bCambiarCartas.setDisable(false);
                    tAyuda.setText("Haz un cambio de cartas para conseguir más ejércitos de rearme, o empieza a atacar.");
                } else {
                    tAyuda.setText("No tienes cartas suficientes para cambiar. Ataca países enemigos para conseguir más.");
                    bCambiarCartas.setDisable(true);
                }
                bAtacar.setDisable(false);
            } else {
                bAtacar.setDisable(true);
                tAyuda.setText("Reparte tus ejércitos entre tus países. (Quedan "
                        + Integer.toString(Partida.getPartida().getJugadorActual().getNumEjercitosRearme()) + ")");
            }
        } else if (estado.equals(Estado.JUGANDO_REARMANDO)) {
            bRearmar.setDisable(true);
            bAtacar.setDisable(true);
            bCambiarCartas.setDisable(true);
            bAsignarCarta.setDisable(true);
            bSiguienteTurno.setDisable(false);

            tAyuda.setText("Arrastra los ejércitos entre tus países para realizar un rearme.");
        }
    }

    private void actualizarJugActual() {
        tJugActual.setText("Jugando: " + Partida.getPartida().getJugadorActual().getNombre());
    }

    public void atacar() {
        this.estado = Estado.JUGANDO_ATACANDO;
        procesarEstado();

        mundoBuilder.setActionClick((p) -> null);

        Function<Entry<Pais, SVGPath>, EventHandler<MouseEvent>> fOnDragDetected = (
                Entry<Pais, SVGPath> e) -> new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (e.getKey().getNumEjercitos() > 1) {
                            Dragboard dragboard = e.getValue().startDragAndDrop(TransferMode.MOVE);

                            Map<DataFormat, Object> mapa = new HashMap<DataFormat, Object>();
                            mapa.put(MundoBuilder.dataFormatPais, e.getKey().getCodigo());

                            dragboard.setContent(mapa);

                            URL url = getClass().getResource("resources/soldado.png");

                            Image imagenSoldado = new Image(url.toExternalForm(), 50, 50, true, true);
                            dragboard.setDragView(imagenSoldado);
                        }

                        event.consume();
                    }
                };
        Function<Entry<Pais, SVGPath>, EventHandler<DragEvent>> fOnDragOver = (
                Entry<Pais, SVGPath> e) -> new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        if (!event.getGestureSource().equals(e.getValue())) {
                            event.acceptTransferModes(TransferMode.ANY);
                        }
                        event.consume();
                    }
                };
        Function<Entry<Pais, SVGPath>, EventHandler<DragEvent>> fOnDragDropped = (
                Entry<Pais, SVGPath> e) -> new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        Dragboard dragboard = event.getDragboard();
                        String paisOrigen = (String) dragboard.getContent(MundoBuilder.dataFormatPais);
                        if (paisOrigen != null) {
                            try {
                                PartidaController.this.realizarAtaque(Mapa.getMapa().getPais(paisOrigen), e.getKey());
                            } catch (ExcepcionRISK e) {
                                Alert alerta = new Alert(AlertType.INFORMATION, e.getMessage(), ButtonType.CLOSE);
                                alerta.setTitle("No se puede atacar");
                                alerta.setHeaderText(null);
                                alerta.show();
                            }
                        }
                        event.setDropCompleted(true);
                    }
                };

        mundoBuilder.setDragNDrop(fOnDragDetected, fOnDragOver, fOnDragDropped);
    }

    public void rearmar() {
        this.estado = Estado.JUGANDO_REARMANDO;
        procesarEstado();

        Function<Entry<Pais, SVGPath>, EventHandler<MouseEvent>> fOnDragDetected = (
                Entry<Pais, SVGPath> e) -> new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (e.getKey().getNumEjercitos() > 1) {
                            Dragboard dragboard = e.getValue().startDragAndDrop(TransferMode.MOVE);

                            Map<DataFormat, Object> mapa = new HashMap<DataFormat, Object>();
                            mapa.put(MundoBuilder.dataFormatPais, e.getKey().getCodigo());

                            dragboard.setContent(mapa);

                            URL url = getClass().getResource("resources/soldado.png");

                            Image imagenSoldado = new Image(url.toExternalForm(), 50, 50, true, true);
                            dragboard.setDragView(imagenSoldado);
                        }

                        event.consume();
                    }
                };
        Function<Entry<Pais, SVGPath>, EventHandler<DragEvent>> fOnDragOver = (
                Entry<Pais, SVGPath> e) -> new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        if (!event.getGestureSource().equals(e.getValue())) {
                            event.acceptTransferModes(TransferMode.ANY);
                        }
                        event.consume();
                    }
                };
        Function<Entry<Pais, SVGPath>, EventHandler<DragEvent>> fOnDragDropped = (
                Entry<Pais, SVGPath> e) -> new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        Dragboard dragboard = event.getDragboard();
                        String paisOrigen = (String) dragboard.getContent(MundoBuilder.dataFormatPais);
                        if (paisOrigen != null) {
                            try {
                                Partida.getPartida().rearmar(Mapa.getMapa().getPais(paisOrigen), e.getKey(), 1);
                            } catch (ExcepcionRISK e) {
                                Alert alerta = new Alert(AlertType.INFORMATION, e.getMessage(), ButtonType.CLOSE);
                                alerta.setTitle("No se puede rearmar");
                                alerta.setHeaderText(null);
                                alerta.show();
                            }
                        }
                        event.setDropCompleted(true);
                    }
                };

        mundoBuilder.setDragNDrop(fOnDragDetected, fOnDragOver, fOnDragDropped);
    }

    public void realizarAtaque(Pais p1, Pais p2) {
        try {
            Map<Pais, Set<Dado>> dados = Partida.getPartida().atacar(p1, p2);
            for (Dado dado : dados.get(p1)) {
                Stage cargarArchivoStage = new DadoStage(dado, Color.WHITE);
                cargarArchivoStage.initOwner(Main.getStage());
                cargarArchivoStage.initModality(Modality.APPLICATION_MODAL);
                cargarArchivoStage.show();
            }
            for (Dado dado : dados.get(p2)) {
                Stage cargarArchivoStage = new DadoStage(dado, Color.rgb(255, 200, 200));
                cargarArchivoStage.initOwner(Main.getStage());
                cargarArchivoStage.initModality(Modality.APPLICATION_MODAL);
                cargarArchivoStage.show();
            }
        } catch (ExcepcionRISK e) {
            Alert alerta = new Alert(AlertType.INFORMATION, e.getMessage(), ButtonType.CLOSE);
            alerta.setTitle("No se puede atacar");
            alerta.setHeaderText(null);
            alerta.show();
        }
    }

    public void siguienteTurno() {
        if (Partida.getPartida().getJugadorActual().jugadorHaCompletadoMision()) {
            Alert alerta = new Alert(AlertType.INFORMATION,
                    Partida.getPartida().getJugadorActual().getNombre() + " ya ha completado su misión.",
                    ButtonType.CLOSE);
            alerta.setTitle("Misión completada");
            alerta.setHeaderText(null);
            alerta.show();
        }
        this.estado = Estado.JUGANDO_REPARTIENDO_EJERCITOS;
        Partida.getPartida().siguienteTurno();
        procesarEstado();
        actualizarJugActual();

        mundoBuilder.setDragNDrop(e -> null, e -> null, e -> null);

        mundoBuilder.setActionClick((p) -> new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                try {
                    Partida.getPartida().repartirEjercitos(1, p);
                } catch (ExcepcionJugador e) {
                    Alert alerta = new Alert(AlertType.WARNING, e.getMessage(), ButtonType.CLOSE);
                    alerta.setTitle("No se puede repartir");
                    alerta.setHeaderText(null);
                    alerta.show();
                }
            }
        });
    }

    public void cambiarCartas() {
        Stage cambiarCartasStage = new CambiarCartasStage();
        cambiarCartasStage.initOwner(Main.getStage());
        cambiarCartasStage.initModality(Modality.APPLICATION_MODAL);
        cambiarCartasStage.showAndWait();
        procesarEstado();
    }
    public void asignarCarta() {
        rearmar();
        
        Stage asignarCartaStage = new AsignarCartaStage();
        asignarCartaStage.initOwner(Main.getStage());
        asignarCartaStage.initModality(Modality.APPLICATION_MODAL);
        asignarCartaStage.show();
    }

}
