package riskgui;

import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import risk.Pais;
public class PaisNode {
    private SVGPath path;
    private Label nombre;
    private Label etiquetaNEjercitos;
    private Pais pais;
    private Pane pane;

    PaisNode(Pais pais, SVGPath path) {
        this.pais = pais;
        this.path = path;

        this.nombre = new Label(pais.getCodigo());
        this.etiquetaNEjercitos = new Label(Integer.toString(pais.getNumEjercitos()));
        
    };
    
    public Pane getPane() {
        if (pane == null) {
            Set<Node> nodos = new HashSet<>();
            nodos.add(etiquetaNEjercitos);
            nodos.add(nombre);
            nodos.add(path);
            this.pane = new Pane();
            pane.getChildren().addAll(nodos);
            
            etiquetaNEjercitos.maxHeightProperty().set(path.layoutBoundsProperty().get().getHeight());
            etiquetaNEjercitos.maxWidthProperty().set(path.layoutBoundsProperty().get().getWidth());
        }
        return pane;
    }

    /**
     * @return the path
     */
    public SVGPath getPath() {
        return path;
    }

    /**
     * @return the nombre
     */
    public Label getNombre() {
        return nombre;
    }

    /**
     * @return the etiquetaNEjercitos
     */
    public Label getEtiquetaNEjercitos() {
        return etiquetaNEjercitos;
    }

    /**
     * @return the pais
     */
    public Pais getPais() {
        return pais;
    }
    
}
