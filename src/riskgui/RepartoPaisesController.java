package riskgui;

import java.util.Map.Entry;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.shape.SVGPath;

public class RepartoPaisesController {
    @FXML
    private Group grupoPaises;

    private Mundo mundo;

    public void initialize() {
        mundo = new Mundo();
        for (Entry<String, SVGPath> entrada : mundo.getPaths().entrySet()) {
            grupoPaises.getChildren().add(entrada.getValue());
        }
        mundo.getPaths().get("Alaska").setOnMouseEntered(new EventHandler<Event>(){

			@Override
			public void handle(Event event) {
                Alert alerta = new Alert(AlertType.ERROR);
                alerta.setTitle("Prueba");
                alerta.setHeaderText(null);
                alerta.setContentText("Alaska");
                alerta.show();
			}
            
        });
    }
}
