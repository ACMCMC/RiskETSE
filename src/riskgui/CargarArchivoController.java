package riskgui;

import java.io.File;
import javafx.stage.FileChooser;

public class CargarArchivoController {
    @FXML
    private Label labelFormato;
    @FXML
    private Label labelNombreArchivo;

    private File archivo;

    public void initialize() {
        labelFormato.setText("Formato del archivo:\n[CódigoPaís];[Nombre del país]");
    }

    public void seleccionarArchivo() {
        FileChooser fileChooser = new FileChooser();
        archivo = fileChooser.showOpenDialog(Main.getStage());
        if (archivo == null) {
            labelNombreArchivo.setText("Ningún archivo seleccionado");
        } else {
            labelNombreArchivo.setText(archivo.getName());
        }
    }
   
}
