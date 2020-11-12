/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Comparator;

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
            anadirFronterasIndirectas(); // Esto hay que hacerlo manualmente, porque la clase Mapa no sabe cuáles son
                                         // las fronteras indirectas
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

            Partida.getPartida().asignarEjercitosSinRepartir();

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
                        Mapa.getMapa().getPais(nombrePais)
                                .setJugador(Partida.getPartida().getJugador(nombreJugador).get());
                        Partida.getPartida().getJugador(nombreJugador).get().asignarEjercitosAPais(1,
                                Mapa.getMapa().getPais(nombrePais));
                        FileOutputHelper.printToOutput(OutputBuilder.beginBuild().autoAdd("nombre", nombreJugador)
                                .autoAdd("pais", nombrePais)
                                .autoAdd("continente", Mapa.getMapa().getPais(nombrePais).getContinente().getCodigo())
                                .autoAdd("frontera", Mapa.getMapa().getFronteras(Mapa.getMapa().getPais(nombrePais)))
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
                        .autoAdd("color",
                                Partida.getPartida().getJugador(partesLinea[0]).orElse(null).getColor().getNombre())
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
     * Una tupla que representa Continentes, Jugadores, el numero de paises de ese
     * jugador dentro del continente, y el porcentaje de países del continente que
     * posee ese jugador
     */
    private static class TuplaContinenteJugadorPorcentaje {
        final Continente c;
        final Jugador j;
        final int n;
        final float p;

        TuplaContinenteJugadorPorcentaje(Continente c, Jugador j, int n, float p) {
            this.c = c;
            this.j = j;
            this.n = n;
            this.p = p;
        }

        Continente getContinente() {
            return c;
        }

        Jugador getJugador() {
            return j;
        }

        int getNumPaises() {
            return n;
        }

        float getPorcentaje() {
            return p;
        }
    }

    /**
     * Se encarga automáticamente de realizar el reparto de los ejércitos.
     */
    private void repartirEjercitos() {
        /*
         * R1 Si inicialmente existe un continente en el que más del 50% de los países
         * están ocupados por un mismo jugador, entonces en cada país se colocará
         * automáticamente el siguiente número de ejércitos de dicho jugador:
         * #ejercitos = ejercitos_disponibles/(factor_division ∗ numero_paises_ocupados)
         * donde factor_división es 1,5 si el continente es Oceanía o América del Sur
         * y 1 para el resto de los continentes.
         */

        Set<TuplaContinenteJugadorPorcentaje> tuplas = obtenerTuplasContinenteJugadorPorcentaje();
        if (!aplicarReglasPorcentajes(tuplas, tupla -> tupla.getPorcentaje() >= 0.5, tupla -> {
            if (tupla.getContinente().equals(Mapa.getMapa().getContinente("Oceanía"))
                    || tupla.getContinente().equals(Mapa.getMapa().getContinente("AméricaSur"))) {
                return new Float(1.5);
            } else {
                return new Float(1);
            }
        })) {
            aplicarReglasPorcentajes(tuplas, tupla -> tupla.getPorcentaje() >= 0.25 && tupla.getPorcentaje() < 0.5,
                    tupla -> new Float(2));
        }

        Map<Jugador, List<TuplaContinenteJugadorPorcentaje>> tuplasJugs = tuplas.parallelStream()
                .collect(Collectors.groupingBy(TuplaContinenteJugadorPorcentaje::getJugador));
        // Un Map que relaciona Jugadores con sus tuplas
        tuplasJugs.entrySet().parallelStream()
                .filter(entry -> entry.getValue().stream().allMatch(tupla -> tupla.getPorcentaje() < 0.25))
                // Nos quedamos solo con las entradas del Map en las que todas las tuplas dicen
                // que el jugador tiene menos del 25% del porcentaje (es decir, solo nos
                // quedamos con los jugadores que tienen todos los porcentajes de menos del 25%)
                .forEach(entrada -> { // Por cada jugador...
                    entrada.getValue().forEach(tupla -> {
                        // Por cada tupla (continente, porque el jugador va a ser el mismo en cada
                        // iteración de este bucle)...
                        int numEjercitos = (Partida.getPartida().getJugadores().size() < 5 ? 2 : 3)
                                * tupla.getNumPaises();
                        // La regla del PDF de cuántos países hay que asignar (R7)
                        tupla.getContinente().getPaises().stream()
                                .filter(pais -> pais.getJugador().get().equals(tupla.getJugador()))
                                .forEach(pais -> tupla.getJugador().asignarEjercitosAPais(numEjercitos, pais));
                        // Le asignamos numEjercitos a todos los países del jugador, en ese continente
                    });
                });

        /*
         * Si después de haber aplicado la regla R7 aún queda ejércitos disponibles,
         * entonces se colocará 1 ejército en cada uno de los países que tienen un
         * único ejército, priorizando aquellos países que pertenecen a continentes
         * con menos países frontera.
         */
        asignar1EjercitoAPaisesCon1Ejercito(Mapa.getMapa().getContinentes());
    }

    private void asignar1EjercitoAPaisesCon1Ejercito(Set<Continente> setContinentes) {
        PriorityQueue<Continente> colaAsignar = new PriorityQueue<>(new Comparator<Continente>() {

            @Override
            public int compare(Continente o1, Continente o2) {
                int numFronterasO1 = Mapa.getMapa().getNumFronterasIntercontinentales(o1);
                int numFronterasO2 = Mapa.getMapa().getNumFronterasIntercontinentales(o2);
                return (numFronterasO1 == numFronterasO2 ? 0 : numFronterasO1 > numFronterasO2 ? -1 : 1);
            }

        }.reversed());
        colaAsignar.addAll(setContinentes); // Ponemos los continentes en una cola de prioridad, ordenándolos por los
                                            // que tengan menos fronteras
        Stream.generate(colaAsignar::poll);
        colaAsignar.stream().sorted(colaAsignar.comparator()).forEach(continente -> {
            continente.getPaises().parallelStream().filter(pais -> pais.getEjercitos().size() == 1)
                    .forEach(pais -> pais.getJugador().get().asignarEjercitosAPais(1, pais));
        });
    }

    private boolean aplicarReglasPorcentajes(Set<TuplaContinenteJugadorPorcentaje> setTuplas,
            Predicate<TuplaContinenteJugadorPorcentaje> predicadoFiltrado,
            Function<TuplaContinenteJugadorPorcentaje, Float> factorDivision) {

        Set<TuplaContinenteJugadorPorcentaje> tuplasFiltradas = setTuplas.parallelStream().filter(predicadoFiltrado)
                .collect(Collectors.toSet()); // Aplicamos el predicado de filtrado a las tuplas
        float procentajeMaximo = tuplasFiltradas.parallelStream()
                .max(Comparator.comparing(TuplaContinenteJugadorPorcentaje::getPorcentaje)).get().getPorcentaje();
        // Obtenemos el porcentaje máximo de las tuplas que resultaron del filtrado, ya
        // que sólo vamos a aplicar la regla en un continente

        tuplasFiltradas = tuplasFiltradas.stream().filter(tupla -> {
            return (Float.compare(tupla.getPorcentaje(), procentajeMaximo) == 0);
        }).collect(Collectors.toSet());
        // Ahora sólo nos quedan las tuplas de continentes con jugadores que cumplen el
        // predicado de filtrado y están empatadas entre sí (puede haber varios
        // jugadores dentro de la misma tupla)

        Continente continenteMenosFronteras = tuplasFiltradas.stream()
                .min(new Comparator<TuplaContinenteJugadorPorcentaje>() {
                    @Override
                    public int compare(TuplaContinenteJugadorPorcentaje o1, TuplaContinenteJugadorPorcentaje o2) {
                        int numFronterasO1 = Mapa.getMapa().getNumFronterasIntercontinentales(o1.getContinente());
                        int numFronterasO2 = Mapa.getMapa().getNumFronterasIntercontinentales(o2.getContinente());
                        return (numFronterasO1 == numFronterasO2 ? 0 : numFronterasO1 > numFronterasO2 ? -1 : 1);
                    }
                }.reversed()).get().getContinente();
        // Buscamos una de las tuplas con continente con menos fronteras, y nos quedamos
        // con ese Continente

        tuplasFiltradas = tuplasFiltradas.stream()
                .filter(tupla -> tupla.getContinente().equals(continenteMenosFronteras)).collect(Collectors.toSet());
        // Ahora sólo nos quedan las tuplas de continentes con jugadores que cumplen el
        // predicado de filtrado y están empatadas entre sí (puede haber varios
        // jugadores dentro de la misma tupla, incluso si no hay continentes empatados
        // entre sí), y de los posibles continentes empatados, el que tiene menos
        // fronteras.

        if (tuplasFiltradas.isEmpty()) {
            return false; // Ninguna tupla ha cumplido las condiciones, así que no se ha aplicado la
                          // regla. Devolvemos false.
        }

        tuplasFiltradas.forEach(tupla -> {
            int numEjercitos = (int) Math.round(((float) tupla.getJugador().getEjercitosSinRepartir())
                    / (factorDivision.apply(tupla) * (float) tupla.getNumPaises()));
            tupla.getJugador().getPaises().stream().filter(pais -> pais.getContinente().equals(tupla.getContinente()))
                    .forEach(pais -> {
                        tupla.getJugador().asignarEjercitosAPais(numEjercitos, pais);
                    });
        });

        asignar1EjercitoAPaisesCon1Ejercito(Mapa.getMapa().getContinentes());

        return true; // Sí se ha aplicado la regla
    }

    /**
     * Elabora tuplas de la forma (Continente, Jugador, Numero de paises,
     * Porcentaje). Expresan que el jugador tiene un determinado porcentaje de los
     * países del Continente de la tupla
     * 
     * @return un Set de Tuplas
     */
    private Set<TuplaContinenteJugadorPorcentaje> obtenerTuplasContinenteJugadorPorcentaje() {
        Set<TuplaContinenteJugadorPorcentaje> tuplas = Mapa.getMapa().getContinentes().parallelStream()
                .map(continente -> { // Por cada continente...
                    List<Jugador> listaJugadoresContinente = continente.getPaises().stream()
                            .map(pais -> pais.getJugador().get()).collect(Collectors.toList()); // Elaboro una lista de
                                                                                                // los jugadores de ese
                                                                                                // continente; eso lo
                                                                                                // obtengo a través de
                                                                                                // los países del
                                                                                                // continente
                    Set<TuplaContinenteJugadorPorcentaje> setTuplas = listaJugadoresContinente.stream().distinct()
                            .map(jugador -> {
                                float porcentaje = ((float) Collections.frequency(listaJugadoresContinente, jugador))
                                        / ((float) listaJugadoresContinente.size()); // Este es el porcentaje de los
                                                                                     // países dentro de este continente
                                                                                     // que posee el jugador
                                return (new TuplaContinenteJugadorPorcentaje(continente, jugador,
                                        Collections.frequency(listaJugadoresContinente, jugador), porcentaje)); // Generamos
                                                                                                                // la
                                                                                                                // tupla
                            }).collect(Collectors.toSet()); // Guardamos todas las tuplas de este continente en un mismo
                                                            // Set
                    return (setTuplas); // Devolvemos ese Set
                }).flatMap(Set::stream).collect(Collectors.toSet()); // Hasta aquí teníamos un
                                                                     // Set<Set<TuplaContinenteJugadorPorcentaje>>, ya
                                                                     // que tenemos las tuplas en un Set por cada
                                                                     // Continente. Pero realmente no nos interesa
                                                                     // tenerlas en Sets separados, así que las
                                                                     // convertimos a un único Set
        return tuplas;
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

    /**
     * Añade manualmente las fronteras indirectas
     */
    private void anadirFronterasIndirectas() {
        Set<String[]> fronterasPaises = new HashSet<>();

        fronterasPaises.add(new String[] { "Brasil", "ANorte" });
        fronterasPaises.add(new String[] { "EurOcc", "ANorte" });
        fronterasPaises.add(new String[] { "Groenlan", "Islandia" });
        fronterasPaises.add(new String[] { "Kamchatka", "Alaska" });
        fronterasPaises.add(new String[] { "EurSur", "Egipto" });
        fronterasPaises.add(new String[] { "SAsiático", "Indonesia" });

        fronterasPaises.parallelStream().map(paises -> {
            List<Pais> par = new ArrayList<>();
            par.add(Mapa.getMapa().getPais(paises[0]));
            par.add(Mapa.getMapa().getPais(paises[1]));
            return (par);
        }).forEach(par -> Mapa.getMapa().anadirFronteraIndirecta(par.get(0), par.get(1)));
    }

    /**
     * Imprime el color asociado a un pais
     * 
     * @param abrevPais
     */
    private void obtenerColor(String abrevPais) {
        Color color;
        color = Mapa.getMapa().getPais(abrevPais).getContinente().getColor();
        FileOutputHelper.printToOutput(OutputBuilder.beginBuild().autoAdd("Color", color).build());
    }

    /**
     * Muestra las caracteristicas de un pais: nombre, abreviatura, continente,
     * fronteras, jugador al que pertenece, numero de ejercitos que lo ocupan y el
     * numero de veces que ha sido conquistado
     * 
     * @param abrevPais
     */
    private void describirPais(String abrevPais) {
        String nombreHumano;
        String abreviatura;
        // Continente continente;
        Jugador jugador;
        Ejercito ejercito;
        Integer numeroConquistas;

        nombreHumano = Mapa.getMapa().getPais(abrevPais).getNombreHumano();
        FileOutputHelper.printToOutput(OutputBuilder.beginBuild().autoAdd("Nombre", nombreHumano).build());

        abreviatura = Mapa.getMapa().getPais(abrevPais).getCodigo();
        FileOutputHelper.printToOutput(OutputBuilder.beginBuild().autoAdd("Abreviatura", abreviatura).build());

        // continente=Mapa.getContinente(abrevPais);
        FileOutputHelper.printToOutput(OutputBuilder.beginBuild()
                .autoAdd("Continente", Mapa.getMapa().getPais(abrevPais).getContinente().getNombreHumano()).build());

        // FileOutputHelper.printToOutput(OutputBuilder.beginBuild().autoAdd("Frontera",
        // obtenerFronteras()).build());

        abreviatura = Mapa.getMapa().getPais(abrevPais).getCodigo();
        FileOutputHelper.printToOutput(OutputBuilder.beginBuild().autoAdd("Jugador", abreviatura).build());
    }

}
