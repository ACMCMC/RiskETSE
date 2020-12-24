package riskgui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import risk.Mapa;
import risk.riskexception.ExcepcionGeo;
import risk.riskexception.RiskExceptionEnum;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        crearMapa();

        Parent rootCreacionJugadores = FXMLLoader.load(getClass().getResource("creacionJugadores.fxml"));
        Scene escenaCreacionJugadores = new Scene(rootCreacionJugadores);

        Parent rootPartida = FXMLLoader.load(getClass().getResource("partida.fxml"));
        Scene escena1 = new Scene(rootPartida);

        primaryStage.setTitle("RiskETSE");
        primaryStage.setScene(escenaCreacionJugadores);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        risk.IOHelperFactory.setType(risk.ConsolaNormal.class);
        launch(args);
    }

    public void crearMapa() {
        risk.Consola io = risk.IOHelperFactory.getInstance();

        File filePaisesCoordenadas = new File("paisesCoordenadas.csv");
        File fileColoresContinentes = new File("coloresContinentes.csv");
        try {
            Mapa.crearMapa(filePaisesCoordenadas);
            try {
                Mapa.getMapa().asignarColoresContinentes(fileColoresContinentes);
            } catch (FileNotFoundException e) {
                try {
                    risk.FileCreatorFallback.crearArchivoColoresContinentes(fileColoresContinentes);
                    Mapa.getMapa().asignarColoresContinentes(fileColoresContinentes);
                } catch (IOException ex) {
                    io.printToErrOutput(RiskExceptionEnum.NO_SE_HA_PODIDO_LEER_NI_CREAR.get());
                }
            } catch (IOException e) {
                io.printToErrOutput(RiskExceptionEnum.NO_SE_HA_PODIDO_LEER.get());
            }
        } catch (IOException ex) {
            if (ex instanceof FileNotFoundException) {
                try {
                    risk.FileCreatorFallback.crearArchivoPaisesCoordenadas(filePaisesCoordenadas);
                    try {
                        Mapa.crearMapa(filePaisesCoordenadas);
                        try {
                            Mapa.getMapa().asignarColoresContinentes(fileColoresContinentes);
                        } catch (FileNotFoundException e) {
                            try {
                                risk.FileCreatorFallback.crearArchivoColoresContinentes(fileColoresContinentes);
                                Mapa.getMapa().asignarColoresContinentes(fileColoresContinentes);
                            } catch (IOException exc) {
                                io.printToErrOutput(RiskExceptionEnum.NO_SE_HA_PODIDO_LEER_NI_CREAR.get());
                            }
                        } catch (IOException e) {
                            io.printToErrOutput(RiskExceptionEnum.NO_SE_HA_PODIDO_LEER.get());
                        }
                    } catch (ExcepcionGeo e) {
                        io.printToErrOutput(e);
                    }
                } catch (IOException e) {
                    io.printToErrOutput(RiskExceptionEnum.NO_SE_HA_PODIDO_LEER.get());
                }
            } else {
                io.printToErrOutput(RiskExceptionEnum.NO_SE_HA_PODIDO_LEER.get());
            }
        } catch (ExcepcionGeo e) {
            io.printToErrOutput(e);
        }
    }
}
