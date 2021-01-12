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
import risk.Menu;
import risk.Partida;
import risk.riskexception.ExcepcionGeo;
import risk.riskexception.RiskExceptionEnum;

public class Main extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        crearMapa();

        Menu menu = new Menu();
        menu.crearJugadores("jugadores.csv");
        Partida.getPartida().asignarEjercitosSinRepartir();
        menu.asignarMisiones("misiones.csv");
        //menu.asignarPaises("paises.csv");
        //menu.repartirEjercitos();

        goToRepartoPaises();
        
        primaryStage.setTitle("RiskETSE");
        primaryStage.setResizable(false);
    }
    
    public static void goToCreacionJugadores() {
        try {
            Parent rootCreacionJugadores = FXMLLoader.load(Main.class.getResource("creacionJugadores.fxml"));
            Scene escenaCreacionJugadores = new Scene(rootCreacionJugadores);
            stage.setScene(escenaCreacionJugadores);
            stage.show();
        } catch (IOException e) {
        }
    }
    public static void goToAsignacionMisiones() {
        try {
            Parent rootAsignacionMisiones = FXMLLoader.load(Main.class.getResource("asignacionMisiones.fxml"));
            Scene escenaAsignacionMisiones = new Scene(rootAsignacionMisiones);
            stage.setScene(escenaAsignacionMisiones);
            stage.show();
        } catch (IOException e) {
        }
    }
    public static void goToRepartoPaises() {
        try {
            Parent rootRepartoPaises = FXMLLoader.load(Main.class.getResource("repartoPaises.fxml"));
            Scene escenaRepartoPaises = new Scene(rootRepartoPaises);
            stage.setScene(escenaRepartoPaises);
            stage.show();
            stage.sizeToScene();
        } catch (IOException e) {
        }
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

    public static Stage getStage() {
        return stage;
    }
}
