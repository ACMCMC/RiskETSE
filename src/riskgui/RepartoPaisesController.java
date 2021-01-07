package riskgui;

import java.util.Comparator;
import java.util.Map.Entry;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;

public class RepartoPaisesController {
    @FXML
    private Pane panelMapa;

    private Mundo mundo;

    public void initialize() {
        mundo = new Mundo();

        panelMapa.getChildren().add(mundo.getWorldStackPane());

        mundo.getWorldStackPane().getChildren().stream().max(Comparator.comparing((Node n) -> n.getLayoutBounds().getMinY() ));

        /*mundo.getPaths().get("Alaska").setOnMouseEntered(new EventHandler<Event>(){

			@Override
			public void handle(Event event) {
                Alert alerta = new Alert(AlertType.ERROR);
                alerta.setTitle("Prueba");
                alerta.setHeaderText(null);
                alerta.setContentText("Alaska");
                alerta.show();
			}
            
        });*/
        //panelMapa.setPrefWidth(panelMapa.getScene().widthProperty().doubleValue());
    }
}
