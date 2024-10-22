package riskgui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.NoSuchElementException;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Transition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.Duration;
import risk.Jugador;
import risk.Partida;
import risk.cartasmision.CartaMision;
import risk.cartasmision.CartaMisionFactory;
import risk.riskexception.ExcepcionRISK;
import risk.riskexception.RiskExceptionEnum;

public class AsignacionMisionesController {
    @FXML
    private ListView<Jugador> listaJugadores;
    @FXML
    private ListView<Class<? extends CartaMision>> listaMisiones;
    @FXML
    private Button bSiguiente;
    @FXML
    private Button bCargarDesdeArchivo;
    @FXML
    private Label tDescMision;
    @FXML
    private ImageView iBack;
    @FXML
    private ImageView iFront;
    @FXML
    private StackPane stackPaneCarta;

    private Transition transicionActualCarta;

    public void initialize() {

        ObservableList<Jugador> listaJ = FXCollections.observableArrayList(Partida.getPartida().getJugadores());
        listaJugadores.setItems(listaJ);
        listaJugadores.setCellFactory(new Callback<ListView<Jugador>, ListCell<Jugador>>() {
            @Override
            public ListCell<Jugador> call(ListView<Jugador> param) {
                ListCell<Jugador> celda = new ListCell<Jugador>() {
                    @Override
                    protected void updateItem(Jugador item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getNombre());
                        }
                    }
                };
                return celda;
            }
        });

        listaMisiones.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Class<? extends CartaMision>> listaM = FXCollections
                .observableArrayList(CartaMisionFactory.getAllAssignable());
        SortedList<Class<? extends CartaMision>> listaMOrdenada = new SortedList<>(listaM);
        listaMOrdenada.setComparator(Comparator.comparing((Class<? extends CartaMision> c) -> c.getSimpleName()));
        listaMisiones.setItems(listaMOrdenada);
        listaMisiones.setCellFactory(
                new Callback<ListView<Class<? extends CartaMision>>, ListCell<Class<? extends CartaMision>>>() {
                    @Override
                    public ListCell<Class<? extends CartaMision>> call(ListView<Class<? extends CartaMision>> param) {
                        ListCell<Class<? extends CartaMision>> celda = new ListCell<Class<? extends CartaMision>>() {
                            @Override
                            protected void updateItem(Class<? extends CartaMision> item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
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
                handleJugadorChange(newValue);
            }
        });
        listaMisiones.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Class<? extends CartaMision>>() {
                    @Override
                    public void changed(ObservableValue<? extends Class<? extends CartaMision>> observable,
                            Class<? extends CartaMision> oldValue, Class<? extends CartaMision> newValue) {
                                if (!listaMisiones.getSelectionModel().isEmpty()) {
                                    bCargarDesdeArchivo.setDisable(true);
                                }
                        handleMisionChange(newValue, oldValue);
                    }
                });
    }

    public void showFrontCarta() {
        if (transicionActualCarta == null && !listaMisiones.getSelectionModel().isEmpty()) {
            RotateTransition rotateTransitionBack = new RotateTransition(Duration.millis(300), iBack);
            rotateTransitionBack.setToAngle(90);
            rotateTransitionBack.setAxis(Rotate.Y_AXIS);
            rotateTransitionBack.setCycleCount(0);
            rotateTransitionBack.setAutoReverse(false);
            rotateTransitionBack.setInterpolator(Interpolator.EASE_OUT);

            transicionActualCarta = rotateTransitionBack;

            rotateTransitionBack.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    RotateTransition rotateTransitionFront = new RotateTransition(Duration.millis(300), iFront);
                    rotateTransitionFront.setToAngle(0);
                    rotateTransitionFront.setAxis(Rotate.Y_AXIS);
                    rotateTransitionFront.setCycleCount(0);
                    rotateTransitionFront.setAutoReverse(false);
                    rotateTransitionFront.setInterpolator(Interpolator.EASE_OUT);

                    RotateTransition rotateTransitionText = new RotateTransition(Duration.millis(300), tDescMision);
                    rotateTransitionText.setToAngle(0);
                    rotateTransitionText.setAxis(Rotate.Y_AXIS);
                    rotateTransitionText.setCycleCount(0);
                    rotateTransitionText.setAutoReverse(false);
                    rotateTransitionText.setInterpolator(Interpolator.EASE_OUT);

                    rotateTransitionFront.setOnFinished(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            transicionActualCarta = null;
                            if (!stackPaneCarta.isHover()) {
                                showBackCarta();
                            }
                        }

                    });

                    transicionActualCarta = rotateTransitionFront;
                    rotateTransitionFront.play();
                    rotateTransitionText.play();
                }
            });

            rotateTransitionBack.play();
        }
    }

    public void showBackCarta() {
        if (transicionActualCarta == null) {
            RotateTransition rotateTransitionBack = new RotateTransition(Duration.millis(300), iFront);
            rotateTransitionBack.setToAngle(-90);
            rotateTransitionBack.setAxis(Rotate.Y_AXIS);
            rotateTransitionBack.setCycleCount(0);
            rotateTransitionBack.setAutoReverse(false);
            rotateTransitionBack.setInterpolator(Interpolator.EASE_OUT);

            RotateTransition rotateTransitionText = new RotateTransition(Duration.millis(300), tDescMision);
            rotateTransitionText.setToAngle(-90);
            rotateTransitionText.setAxis(Rotate.Y_AXIS);
            rotateTransitionText.setCycleCount(0);
            rotateTransitionText.setAutoReverse(false);
            rotateTransitionText.setInterpolator(Interpolator.EASE_OUT);

            transicionActualCarta = rotateTransitionBack;

            rotateTransitionBack.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    RotateTransition rotateTransitionFront = new RotateTransition(Duration.millis(300), iBack);
                    rotateTransitionFront.setToAngle(0);
                    rotateTransitionFront.setAxis(Rotate.Y_AXIS);
                    rotateTransitionFront.setCycleCount(0);
                    rotateTransitionFront.setAutoReverse(false);
                    rotateTransitionFront.setInterpolator(Interpolator.EASE_OUT);

                    rotateTransitionFront.setOnFinished(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            transicionActualCarta = null;
                            if (stackPaneCarta.isHover()) {
                                showFrontCarta();
                            }
                        }

                    });

                    transicionActualCarta = rotateTransitionFront;
                    rotateTransitionFront.play();
                }
            });

            rotateTransitionBack.play();
            rotateTransitionText.play();
        }
    }

    public void handleJugadorChange(Jugador j) {
        if (j!=null) {
            ObservableList<Class<? extends CartaMision>> listaM = FXCollections
                .observableArrayList(CartaMisionFactory.getAllAssignable(j));
        SortedList<Class<? extends CartaMision>> listaMOrdenada = new SortedList<>(listaM);
        listaMOrdenada.setComparator(Comparator.comparing((Class<? extends CartaMision> c) -> c.getSimpleName()));
        listaMisiones.setItems(FXCollections.emptyObservableList());
        listaMisiones.setItems(listaMOrdenada);
        }
        try {
            listaMisiones.getSelectionModel().select(j.getCartaMision().getClass());
        } catch (NoSuchElementException e) {
            listaMisiones.getSelectionModel().clearSelection();
        }
    }

    public void handleClickOnSiguiente() {
        if (Partida.getPartida().areMisionesAsignadas()) {
            Main.goToRepartoPaises();
        }
    }

    public void handleMisionChange(Class<? extends CartaMision> claseCartaMision,
            Class<? extends CartaMision> oldValue) {
        Jugador jSeleccionado = listaJugadores.getSelectionModel().getSelectedItem();
        if (jSeleccionado != null) {
            Class<? extends CartaMision> misionJug;
            try {
                misionJug = jSeleccionado.getCartaMision().getClass();
            } catch (NoSuchElementException e) {
                misionJug = null;
            }
            if (claseCartaMision != null && !claseCartaMision.equals(misionJug)) {
                try {
                    CartaMision m = CartaMisionFactory.build(claseCartaMision.getSimpleName(), jSeleccionado);
                    if (misionJug != null) {
                        jSeleccionado.removeMision(jSeleccionado.getCartaMision());
                    }
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
        try {
            CartaMision misionJugActual = jSeleccionado.getCartaMision();
            tDescMision.setText(misionJugActual.getDescripcion());
            tDescMision.setVisible(true);
        } catch (NoSuchElementException | NullPointerException e) {
            tDescMision.setVisible(false);
        }
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
                        Jugador jugadorActual = Partida.getPartida().getJugador(partesLinea[0]);
                        CartaMision mision = CartaMisionFactory.build(partesLinea[1], jugadorActual);
                        Partida.getPartida().asignarMisionAJugador(mision, jugadorActual);
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

            handleClickOnSiguiente();
        }
    }
}
