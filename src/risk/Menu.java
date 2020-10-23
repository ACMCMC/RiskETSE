/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Manuel Lama
 */
public class Menu {
    // En esta clase se deberían de definir los atributos a los que será
    // necesario acceder durante la ejecución del programa como, por ejemplo,
    // el mapa o los jugadores

    static final Logger logger = Logger.getLogger(Menu.class.getCanonicalName());

    private static final String PROMPT = "$> ";

    public Menu() {
        // Inicialización de algunos atributos

        // Iniciar juego
        String orden = null;
        String[] partes;
        BufferedReader bufferLector = null;
        try {
            File fichero = new File("comandos.csv");
            FileReader lector = new FileReader(fichero);
            bufferLector = new BufferedReader(lector);
            while ((orden = bufferLector.readLine()) != null && !orden.equals("crear mapa")) { // La primera línea tiene
                                                                                               // que ser "crear mapa".
                                                                                               // Mostramos un error
                                                                                               // mientras no sea esa.
                System.out.println(PROMPT + orden);
                FileOutputHelper
                .printToErrOutput(new RiskException(RiskException.RiskExceptionEnum.MAPA_NO_CREADO).toString());
            }
            System.out.println(PROMPT + orden);
            crearMapa();
            anadirFronterasIndirectas(); // Esto hay que hacerlo manualmente, porque la clase Mapa no sabe cuáles son las fronteras indirectas
            Mapa.getMapa().imprimirMapa(); // Imprimimos el mapa una vez creado


            boolean jugadoresCreados = false; // Lo usaremos como flag para saber cuándo salir del while
            while ((orden = bufferLector.readLine()) != null
                    && (!jugadoresCreados || orden.startsWith("crear jugador"))) { // La segunda línea (y posiblemente
                // las siguientes) tienen que ser
                // "crear jugador nombre color".
                // Mostramos un error mientras no sea
                // esa. Tiene que haber al menos 3
                // jugadores.
                System.out.println(PROMPT + orden);
                if (orden.startsWith("crear jugador")) { // Entramos por aquí si es crear jugador o crear jugadores
                    partes = orden.split(" ");
                    if (partes[1].equals("jugador") && partes.length == 4) {
                        crearJugador(partes[2], partes[3]);
                    } else if (partes[1].equals("jugadores") && partes.length == 3) {
                        crearJugadores(new File(partes[2]));
                    } else {
                        comandoIncorrecto();
                    }
                    if (Partida.getPartida().getJugadores().size() >= 3) { // Comprobamos si ya hay 3 jugadores creados
                        jugadoresCreados = true;
                    }
                } else { // Si el comando no es crear jugador
                    FileOutputHelper.printToErrOutput(
                            new RiskException(RiskException.RiskExceptionEnum.JUGADORES_NO_CREADOS).toString());
                }
            }

            while ((orden = bufferLector.readLine()) != null) {
                System.out.println(PROMPT + orden);
                partes = orden.split(" ");
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
                                comandoNoPermitido();
                            } else {
                                comandoIncorrecto();
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
                        // *********************************************************************
                        // CORREGIR
                        // *********************************************************************
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
                            asignarPais(partes[1], partes[2]);
                        }
                        break;
                    case "ver":
                        if (partes.length == 2) {
                            if (partes[1].equals("mapa")) {
                                Mapa.getMapa().imprimirMapa();
                            }
                        }
                        break;
                    case "obtener":
                        if (partes.length == 3) {
                            if (partes[1].equals("color")) {
                                FileOutputHelper.printToOutput(new OutputBuilder().manualAddString("color",
                                        Mapa.getMapa().getPais(partes[2]).getContinente().getColor().getNombre())
                                        .toString());
                            } else if (partes[1].equals("frontera")) {
                                obtenerFronteras(partes[2]);
                            }
                        }
                        break;
                    case "repartir":
                        if (partes.length == 2) {
                            if (partes[1].equals("ejercitos")) {
                                repartirEjercitos();
                            }
                        }
                    default:
                        comandoIncorrecto();
                }
            }
        } catch (Exception excepcion) {
            excepcion.printStackTrace();
        }
    }

    /**
     * Procesa un archivo con [NombreJugador];[NombrePais] para realizar las
     * asignaciones en el mapa
     * 
     * @param archivoAsignaciones
     */
    public void asignarPaises(File archivoAsignaciones) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(archivoAsignaciones));
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                String partes[] = linea.split(";");
                String nombrePais = partes[1];
                String nombreJugador = partes[0];

                asignarPais(nombrePais, nombreJugador);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     *
     * @param nombrePais
     * @param nombreJugador
     */
    public void asignarPais(String nombrePais, String nombreJugador) {
        if (Mapa.getMapa().getPais(nombrePais) == null) {
            FileOutputHelper
                    .printToErrOutput(new RiskException(RiskException.RiskExceptionEnum.PAIS_NO_EXISTE).toString());
        } else {
            if (Mapa.getMapa().getPais(nombrePais).getJugador().isPresent()) {
                FileOutputHelper.printToErrOutput(
                        new RiskException(RiskException.RiskExceptionEnum.PAIS_YA_ASIGNADO).toString());
            } else {
                if (!Partida.getPartida().getJugador(nombreJugador).isPresent()) {
                    FileOutputHelper.printToErrOutput(
                            new RiskException(RiskException.RiskExceptionEnum.JUGADOR_NO_EXISTE).toString());
                } else {
                    if (false) {
                        // TODO: Las misiones no están asignadas ERROR
                    } else {
                        Mapa.getMapa().getPais(nombrePais).setJugador(Partida.getPartida().getJugador(nombreJugador).get());
                        FileOutputHelper.printToOutput(
                                OutputBuilder.beginBuild().autoAdd("nombre", nombreJugador).autoAdd("pais", nombrePais)
                                        .autoAdd("continente", Mapa.getMapa().getPais(nombrePais).getContinente().getCodigo())
                                        .autoAdd("frontera",
                                                Mapa.getMapa().getFronteras(Mapa.getMapa().getPais(nombrePais)))
                                        .build());
                    }
                }
            }
        }
    }

    /**
     *
     */
    private void crearMapa() {
        File filePaisesCoordenadas = new File("paisesCoordenadas.csv");
        try {
            Mapa.crearMapa(filePaisesCoordenadas);
        } catch (FileNotFoundException ex) {
            logger.log(Level.WARNING, "No se ha encontrado el archivo {0}", filePaisesCoordenadas.getAbsolutePath());
        }
    }

    /**
     * Imprime el error 99 (Comando no permitido en este momento)
     */
    private void comandoNoPermitido() {
        FileOutputHelper
                .printToErrOutput(new RiskException(RiskException.RiskExceptionEnum.COMANDO_NO_PERMITIDO).toString());
    }

    /**
     * Imprime el error 101 (Comando incorrecto)
     */
    private void comandoIncorrecto() {
        FileOutputHelper
                .printToErrOutput(new RiskException(RiskException.RiskExceptionEnum.COMANDO_INCORRECTO).toString());
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
                FileOutputHelper.printToOutput(OutputBuilder.beginBuild()
                        .autoAdd("nombre", Partida.getPartida().getJugador(partesLinea[0]).orElse(null).getNombre())
                        .autoAdd("color", Partida.getPartida().getJugador(partesLinea[0]).orElse(null).getColor().getNombre())
                        .build());
            }

            bufferLector.close();
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
        FileOutputHelper.printToOutput(OutputBuilder.beginBuild().manualAddString("nombre", jugador.getNombre())
                .manualAddString("color", jugador.getColor().getNombre()).build());
    }

    /**
     * Se encarga automáticamente de realizar el reparto de los ejércitos.
     */
    private void repartirEjercitos() {
        // TODO Completar esta parte
    }

    /**
     * Imprime las fronteras de un país
     * 
     * @param codigoPais
     */
    private void obtenerFronteras(String codigoPais) {
        Set<String> nombresPaisesFronteras = Mapa.getMapa().getFronteras(Mapa.getMapa().getPais(codigoPais)).stream() // Creamos
                                                                                                                      // un
                                                                                                                      // Stream
                                                                                                                      // de
                                                                                                                      // las
                                                                                                                      // Fronteras
                                                                                                                      // de
                                                                                                                      // ese
                                                                                                                      // país
                .map((Frontera frontera) -> {
                    return (frontera.getPaises().stream().filter((Pais pais) -> {
                        return (!Mapa.getMapa().getPais(codigoPais).equals(pais)); // Buscamos, dentro de los dos países
                                                                                   // de esa frontera, el país que no
                                                                                   // sea el de la consulta
                    }).collect(Collectors.toList()).get(0)).getNombreHumano(); // En ese país, nos quedamos con el
                                                                               // nombre en formato humano
                }).collect(Collectors.toSet()); // Lo convertimos a un Set
        FileOutputHelper.printToOutput(OutputBuilder.beginBuild().autoAdd("frontera", nombresPaisesFronteras).build()); // Lo
                                                                                                                        // sacamos
                                                                                                                        // a
                                                                                                                        // la
                                                                                                                        // salida
    }

    /**
     * Imprime el continente al que pertenece un pais
     * 
     * @param abrevPais
     */
    private void obtenerContinente(String abrevPais) {
        String Continente;
        Continente = Mapa.getMapa().getPais(abrevPais).getContinente().getNombreHumano();
        FileOutputHelper.printToOutput(OutputBuilder.beginBuild().autoAdd("Continente", Continente).build());
    }

    private void obtenerColor(String abrevPais) {

    }

    /**
     * Añade manualmente las fronteras indirectas
     */
    private void anadirFronterasIndirectas() {
        Mapa.getMapa().anadirFronteraIndirecta(Mapa.getMapa().getPais("Brasil"), Mapa.getMapa().getPais("ANorte"));
        Mapa.getMapa().anadirFronteraIndirecta(Mapa.getMapa().getPais("EurOcc"), Mapa.getMapa().getPais("ANorte"));
        Mapa.getMapa().anadirFronteraIndirecta(Mapa.getMapa().getPais("Groenlan"), Mapa.getMapa().getPais("Islandia"));
        Mapa.getMapa().anadirFronteraIndirecta(Mapa.getMapa().getPais("Kamchatka"), Mapa.getMapa().getPais("Alaska"));
        Mapa.getMapa().anadirFronteraIndirecta(Mapa.getMapa().getPais("EurSur"), Mapa.getMapa().getPais("Egipto"));
        Mapa.getMapa().anadirFronteraIndirecta(Mapa.getMapa().getPais("SAsiático"), Mapa.getMapa().getPais("Indonesia"));
    }

}
