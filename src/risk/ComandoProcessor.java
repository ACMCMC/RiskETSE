package risk;

/**
 * Clase auxiliar para procesar los comandos. Guarda la información del estado actual.
 */
public class ComandoProcessor {
    private enum ComandoProcessorStatus {
        CREANDO_MAPA, CREANDO_JUGADORES, ASIGNANDO_MISIONES, ASIGNANDO_PAISES, REPARTIENDO_EJERCITOS, JUGANDO_CAMBIANDO_CARTAS, JUGANDO_REPARTIENDO_EJERCITOS, JUGANDO, JUGANDO_REARMANDO;
    }

    private ComandoProcessorStatus status;
    private Menu menu;

    ComandoProcessor(Menu menu) {
        this.status = ComandoProcessorStatus.CREANDO_MAPA;
        this.menu = menu;
    }

    public void procesarComando(String comando) {
        if (status.equals(ComandoProcessorStatus.CREANDO_MAPA)) {
            procesarComandoCreandoMapa(comando);
        } else if (status.equals(ComandoProcessorStatus.CREANDO_JUGADORES)) {
            procesarComandoCreandoJugadores(comando);
        } else if (status.equals(ComandoProcessorStatus.ASIGNANDO_MISIONES)) {
            procesarComandoAsignandoMisiones(comando);
        } else if (status.equals(ComandoProcessorStatus.ASIGNANDO_PAISES)) {
            procesarComandoAsignandoPaises(comando);
        } else if (status.equals(ComandoProcessorStatus.REPARTIENDO_EJERCITOS)) {
            procesarComandoRepartiendoEjercitos(comando);
        } else if (status.equals(ComandoProcessorStatus.JUGANDO_CAMBIANDO_CARTAS)) {
            procesarComandoJugandoCambiandoCartas(comando);
        } else if (status.equals(ComandoProcessorStatus.JUGANDO_REPARTIENDO_EJERCITOS)) {
            procesarComandoJugandoRepartiendoEjercitos(comando);
        } else if (status.equals(ComandoProcessorStatus.JUGANDO_REARMANDO)) {
            procesarComandoJugandoRearmando(comando);
        } else if (status.equals(ComandoProcessorStatus.JUGANDO)) {
            procesarComandoJugando(comando);
        }
    }

    private void imprimirComandoNoPermitidoOIncorrecto(String comando) {
        if (comprobarSiComandoEsSintacticamenteCorrecto(comando)) {
            menu.comandoNoPermitido();
        } else {
            menu.comandoIncorrecto();
        }
    }

    private void procesarComandoCreandoMapa(String comando) {
        if (comando.equals("crear mapa")) {
            menu.crearMapa();
            menu.anadirFronterasIndirectas(); // Esto hay que hacerlo manualmente, porque la clase Mapa no sabe cuáles
                                              // son
            // las fronteras indirectas
            menu.verMapa(); // Imprimimos el mapa una vez creado
            this.status = ComandoProcessorStatus.CREANDO_JUGADORES;
        } else if (comando.startsWith("crear ")) { // Si el comando es "crear jugador" o "crear jugadores", lo
                                                   // ejecutamos, aunque ya sepamos de antemano que va a fallar
            String partes[] = comando.split(" ");
            if (partes.length == 3 && partes[1].equals("jugadores")) {
                menu.crearJugadores(partes[2]);
            } else if (partes.length == 3) {
                menu.crearJugador(partes[1], partes[2]);
            } else {
                imprimirComandoNoPermitidoOIncorrecto(comando);
            }
        } else {
            imprimirComandoNoPermitidoOIncorrecto(comando);
        }
    }

    private void procesarComandoCreandoJugadores(String comando) {
        String partes[] = comando.split(" ");
        if (partes.length == 3 && partes[0].equals("crear") && partes[1].equals("jugadores")) {
            menu.crearJugadores(partes[2]);
        } else if (partes.length == 3 && partes[0].equals("crear")) {
            menu.crearJugador(partes[1], partes[2]);
        } else if (Partida.getPartida().areJugadoresCreados() && comprobarSiComandoEsSintacticamenteCorrecto(comando)) {
            this.status = ComandoProcessorStatus.ASIGNANDO_MISIONES;
            this.procesarComando(comando);
        } else if (partes.length == 4 && partes[0].equals("asignar") && partes[1].equals("mision")) {
            menu.asignarMisionJugador(partes[2], partes[3]);
        } else if (partes.length == 3 && partes[0].equals("asignar") && partes[1].equals("misiones")) {
            menu.asignarMisiones(partes[2]);
        } else {
            imprimirComandoNoPermitidoOIncorrecto(comando);
        }
    }

