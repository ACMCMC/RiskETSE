package riskgui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import risk.Mapa;
import risk.Pais;

public class CargarArchivoController {
    @FXML
    private Text textFormato;
    @FXML
    private Label labelNombreArchivo;

    private Stage stage;

    private File archivo;

    public void initialize() {
        textFormato.setText("Formato del archivo:\n[CódigoPaís];[Nombre del país]");
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

    public void cancelar() {
        stage.close();
    }

    public void ejecutar() {
        BufferedReader inputReader;
        String input;
        Iterator<Pais> itPaises = Mapa.getMapa().getPaises().iterator();
        try {
            inputReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8));
            try {
                input = inputReader.readLine();
                while (input != null) {
                    String partes[] = input.split(";");

                    if (partes.length != 2) {
                        throw new ArrayIndexOutOfBoundsException();
                    }

                    if (!itPaises.hasNext()) {
                        itPaises = Mapa.getMapa().getPaises().iterator();
                    }

                    Pais p = itPaises.next();
                    p.setCodigo(partes[0]);
                    p.setNombreHumano(partes[1]);

                    input = inputReader.readLine();
                }
                inputReader.close();
            } catch (IOException e) {
                Alert alerta = new Alert(AlertType.ERROR);
                alerta.setTitle("Error de lectura");
                alerta.setHeaderText(null);
                alerta.setContentText("Se ha producido un error al leer el archivo.");
                alerta.showAndWait();
            } catch (ArrayIndexOutOfBoundsException e) {
                Alert alerta = new Alert(AlertType.ERROR);
                alerta.setTitle("Formato incorrecto");
                alerta.setHeaderText(null);
                alerta.setContentText("El formato del archivo es incorrecto.");
                alerta.showAndWait();
            } finally {
                try {
                    inputReader.close();
                } catch (IOException e) {
                }
            }
        } catch (FileNotFoundException e) {
            stage.close();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
