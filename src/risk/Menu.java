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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import risk.RiskException.ExcepcionGeo;
import risk.RiskException.ExcepcionJugador;
import risk.RiskException.RiskException;
import risk.RiskException.RiskExceptionEnum;

/**
 *
 * @author Manuel Lama
 */
public class Menu {
    // En esta clase se deberían de definir los atributos a los que será
    // necesario acceder durante la ejecución del programa como, por ejemplo,
    // el mapa o los jugadores

    private IOHelper io;
    {
        // Inicialización de io; podemos hacerla antes del constructor
        io = IOHelperFactory.getInstance();
    }

    public Menu() {
        // Inicialización de algunos atributos

        // Iniciar juego
        String orden = null;
        String[] partes;
        try {
            while ((orden = io.readLine()) != null && !orden.equals("crear mapa")) { // La primera línea tiene
                                                                                     // que ser "crear mapa".
                                                                                     // Mostramos un error
                                                                                     // mientras no sea esa.
                io.printToErrOutput(RiskExceptionEnum.MAPA_NO_CREADO.get());
            }
            crearMapa();
            anadirFronterasIndirectas(); // Esto hay que hacerlo manualmente, porque la clase Mapa no sabe cuáles son
                                         // las fronteras indirectas
            imprimirMapa(); // Imprimimos el mapa una vez creado

            boolean jugadoresCreados = false; // Lo usaremos como flag para saber cuándo salir del while
            while ((orden = io.readLine()) != null && (!jugadoresCreados || orden.startsWith("crear jugador"))) { // La
                                                                                                                  // segunda
                                                                                                                  // línea
                                                                                                                  // (y
                                                                                                                  // posiblemente
                // las siguientes) tienen que ser
                // "crear jugador nombre color".
                // Mostramos un error mientras no sea
                // esa. Tiene que haber al menos 3
                // jugadores.
                if (orden.startsWith("crear")) { // Entramos por aquí si es crear jugador o crear jugadores
                    partes = orden.split(" ");
                    if (partes.length == 3 && partes[1].equals("jugadores")) {
                        crearJugadores(new File(partes[2]));
                    } else if (partes.length == 3) {
                        crearJugador(partes[1], partes[2]);
                    } else {
                        comandoIncorrecto();
                    }
                    if (Partida.getPartida().getJugadores().size() >= 3) { // Comprobamos si ya hay 3 jugadores creados
                        jugadoresCreados = true;
                    }
                } else { // Si el comando no es crear jugador
                    io.printToErrOutput(RiskExceptionEnum.JUGADORES_NO_CREADOS.get());
                }
            }

            Partida.getPartida().asignarEjercitosSinRepartir();

            do {
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
                                }
                            } else {
                                crearJugador(partes[1], partes[2]);
                            }
                        } else {
                            comandoIncorrecto();
                        }
                        break;
                    case "asignar":
                        // *********************************************************************
                        // CORREGIR
                        // *********************************************************************
                        if (partes.length != 3) {
                            comandoIncorrecto();
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
                                imprimirMapa();
                            }
                        }
                        break;
                    case "obtener":
                        if (partes.length == 3) {
                            if (partes[1].equals("color")) {
                                obtenerColor(partes[2]);
                            } else if (partes[1].equals("frontera")) {
                                obtenerFronteras(partes[2]);
                            } else if(partes[1].equals("paises")){
                                obtenerPaisesContinente(partes[2]);
                            } else {
                                comandoIncorrecto();
                            }
                        }
                        break;
                    case "atacar":
                        if (partes.length==3) {
                            atacar(partes[1], partes[2]);
                        } else if (partes.length==5) {
                            atacar(partes[1], partes[2], partes[3], partes[4]);
                        } else {
                            comandoIncorrecto();
                        }
                        break;
                    case "repartir":
                        if (partes.length == 2 && partes[1].equals("ejercitos")) {
                            repartirEjercitos();
                        } else if (partes.length==4 && partes[1].equals("ejercitos")) {
                            repartirEjercitos(partes[2], partes[3]);
                        } else {
                            comandoIncorrecto();
                        }
                    case "describir":
                        if (partes.length == 3) {
                            if (partes[1].equals("pais")) {
                                describirPais(partes[2]);
                            } else if (partes[1].equals("continente")) {
                                describirContinente(partes[2]);
                            } else if (partes[1].equals("jugador")) {
                                describirJugador(partes[2]);
                            } else {
                                comandoIncorrecto();
                            }
                        } else {
                            comandoIncorrecto();
                        }
                        break;
                    case "jugador":
                        if (partes.length == 1) {
                            describirJugadorActual();
                        } else {
                            comandoIncorrecto();
                        }
                        break;
                    case "acabar":
                        if(partes.length == 2 && partes[1].equals("turno")){
                            acabarTurno();
                        }
                        else{
                            comandoIncorrecto();
                        }
                        break;
                    default:
                        comandoIncorrecto();
                }
            } while ((orden = io.readLine()) != null);
        } catch (Exception excepcion) {
            excepcion.printStackTrace();
        }

        io.escribirFinComandos();
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
        Pais pais;
        try {
            pais = Mapa.getMapa().getPais(nombrePais);
            pais.setJugador(Partida.getPartida().getJugador(nombreJugador));
            Partida.getPartida().getJugador(nombreJugador).asignarEjercitosAPais(1, pais);
            Set<String> fronterasPais = Mapa.getMapa().getNombresPaisesFrontera(pais);
            io.printToOutput(OutputBuilder.beginBuild().autoAdd("nombre", nombreJugador).autoAdd("pais", nombrePais)
                    .autoAdd("continente", pais.getContinente().getNombreHumano()).autoAdd("frontera", fronterasPais)
                    .build());
        } catch (RiskException e) {
            io.printToErrOutput(e);
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
        } catch (ExcepcionGeo e) {
            io.printToErrOutput(e);
        }
    }

    /**
     * Imprime el error 99 (Comando no permitido en este momento)
     */
    private void comandoNoPermitido() {
        io.printToErrOutput(RiskExceptionEnum.COMANDO_NO_PERMITIDO.get());
    }

    /**
     * Imprime el error 101 (Comando incorrecto)
     */
    private void comandoIncorrecto() {
        io.printToErrOutput(RiskExceptionEnum.COMANDO_INCORRECTO.get());
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
                try {
                    Color color = Color.getColorByString(partesLinea[1]);
                    Partida.getPartida().addJugador(new Jugador(partesLinea[0], color));
                    io.printToOutput(OutputBuilder.beginBuild().autoAdd("nombre", partesLinea[0])
                            .autoAdd("color", color.getNombre()).build());
                } catch (RiskException e) {
                    io.printToErrOutput(e);
                }
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
        try {
            Jugador jugador = new Jugador(nombre, Color.getColorByString(color));
            Partida.getPartida().addJugador(jugador);
            io.printToOutput(
                    OutputBuilder.beginBuild().autoAdd("nombre", jugador.getNombre()).autoAdd("color", color).build());
        } catch (ExcepcionGeo | ExcepcionJugador e) {
            io.printToErrOutput(e);
        }
    }

    /**
     * Se encarga automáticamente de realizar el reparto de los ejércitos.
     */
    private void repartirEjercitos() {
        try {
            Partida.getPartida().repartirEjercitos();
        } catch (ExcepcionGeo e) {
            io.printToErrOutput(e);
        }
    }

    /**
     * Manualmente, se asignan X ejercitos a un país
     * 
     * @param numero
     * @param nombrePais
     */
    private void repartirEjercitos(String numero, String nombrePais) {
        try {
            Partida.getPartida().repartirEjercitos(Integer.parseInt(numero), Mapa.getMapa().getPais(nombrePais));
        } catch (RiskException e) {
            io.printToErrOutput(e);
        }
    }

    /**
     * Imprime las fronteras de un país
     * 
     * @param codigoPais
     */
    private void obtenerFronteras(String codigoPais) {
        try {
            Set<String> nombresPaisesFronteras = Mapa.getMapa()
                    .getNombresPaisesFrontera(Mapa.getMapa().getPais(codigoPais));
            io.printToOutput(OutputBuilder.beginBuild().autoAdd("frontera", nombresPaisesFronteras).build()); // Lo
                                                                                                              // sacamos
                                                                                                              // a
                                                                                                              // la
                                                                                                              // salida
        } catch (ExcepcionGeo e) {
            io.printToErrOutput(e);
        }
    }

    /**
     * Imprime el continente al que pertenece un pais
     * 
     * @param abrevPais
     */
    private void obtenerContinente(String abrevPais) {
        String Continente;
        try {
            Continente = Mapa.getMapa().getPais(abrevPais).getContinente().getNombreHumano();
            io.printToOutput(OutputBuilder.beginBuild().autoAdd("Continente", Continente).build());
        } catch (ExcepcionGeo e) {
            io.printToErrOutput(e);
        }
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
            try {
                par.add(Mapa.getMapa().getPais(paises[0]));
                par.add(Mapa.getMapa().getPais(paises[1]));
            } catch (ExcepcionGeo e) { // Sería conveniente implementar manejo de excepciones
            }
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
        try {
            color = Mapa.getMapa().getPais(abrevPais).getContinente().getColor();
            io.printToOutput(OutputBuilder.beginBuild().autoAdd("color", color.getNombre()).build());
        } catch (ExcepcionGeo e) {
            io.printToErrOutput(e);
        }
    }

    /**
     * Muestra las características de un Continente
     * 
     * @param abrevContinente
     */
    private void describirContinente(String abrevContinente) {
        try {
            Continente continente = Mapa.getMapa().getContinente(abrevContinente);
            Set<String> listaJugs = continente.getJugadores().stream()
                    .map(j -> "{ " + j.getNombre() + ", " + String.valueOf(j.getTotalEjercitos()) + " }")
                    .collect(Collectors.toSet());
            String output = OutputBuilder.beginBuild().autoAdd("nombre", continente.getNombreHumano())
                    .autoAdd("abreviatura", continente.getCodigo()).disableQuoting().autoAdd("jugadores", listaJugs)
                    .enableQuoting()
                    .autoAdd("numeroEjercitos", continente.getPaises().stream().reduce(0,
                            (total, pais) -> total + pais.getNumEjercitos(), Integer::sum))
                    .autoAdd("rearme", "RELLENAR").build();
            io.printToOutput(output);
        } catch (ExcepcionGeo e) {
            io.printToErrOutput(e);
        }
    }

    /**
     * Muestra las características de un jugador
     * 
     * @param nombre
     */
    private void describirJugador(String nombre) {
        try {
            Jugador jugador = Partida.getPartida().getJugador(nombre);
            String output = OutputBuilder.beginBuild().autoAdd("nombre", jugador.getNombre())
                    .autoAdd("color", jugador.getColor().getNombre()).autoAdd("mision", "RELLENAR")
                    .autoAdd("numeroEjercitos", jugador.getTotalEjercitos())
                    .autoAdd("paises", jugador.getPaises())
                    .autoAdd("continentes",
                            jugador.getPaises().stream().map(p -> p.getContinente()).distinct()
                                    .collect(Collectors.toSet()))
                    .autoAdd("cartas", "RELLENAR").autoAdd("numEjercitoRearme", "RELLENAR").build();
            io.printToOutput(output);
        } catch (ExcepcionJugador e) {
            io.printToErrOutput(e);
        }
    }

    /**
     * Muestra las características del jugador que tiene el turno actual
     * 
     * @param nombre
     */
    private void describirJugadorActual() {
        describirJugador(Partida.getPartida().getJugadorActual().getNombre());
    }

    /**
     * Muestra las caracteristicas de un pais: nombre, abreviatura, continente,
     * fronteras, jugador al que pertenece, numero de ejercitos que lo ocupan y el
     * numero de veces que ha sido conquistado
     * 
     * @param abrevPais
     */
    private void describirPais(String abrevPais) {
        Pais pais;

        try {
            pais = Mapa.getMapa().getPais(abrevPais);
            io.printToOutput(OutputBuilder.beginBuild().autoAdd("nombre", pais.getNombreHumano())
                    .autoAdd("abreviatura", pais.getCodigo())
                    .autoAdd("continente", pais.getContinente().getNombreHumano())
                    .autoAdd("frontera", Mapa.getMapa().getNombresPaisesFrontera(pais))
                    .autoAdd("jugador", Optional.ofNullable(pais.getJugador()).map(j -> j.getNombre()).orElse(null))
                    .autoAdd("numeroEjercitos", pais.getNumEjercitos())
                    .autoAdd("numeroVecesOcupado", pais.getNumVecesConquistado()).build());
        } catch (ExcepcionGeo e) {
            io.printToErrOutput(e);
        }

    }

    /**
     * Comando manual para realizar un ataque.
     * @param nombrePais1
     * @param dadosAtaque
     * @param nombrePais2
     * @param dadosDefensa
     */
    private void atacar(String nombrePais1, String dadosAtaque, String nombrePais2, String dadosDefensa) {

    }

    private void atacar(String nombrePaisAtacante, String nombrePaisDefensor) {
        try {
            Pais paisAtacante = Mapa.getMapa().getPais(nombrePaisAtacante);
            Pais paisDefensor = Mapa.getMapa().getPais(nombrePaisDefensor);
            int ejercitosPaisAtaqueAntes;
            int ejercitosPaisDefensaAntes;
            Optional<Continente> continenteConquistado;

            ejercitosPaisAtaqueAntes = paisAtacante.getNumEjercitos();
            ejercitosPaisDefensaAntes = paisDefensor.getNumEjercitos();

            Map<Pais, Set<Integer>> resultadoAtacar = Partida.getPartida().atacar(paisAtacante, paisDefensor);

            if (paisDefensor.getContinente().getJugadores().size() == 1) { // Solo queda un Jugador en el continente del
                                                                           // país defendido, esto implica que es el
                                                                           // jugador
                                                                           // que ataca
                continenteConquistado = Optional.of(paisDefensor.getContinente());
            } else {
                continenteConquistado = Optional.empty();
            }

            io.printToOutput(OutputBuilder.beginBuild().autoAdd("dadosAtaque", resultadoAtacar.get(paisAtacante))
                    .autoAdd("dadosDefensa", resultadoAtacar.get(paisDefensor))
                    .autoAdd("ejercitosPaisAtaque", new ArrayList<Integer>() {
                        {
                            add(ejercitosPaisAtaqueAntes);
                            add(paisAtacante.getNumEjercitos());
                        }
                    }).autoAdd("ejercitosPaisDefensa", new ArrayList<Integer>() {
                        {
                            add(ejercitosPaisDefensaAntes);
                            add(paisDefensor.getNumEjercitos());
                        }
                    }).autoAdd("paisAtaquePerteneceA", paisAtacante.getJugador().getNombre())
                    .autoAdd("paisDefensaPerteneceA", paisDefensor.getJugador().getNombre())
                    .autoAdd("continenteConquistado",
                            continenteConquistado.map(continente -> continente.getCodigo()).orElse("null"))
                    .build());
        } catch (RiskException e) {
            io.printToErrOutput(e);
        }
    }

    /**
     * Imprime el mapa
     */
    private void imprimirMapa() {
        io.printToOutput(Mapa.getMapa().toString());
    }
    
    /**
     * Imprime los países de un continente
     */
    private void obtenerPaisesContinente(String abrevContinente ){
        try{
            Set<Pais> paises = Mapa.getMapa().getContinente(abrevContinente).getPaises();
            Set<String> nombresPaises = new HashSet<String>();
            for(Pais pais : paises){
                nombresPaises.add(pais.getNombreHumano());
            }
            io.printToOutput(OutputBuilder.beginBuild().autoAdd("paises", nombresPaises).build());
        }
        catch(RiskException e){
            io.printToErrOutput(e);
        }
    }

    /**
     * Acabar el turno del jugador actual
     */
    private void acabarTurno(){
        Partida.getPartida().siguienteTurno();
        io.printToOutput(OutputBuilder.beginBuild().autoAdd("nombre", Partida.getPartida().getJugadorActual().getNombre()).autoAdd("numeroEjercitosRearmar", "COMPLETAR").build());
    }

}
