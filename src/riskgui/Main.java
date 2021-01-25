package riskgui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import risk.Mapa;
import risk.riskexception.ExcepcionGeo;
import risk.riskexception.RiskExceptionEnum;

public class Main extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        crearMapa();
        
        primaryStage.setTitle("RiskETSE");
        primaryStage.setResizable(false);

        /*Menu menu = new Menu();
        menu.crearJugadores("jugadores.csv");
        menu.asignarMisiones("misiones.csv");
        Partida.getPartida().asignarEjercitosSinRepartir();
        menu.asignarPaises("paises.csv");
        menu.repartirEjercitos();
        goToPartida();*/
        goToPantallaInicial();
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
    
    public static void goToEditarMapa() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("edicionMapa.fxml"));
            Scene escena = new Scene(root);
            stage.setScene(escena);
            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
        }
    }

    public static void goToRepartoEjercitos() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("repartoEjercitos.fxml"));
            Scene escena = new Scene(root);
            stage.setScene(escena);
            stage.show();
        } catch (IOException e) {
        }
    }

    public static void goToPartida() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("partida.fxml"));
            Scene escena = new Scene(root);
            stage.setScene(escena);
            stage.show();
        } catch (IOException e) {
        }
    }

    public static void goToPantallaInicial() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("pantallaInicial.fxml"));
            Scene escena = new Scene(root);
            stage.setScene(escena);
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
            stage.centerOnScreen();
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

        anadirFronterasIndirectas();
    }

    private void anadirFronterasIndirectas() {
        Set<String[]> fronterasPaises = new HashSet<>();

        fronterasPaises.add(new String[] { "Brasil", "ANorte" });
        fronterasPaises.add(new String[] { "EurOcc", "ANorte" });
        fronterasPaises.add(new String[] { "Groenlan", "Islandia" });
        fronterasPaises.add(new String[] { "Kamchatka", "Alaska" });
        fronterasPaises.add(new String[] { "EurSur", "Egipto" });
        fronterasPaises.add(new String[] { "SAsiático", "Indonesia" });

        fronterasPaises.stream().map(paises -> {
            List<risk.Pais> par = new ArrayList<>();
            try {
                par.add(Mapa.getMapa().getPais(paises[0]));
                par.add(Mapa.getMapa().getPais(paises[1]));
            } catch (ExcepcionGeo e) { // Sería conveniente implementar manejo de excepciones
            }
            return (par);
        }).forEach(par -> {
            try{
            Mapa.getMapa().anadirFronteraIndirecta(par.get(0), par.get(1));}
            catch(IndexOutOfBoundsException e) {}
        });
    }

    public static Stage getStage() {
        return stage;
    }
}