    private void procesarComandoAsignandoMisiones(String comando) {
        String partes[] = comando.split(" ");
        if (partes.length == 4 && partes[0].equals("asignar") && partes[1].equals("mision")) {
            menu.asignarMisionJugador(partes[2], partes[3]);
        } else if (partes.length == 3 && partes[0].equals("asignar") && partes[1].equals("misiones")) {
            menu.asignarMisiones(partes[2]);
        } else if (Partida.getPartida().areMisionesAsignadas() && comprobarSiComandoEsSintacticamenteCorrecto(comando)) {
            this.status = ComandoProcessorStatus.ASIGNANDO_PAISES;
            Partida.getPartida().asignarEjercitosSinRepartir();
            this.procesarComando(comando);
        } else if (partes.length == 4 && partes[0].equals("asignar") && partes[1].equals("pais")) {
            menu.asignarPais(partes[2], partes[3]);
        } else if (partes.length == 3 && partes[0].equals("asignar") && partes[1].equals("paises")) {
            menu.asignarPaises(partes[2]);
        } else {
            imprimirComandoNoPermitidoOIncorrecto(comando);
        }
    }

    private void procesarComandoAsignandoPaises(String comando) {
        String partes[] = comando.split(" ");
        if (partes.length == 4 && partes[0].equals("asignar") && partes[1].equals("pais")) {
            menu.asignarPais(partes[2], partes[3]);
        } else if (partes.length == 3 && partes[0].equals("asignar") && partes[1].equals("paises")) {
            menu.asignarPaises(partes[2]);
        } else if (Mapa.getMapa().arePaisesAsignados() && comprobarSiComandoEsSintacticamenteCorrecto(comando)) {
            this.status = ComandoProcessorStatus.REPARTIENDO_EJERCITOS;
            this.procesarComando(comando);
        } else {
            imprimirComandoNoPermitidoOIncorrecto(comando);
        }
    }
    
    private void procesarComandoRepartiendoEjercitos(String comando) {
        String partes[] = comando.split(" ");
        if (partes.length == 4 && partes[0].equals("repartir") && partes[1].equals("ejercitos")) {
            menu.repartirEjercitos(partes[2], partes[3]);
        } else if (partes.length == 2 && partes[0].equals("repartir") && partes[1].equals("ejercitos")) {
            menu.repartirEjercitos();
            this.status = ComandoProcessorStatus.JUGANDO_CAMBIANDO_CARTAS; // Ya no vamos a permitir ejecutar manualmente el reparto de ejércitos
        } else if (!Partida.getPartida().areEjercitosRepartidos() && partes.length == 2 && partes[0].equals("acabar") && partes[1].equals("turno")) {
            menu.acabarTurnoReparto();
        } else if (Partida.getPartida().areEjercitosRepartidos() && partes.length == 2 && partes[0].equals("acabar") && partes[1].equals("turno")) {
            this.status = ComandoProcessorStatus.JUGANDO_CAMBIANDO_CARTAS;
            menu.acabarTurno();
        } else {
            imprimirComandoNoPermitidoOIncorrecto(comando);
        }
    }
    
    private void procesarComandoJugandoCambiandoCartas(String comando) {
        String partes[] = comando.split(" ");
        if (partes.length == 5 && partes[0].equals("cambiar") && partes[1].equals("cartas")) {
            menu.cambiarCartas(partes[2], partes[3], partes[4]);
        } else if (partes.length == 3 && partes[0].equals("cambiar") && partes[1].equals("cartas") && partes[2].equals("todas")) {
            this.status = ComandoProcessorStatus.JUGANDO_REPARTIENDO_EJERCITOS;
            menu.cambiarCartasTodas();
        } else if (isComandoSiemprePermitidoDuranteJuego(comando)) {
            this.procesarComandoSiemprePermitidoDuranteJuego(comando);
        } else if (comprobarSiComandoEsSintacticamenteCorrecto(comando)) {
            this.status = ComandoProcessorStatus.JUGANDO_REPARTIENDO_EJERCITOS;
            this.procesarComando(comando);
        } else {
            imprimirComandoNoPermitidoOIncorrecto(comando);
        }
    }
    
