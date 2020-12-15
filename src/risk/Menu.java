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

import risk.cartasmision.CartaMision;
import risk.cartasmision.CartaMisionFactory;
import risk.riskexception.ExcepcionGeo;
import risk.riskexception.ExcepcionJugador;
import risk.riskexception.ExcepcionRISK;
import risk.riskexception.RiskExceptionEnum;

/**
 *
 * @author Manuel Lama
 */
public class Menu {
    // En esta clase se deberían de definir los atributos a los que será
    // necesario acceder durante la ejecución del programa como, por ejemplo,
    // el mapa o los jugadores

    private Consola io;
    {
        // Inicialización de io; podemos hacerla antes del constructor
        io = IOHelperFactory.getInstance();
    }

    private ComandoProcessor comandoProcessor;

    private void bucleProcesarComandos() {
        String orden;
        while ((orden = io.readLine()) != null) {
            comandoProcessor.procesarComando(orden);
        }
    }

    public Menu() {
        // Inicialización de algunos atributos
        comandoProcessor = new ComandoProcessor(this);
        bucleProcesarComandos();

        io.escribirFinComandos();
    }

    /**
     * Procesa un archivo con [NombreJugador];[NombrePais] para realizar las
     * asignaciones en el mapa
     * 
     * @param archivoAsignaciones
     */
    public void asignarPaises(String archivoAsignaciones) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(archivoAsignaciones)));
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                String partes[] = linea.split(";");
                String nombrePais = partes[1];
                String nombreJugador = partes[0];

                asignarPais(nombreJugador, nombrePais);
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
    public void asignarPais(String nombreJugador, String nombrePais) {
        try {
            Mapa.getMapa().asignarPaisAJugadorInicialmente(nombrePais, nombreJugador);
            Set<String> fronterasPais = Mapa.getMapa().getNombresPaisesFrontera(Mapa.getMapa().getPais(nombrePais));
            io.printToOutput(OutputBuilder.beginBuild().autoAdd("nombre", nombreJugador).autoAdd("pais", nombrePais)
                    .autoAdd("continente", Mapa.getMapa().getPais(nombrePais).getContinente().getNombreHumano())
                    .autoAdd("frontera", fronterasPais).build());
        } catch (ExcepcionRISK e) {
            io.printToErrOutput(e);
        }
    }

    /**
     *
     */
    public void crearMapa() {
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
    public void comandoNoPermitido() {
        io.printToErrOutput(RiskExceptionEnum.COMANDO_NO_PERMITIDO.get());
    }

    /**
     * Imprime el error 101 (Comando incorrecto)
     */
    public void comandoIncorrecto() {
        io.printToErrOutput(RiskExceptionEnum.COMANDO_INCORRECTO.get());
    }

    /**
     *
     * @param file
     */
    public void crearJugadores(String file) {
        // Código necesario para crear a los jugadores del RISK
        try {
            FileReader lector = new FileReader(new File(file));
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
                } catch (ExcepcionRISK e) {
                    io.printToErrOutput(e);
                }
            }

            bufferLector.close();
        } catch (FileNotFoundException fileNotFoundException) {
            // Si no se encuentra el archivo, falla el programa
            fileNotFoundException.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Crea un jugador a partir de un nombre y un color
     * 
     * @param file
     */
    public void crearJugador(String nombre, String color) {
        // Código necesario para crear a un jugador a partir de su nombre y color
        try {
            Jugador jugador = new Jugador(nombre, Color.getColorByString(color));
            Partida.getPartida().addJugador(jugador);
            io.printToOutput(OutputBuilder.beginBuild().autoAdd("nombre", jugador.getNombre())
                    .autoAdd("color", Color.getColorByString(color).getNombre()).build());
        } catch (ExcepcionGeo | ExcepcionJugador e) {
            io.printToErrOutput(e);
        }
    }

    /**
     * Se encarga automáticamente de realizar el reparto de los ejércitos.
     */
    public void repartirEjercitos() {
        try {
            Partida.getPartida().repartirEjercitos();
        } catch (ExcepcionGeo e) {
            io.printToErrOutput(e);
        }
    }

    /**
     * Comando "cambiar cartas <id1> <id2> <id3>"
     */
    public void cambiarCartas(String carta1, String carta2, String carta3) {

    }

    /**
     * Comando "cambiar cartas todas"
     */
    public void cambiarCartasTodas() {

    }

    /**
     * Manualmente, se asignan X ejercitos a un país
     * 
     * @param numero
     * @param nombrePais
     */
    public void repartirEjercitos(String numero, String nombrePais) {
        try {
            Pais pais = Mapa.getMapa().getPais(nombrePais);
            int numeroEjercitosAsignados = Partida.getPartida().repartirEjercitos(Integer.parseInt(numero),
                    Mapa.getMapa().getPais(nombrePais));
            Set<String> setPaisesOcupadosContinente = pais.getContinente().getPaises().stream()
                    .filter(p -> p.getJugador().equals(pais.getJugador()))
                    .map(p -> "{ \"" + p.getNombreHumano() + "\", " + Integer.toString(p.getNumEjercitos()) + " }")
                    .collect(Collectors.toSet());
            String output = OutputBuilder.beginBuild().autoAdd("pais", nombrePais)
                    .autoAdd("jugador", pais.getJugador().getNombre())
                    .autoAdd("numeroEjercitosAsignados", numeroEjercitosAsignados)
                    .autoAdd("numeroEjercitosTotales", pais.getNumEjercitos()).disableQuoting()
                    .autoAdd("paisesOcupadosContinente", setPaisesOcupadosContinente).build();
            io.printToOutput(output);
        } catch (ExcepcionRISK e) {
            io.printToErrOutput(e);
        }
    }

    /**
     * Imprime las fronteras de un país
     * 
     * @param codigoPais
     */
    public void obtenerFronteras(String codigoPais) {
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
    public void obtenerContinente(String abrevPais) {
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
    public void anadirFronterasIndirectas() {
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
    public void obtenerColor(String abrevPais) {
        Color color;
        try {
            color = Mapa.getMapa().getPais(abrevPais).getContinente().getColor();
            io.printToOutput(OutputBuilder.beginBuild().autoAdd("color", color.getNombre()).build());
        } catch (ExcepcionGeo e) {
            io.printToErrOutput(e);
        }
    }

    /**
     * Lee el fichero del nombreFichero especificado, y asigna las CartasMision que
     * allí se especifican a los Jugadores de esta Partida
     */
    public void asignarMisiones(String nombreFichero) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(nombreFichero)));
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                String partesLinea[] = linea.split(";");
                asignarMisionJugador(partesLinea[0], partesLinea[1]);
            }
        } catch (IOException e) {
            io.printToErrOutput(new ExcepcionRISK(0, "Error de lectura del archivo") {
            });
        } catch (ArrayIndexOutOfBoundsException e) {
            io.printToErrOutput(new ExcepcionRISK(0, "El archivo de lectura de misiones tiene un formato erróneo") {
            });
        }
    }

    /**
     * Asigna la CartaMision del ID especificado al Jugador
     */
    public void asignarMisionJugador(String nombreJugador, String idMision) {
        try {
            Jugador jugadorActual = Partida.getPartida().getJugador(nombreJugador);
            CartaMision mision = CartaMisionFactory.build(idMision, jugadorActual);
            Partida.getPartida().asignarCartaMisionJugador(mision, jugadorActual);
            io.printToOutput(OutputBuilder.beginBuild().autoAdd("nombre", nombreJugador)
                    .autoAdd("mision", mision.getDescripcion()).build());
        } catch (ExcepcionRISK e) {
            io.printToErrOutput(e);
        }
    }

    /**
     * Muestra las características de un Continente
     * 
     * @param abrevContinente
     */
    public void describirContinente(String abrevContinente) {
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
    public void describirJugador(String nombre) {
        try {
            Jugador jugador = Partida.getPartida().getJugador(nombre);
            OutputBuilder output = OutputBuilder.beginBuild().autoAdd("nombre", jugador.getNombre()).autoAdd("color",
                    jugador.getColor().getNombre());
            if (jugador.equals(Partida.getPartida().getJugadorActual())) { // Mostramos la misión solo si es el jugador
                                                                           // actual
                output.autoAdd("mision", jugador.getCartaMision());
            }
            output.autoAdd("numeroEjercitos", jugador.getTotalEjercitos()).autoAdd("paises", jugador.getPaises())
                    .autoAdd("continentes", jugador.getContinentesOcupadosExcusivamentePorJugador())
                    .autoAdd("cartas", "RELLENAR").autoAdd("numEjercitoRearme", "RELLENAR");
            io.printToOutput(output.build());
        } catch (ExcepcionJugador e) {
            io.printToErrOutput(e);
        }
    }

    /**
     * Muestra las características del jugador que tiene el turno actual
     * 
     * @param nombre
     */
    public void describirJugadorActual() {
        describirJugador(Partida.getPartida().getJugadorActual().getNombre());
    }

    /**
     * Acaba un turno durante la partida
     */
    public void acabarTurno() {
        Partida.getPartida().siguienteTurno();
        io.printToOutput(OutputBuilder.beginBuild()
                .autoAdd("nombre", Partida.getPartida().getJugadorActual().getNombre())
                .autoAdd("numeroEjercitosRearmar", Partida.getPartida().getJugadorActual().getNumEjercitosRearme()).build());
    }

    /**
     * Durante el reparto inicial de ejércitos, acaba un turno de reparto
     */
    public void acabarTurnoReparto() {
        Partida.getPartida().siguienteTurnoDeReparto();
        io.printToOutput(OutputBuilder.beginBuild()
                .autoAdd("nombre", Partida.getPartida().getJugadorActual().getNombre())
                .autoAdd("numeroEjercitosRearmar", Partida.getPartida().getJugadorActual().getNumEjercitosRearme()).build());
    }

    /**
     * Muestra las caracteristicas de un pais: nombre, abreviatura, continente,
     * fronteras, jugador al que pertenece, numero de ejercitos que lo ocupan y el
     * numero de veces que ha sido conquistado
     * 
     * @param abrevPais
     */
    public void describirPais(String abrevPais) {
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
     * 
     * @param nombrePais1
     * @param dadosAtaque
     * @param nombrePais2
     * @param dadosDefensa
     */
    public void atacar(String nombrePais1, String dadosAtaque, String nombrePais2, String dadosDefensa) {
        try {
            Pais paisAtacante = Mapa.getMapa().getPais(nombrePais1);
            Pais paisDefensor = Mapa.getMapa().getPais(nombrePais2);
            int ejercitosPaisAtaqueAntes;
            int ejercitosPaisDefensaAntes;
            Optional<Continente> continenteConquistado;

            ejercitosPaisAtaqueAntes = paisAtacante.getNumEjercitos();
            ejercitosPaisDefensaAntes = paisDefensor.getNumEjercitos();

            Map<Pais, Set<Dado>> resultadoAtacar = Partida.getPartida().atacar(paisAtacante,
                    parsearStringDados(dadosAtaque), paisDefensor, parsearStringDados(dadosDefensa));

            if (paisDefensor.getContinente().getJugadores().size() == 1) { // Solo queda un Jugador en el continente del
                                                                           // país defendido, esto implica que es el
                                                                           // jugador
                                                                           // que ataca
                continenteConquistado = Optional.of(paisDefensor.getContinente());
            } else {
                continenteConquistado = Optional.empty();
            }
            io.printToOutput(OutputBuilder.beginBuild().disableQuoting()
                    .autoAdd("dadosAtaque",
                            resultadoAtacar.get(paisAtacante).stream().map(Dado::getValor).collect(Collectors.toList()))
                    .autoAdd("dadosDefensa",
                            resultadoAtacar.get(paisDefensor).stream().map(Dado::getValor).collect(Collectors.toList()))
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
                    }).enableQuoting().autoAdd("paisAtaquePerteneceA", paisAtacante.getJugador().getNombre())
                    .autoAdd("paisDefensaPerteneceA", paisDefensor.getJugador().getNombre())
                    .autoAdd("continenteConquistado",
                            continenteConquistado.map(continente -> continente.getCodigo()).orElse("null"))
                    .build());
        } catch (ExcepcionRISK e) {
            io.printToErrOutput(e);
        }
    }

    public Set<Dado> parsearStringDados(String stringDados) {
        Set<Dado> setRetorno = new HashSet<>();
        String dados[] = stringDados.split("x");
        for (int i = 0; i < dados.length; i++) {
            int numeroDado;
            Dado dadoActual;
            numeroDado = Integer.parseInt(dados[i]);
            dadoActual = new Dado(numeroDado);
            setRetorno.add(dadoActual);
        }
        return setRetorno;
    }

    public void atacar(String nombrePaisAtacante, String nombrePaisDefensor) {
        try {
            Pais paisAtacante = Mapa.getMapa().getPais(nombrePaisAtacante);
            Pais paisDefensor = Mapa.getMapa().getPais(nombrePaisDefensor);
            int ejercitosPaisAtaqueAntes;
            int ejercitosPaisDefensaAntes;
            Optional<Continente> continenteConquistado;

            ejercitosPaisAtaqueAntes = paisAtacante.getNumEjercitos();
            ejercitosPaisDefensaAntes = paisDefensor.getNumEjercitos();

            Map<Pais, Set<Dado>> resultadoAtacar = Partida.getPartida().atacar(paisAtacante, paisDefensor);

            if (paisDefensor.getContinente().getJugadores().size() == 1) { // Solo queda un Jugador en el continente del
                                                                           // país defendido, esto implica que es el
                                                                           // jugador
                                                                           // que ataca
                continenteConquistado = Optional.of(paisDefensor.getContinente());
            } else {
                continenteConquistado = Optional.empty();
            }

            io.printToOutput(OutputBuilder.beginBuild()
                    .autoAdd("dadosAtaque",
                            resultadoAtacar.get(paisAtacante).stream().map(Dado::getValor).collect(Collectors.toSet()))
                    .autoAdd("dadosDefensa",
                            resultadoAtacar.get(paisDefensor).stream().map(Dado::getValor).collect(Collectors.toSet()))
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
        } catch (ExcepcionRISK e) {
            io.printToErrOutput(e);
        }
    }

    /**
     * Imprime el mapa
     */
    public void imprimirMapa() {
        io.printToOutput(Mapa.getMapa().toString());
    }

    /**
     * Imprime los países de un continente
     */
    public void obtenerPaisesContinente(String abrevContinente) {
        try {
            Set<Pais> paises = Mapa.getMapa().getContinente(abrevContinente).getPaises();
            Set<String> nombresPaises = new HashSet<String>();
            for (Pais pais : paises) {
                nombresPaises.add(pais.getNombreHumano());
            }
            io.printToOutput(OutputBuilder.beginBuild().autoAdd("paises", nombresPaises).build());
        } catch (ExcepcionRISK e) {
            io.printToErrOutput(e);
        }
    }

}
