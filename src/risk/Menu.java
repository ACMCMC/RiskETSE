/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Manuel Lama
 */
public class Menu {
    // En esta clase se deberían de definir los atributos a los que será
    // necesario acceder durante la ejecución del programa como, por ejemplo,
    // el mapa o los jugadores

    static final Logger logger = Logger.getLogger(Menu.class.getCanonicalName());

    public Menu() {
        // Inicialización de algunos atributos

        // Iniciar juego
        String orden = null;
        BufferedReader bufferLector = null;
        try {
            File fichero = new File("comandos.csv");
            FileReader lector = new FileReader(fichero);
            bufferLector = new BufferedReader(lector);
            while ((orden = bufferLector.readLine()) != null) {
                System.out.println("$> " + orden);
                String[] partes = orden.split(" ");
                String comando = partes[0];
                // COMANDOS INICIALES PARA EMPEZAR A JUGAR
                // crear mapa
                // crear jugadores <nombre_fichero>
                // crear <nombre_jugador> <nombre_color>
                // asignar misiones
                // asignar paises <nombre_fichero>
                // asignar <nombre_pais> <nombre_jugador>

                // COMANDOS DISPONIBLES DURANTE EL JUEGO
                // acabar
                // atacar <nombre_pais> <nombre_pais>
                // describir continente <nombre_continente>
                // describir frontera <nombre_pais>
                // describir frontera <nombre_continente>
                // describir jugador <nombre_jugador>
                // describir pais <nombre_pais>
                // jugador
                // repartir ejercitos
                // ver mapa
                // ver pais <nombre_pais>
                switch (comando) {
                    case "crear":
                        if (partes.length == 2) {
                            if (partes[1].equals("mapa")) {
                                // crearMapa es un método de la clase Menú desde el que se puede invocar
                                // a otros métodos de las clases que contienen los atributos y los métodos
                                // necesarios para realizar esa invocación
                                crearMapa();
                            } else {
                                System.out.println("\nComando incorrecto.");
                            }
                        } else if (partes.length == 3) {
                            if (partes[1].equals("jugadores")) {
                                try {
                                    crearJugadores(new File(partes[2]));
                                } catch (FileNotFoundException ex) {
                                    System.out.println("No existe el archivo especificado.");
                                    logger.info("No existe el archivo especificado.");
                                }
                            } else {
                                crearJugador(partes[1], partes[2]);
                            }
                        } else {
                            System.out.println("\nComando incorrecto.");
                        }
                        break;
                    case "asignar":
                        if (partes.length != 3) {
                            System.out.println("\nComando incorrecto.");
                        } else if (partes[1].equals("paises")) {
                            // asignarPaises es un método de la clase Menu que recibe como entrada el
                            // fichero
                            // en el que se encuentra la asignación de países a jugadores. Dentro de este
                            // método se invocará a otros métodos de las clases que contienen los atributos
                            // y los métodos necesarios para realizar esa invocación
                            asignarPaises(new File(partes[2]));
                        } else {
                            asignarPaises(partes[1], partes[2]);
                        }
                        break;
                    case "ver":
                        if (partes.length == 2) {
                            if (partes[1].equals("mapa")) {
                                try {
                                    Mapa.getMapa().imprimirMapa();
                                } catch (IllegalStateException ex) {
                                    FileOutputHelper.printToErrOutput(ex.getMessage());
                                }
                            }
                        }
                        break;
                    case "obtener":
                        if (partes.length == 3) {
                            if (partes[1].equals("color")) {
                                FileOutputHelper.printToOutput(new OutputBuilder()
                                        .manualAddString("color",
                                                Mapa.getMapa().getPais(partes[2]).getContinente().getColor().getNombre())
                                        .toString());
                            }
                        }
                        break;
                    default:
                        System.out.println("\nComando incorrecto.");
                }
            }
        } catch (Exception excepcion) {
            excepcion.printStackTrace();
        }
    }

    /**
     *
     * @param file
     */
    public void asignarPaises(File file) {
        // Código necesario para asignar países
        try {
            Mapa.crearMapa(file);
        } catch (FileNotFoundException ex) {
            logger.log(Level.WARNING, "No se ha encontrado el archivo {0}", file.getAbsolutePath());
        } catch (IllegalStateException e) {
            FileOutputHelper.printToErrOutput(e.getMessage()); // Print the exception to the output
        }
    }

    /**
     *
     * @param nombrePais
     * @param nombreJugador
     */
    public void asignarPaises(String nombrePais, String nombreJugador) {
        // Código necesario para asignar un país a un jugador
    }

    /**
     *
     */
    private void crearMapa() {
        /*try {
            Mapa.crearMapa(new File("paisesCoordenadas.csv"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            FileOutputHelper.printToErrOutput(e.getMessage()); // Print the exception to the output
        }*/
    }

    /**
     *
     * @param file
     */
    private void crearJugadores(File file) throws FileNotFoundException {
        // Código necesario para crear a los jugadores del RISK
        try {
            FileReader lector = new FileReader(file);
            BufferedReader bufferLector = new BufferedReader(lector);

            String linea;
            String[] partesLinea;

            while ((linea = bufferLector.readLine()) != null) {
                partesLinea = linea.split(";");
                Partida.getPartida().addJugador(new Jugador(partesLinea[0], Color.getColorByString(partesLinea[1])));
                FileOutputHelper.printToOutput(OutputBuilder.beginBuild().autoAdd("nombre", Partida.getPartida().getJugador(partesLinea[0]).getNombre()).autoAdd("color", Partida.getPartida().getJugador(partesLinea[0]).getColor().getNombre()).build());
            }
        } catch (FileNotFoundException fileNotFoundException) {
            // Si no se encuentra el archivo, falla el programa
            throw fileNotFoundException;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     *
     * @param file
     */
    private void crearJugador(String nombre, String color) {
        // Código necesario para crear a un jugador a partir de su nombre y color
        Jugador jugador = new Jugador(nombre, Color.getColorByString(color));
        Partida.getPartida().addJugador(jugador);
        FileOutputHelper.printToOutput(OutputBuilder.beginBuild().manualAddString("nombre", jugador.getNombre()).manualAddString("color", jugador.getColor().getNombre()).build());
    }
}