    private void procesarComandoJugandoRepartiendoEjercitos(String comando) {
        String partes[] = comando.split(" ");
        if (partes.length == 4 && partes[0].equals("repartir") && partes[1].equals("ejercitos")) {
            menu.repartirEjercitos(partes[2], partes[3]);
        } else if (isComandoSiemprePermitidoDuranteJuego(comando)) {
            this.procesarComandoSiemprePermitidoDuranteJuego(comando);
        } else if (Partida.getPartida().areEjercitosRepartidos() && comprobarSiComandoEsSintacticamenteCorrecto(comando)) {
            this.status = ComandoProcessorStatus.JUGANDO;
            this.procesarComando(comando);
        } else {
            imprimirComandoNoPermitidoOIncorrecto(comando);
        }
    }

    private void procesarComandoJugandoRearmando(String comando) {
        String partes[] = comando.split(" ");
        if (partes.length == 4 && partes[0].equals("rearmar")) {
            menu.rearmar(partes[1], partes[2], partes[3]);
        } else if (partes.length == 3 && partes[0].equals("asignar") && partes[1].equals("carta")) {
            menu.asignarCarta(partes[2]);
        } else if (isComandoSiemprePermitidoDuranteJuego(comando)) {
            this.procesarComandoSiemprePermitidoDuranteJuego(comando);
        } else if (comprobarSiComandoEsSintacticamenteCorrecto(comando)) {
            this.status = ComandoProcessorStatus.JUGANDO_CAMBIANDO_CARTAS;
            this.procesarComando(comando);
        } else {
            imprimirComandoNoPermitidoOIncorrecto(comando);
        }
    }
    
    private void procesarComandoJugando(String comando) {
        String partes[] = comando.split(" ");
        String com = partes[0];
        switch (com) {
            case "rearmar":
                if (partes.length == 4) {
                    this.status = ComandoProcessorStatus.JUGANDO_REARMANDO;
                    menu.rearmar(partes[1], partes[2], partes[3]);
                } else {
                    imprimirComandoNoPermitidoOIncorrecto(comando);
                }
            break;
            case "ver":
            if (partes.length == 2) {
                if (partes[1].equals("mapa")) {
                    menu.verMapa();
                }
            }
            break;
            case "obtener":
            if (partes.length == 3) {
                    if (partes[1].equals("color")) {
                        menu.obtenerColor(partes[2]);
                    } else if (partes[1].equals("frontera")) {
                        menu.obtenerFronteras(partes[2]);
                    } else if (partes[1].equals("paises")) {
                        menu.obtenerPaisesContinente(partes[2]);
                    } else {
                        menu.comandoIncorrecto();
                    }
                }
                break;
            case "atacar":
                if (partes.length == 3) {
                    menu.atacar(partes[1], partes[2]);
                } else if (partes.length == 5) {
                    menu.atacar(partes[1], partes[2], partes[3], partes[4]);
                } else {
                    menu.comandoIncorrecto();
                }
                break;
            case "describir":
                if (partes.length == 3) {
                    if (partes[1].equals("pais")) {
                        menu.describirPais(partes[2]);
                    } else if (partes[1].equals("continente")) {
                        menu.describirContinente(partes[2]);
                    } else if (partes[1].equals("jugador")) {
                        menu.describirJugador(partes[2]);
                    } else {
                        menu.comandoIncorrecto();
                    }
                } else {
                    menu.comandoIncorrecto();
                }
                break;
            case "jugador":
                if (partes.length == 1) {
                    menu.describirJugadorActual();
                } else {
                    menu.comandoIncorrecto();
                }
                break;
            case "acabar":
                if (partes.length == 2 && partes[1].equals("turno")) {
                    status=ComandoProcessorStatus.JUGANDO_CAMBIANDO_CARTAS;
                    menu.acabarTurno();
                } else {
                    imprimirComandoNoPermitidoOIncorrecto(comando);
                }
                break;
            case "asignar":
                if (partes.length==3 && partes[1].equals("carta")) {
                    menu.asignarCarta(partes[2]);
                } else {
                    imprimirComandoNoPermitidoOIncorrecto(comando);
                }
            break;
            default:
                imprimirComandoNoPermitidoOIncorrecto(comando);
        }
    }

