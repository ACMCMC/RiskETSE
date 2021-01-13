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
import risk.Partida;
import risk.riskexception.ExcepcionJugador;

public class RepartoEjercitosController {
    @FXML
    private Pane panelMapa;
    @FXML
    private ToolBar toolbar;
    @FXML
    private VBox vBox;
    @FXML
    private ImageView imgSoldado;

    private Mundo mundo;

    public void initialize() {
        mundo = new MundoBuilder().setActionClick((p) -> {
            return new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    if (p.getJugador() != null) {
                        try {
                            p.getJugador().asignarEjercitosAPais(1, p);
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

        imgSoldado.setCursor(Cursor.HAND);
        imgSoldado.setOnDragDetected(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Dragboard dragboard = imgSoldado.startDragAndDrop(TransferMode.MOVE);
                Map<DataFormat, Object> mapa = new HashMap<DataFormat, Object>();
                dragboard.setContent(mapa);

                URL url = getClass().getResource("resources/soldado.png");

                Image imagenSoldado = new Image(url.toExternalForm(), 50, 50, true, true);
                dragboard.setDragView(imagenSoldado);

                event.consume();
            }
        });

    }
}