    public void procesarComandoSiemprePermitidoDuranteJuego(String comando) {
        String partes[] = comando.split(" ");
        if (partes.length == 3 && partes[0].equals("describir")) {
            if (partes[1].equals("jugador")) {
                menu.describirJugador(partes[2]);
            }
            if (partes[1].equals("pais")) {
                menu.describirPais(partes[2]);
            }
            if (partes[1].equals("continente")) {
                menu.describirContinente(partes[2]);
            }
        }
        if (partes.length==2 && partes[0].equals("ver") && partes[1].equals("mapa")) {
            menu.verMapa();
        }
        if (partes.length==1 && partes[0].equals("jugador")) {
            menu.describirJugadorActual();
        }
    }

    public boolean isComandoSiemprePermitidoDuranteJuego(String comando) {
        String partes[] = comando.split(" ");
        if (partes.length == 3 && partes[0].equals("describir")) {
            if (partes[1].equals("jugador")) {
                return true;
            }
            if (partes[1].equals("pais")) {
                return true;
            }
            if (partes[1].equals("continente")) {
                return true;
            }
        }
        if (partes.length==2 && partes[0].equals("ver") && partes[1].equals("mapa")) {
            return true;
        }
        if (partes.length==1 && partes[0].equals("jugador")) {
            return true;
        }
        return false;
    }

    public boolean comprobarSiComandoEsSintacticamenteCorrecto(String comando) {
        String partesComando[] = comando.split(" ");
        if (partesComando.length > 1) {
            switch (partesComando[0]) {
                case "crear":
                    if (partesComando.length == 2 && partesComando[1].equals("mapa")) {
                        return true;
                    } else if (partesComando.length == 3) {
                        return true;
                    } else {
                        return false;
                    }

                case "obtener":
                    if (partesComando.length == 3) {
                        if (partesComando[1].equals("frontera")) {
                            return true;
                        } else if (partesComando[1].equals("continente")) {
                            return true;
                        } else if (partesComando[1].equals("color")) {
                            return true;
                        } else if (partesComando[1].equals("paises")) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }

                case "repartir":
                    if (partesComando.length == 2 && partesComando[1].equals("ejercitos")) {
                        return true;
                    } else if (partesComando.length == 4 && partesComando[1].equals("ejercitos")) {
                        return true;
                    } else {
                        return false;
                    }

                case "cambiar":
                    if (partesComando.length == 3 && partesComando[1].equals("cartas")
                            && partesComando[2].equals("todas")) {
                        return true;
                    } else if (partesComando.length == 5 && partesComando[1].equals("cartas")) {
                        return true;
                    } else {
                        return false;
                    }

                case "asignar":
                    if (partesComando.length == 3) {
                        if (partesComando[1].equals("misiones")) {
                            return true;
                        } else if (partesComando[1].equals("paises")) {
                            return true;
                        } else if (partesComando[1].equals("carta")) {
                            return true;
                        } else {
                            return true; // En cualquier caso va a ser correcto porque puede ser asignar <jugador>
                                         // <pais>
                        }
                    } else {
                        return false;
                    }

                case "describir":
                    if (partesComando.length == 3) {
                        if (partesComando[1].equals("jugador")) {
                            return true;
                        } else if (partesComando[1].equals("pais")) {
                            return true;
                        } else if (partesComando[1].equals("continente")) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }

                case "atacar":
                    if (partesComando.length == 3) {
                        return true;
                    } else if (partesComando.length == 5) {
                        return true;
                    } else {
                        return false;
                    }

                case "rearmar":
                    if (partesComando.length == 4) {
                        return true;
                    } else {
                        return false;
                    }

                case "acabar":
                    if (partesComando.length == 2 && partesComando[1].equals("turno"))
                        return true;
                    else
                        return false;

                case "ver":
                    if (partesComando.length == 2 && partesComando[1].equals("mapa"))
                        return true;
                    else
                        return false;

                default:
                    return false;
            }
        } else if (partesComando.length == 1 && partesComando[0].equals("jugador")) {
            return true;
        } else {
            return false;
        }
    }
}
