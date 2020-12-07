package risk;

public class ClaseMetodoMenuHugo {
    public void Menu() {
        // METE AQUI COMENTARIOS QUE INDIQUEN QUE PASOS HAY QUE SEGUIR PARA HACER EL
        // MENU
        // crear mapa
        // crear jugadores
        // asignar cartas de misión
        // asignar paises a jugadores
        // repartir ejercitos

        // se da el turno en funcion del orden en que se crean los jugadores

        // al iniciar el turno
        // el jugador recibe un numero de ejercitos = num paises/3
        // si todos los paises de un continente son del jugador, recibe el numero de
        // ejercitos segun la tabla 4
        // si tiene 3 cartas equipamiento, se puede cambiar por ejercitos. Si tiene más
        // de 6, se cambian automaticamente
        // al cambiar cartas, si el pais asociado pertenece al jugador se le pone un
        // ejercito adidcional
        // se deben reforzar los paises con los ejercitos obtenidos al principio de
        // turno

        // durante el turno se puede:
        // cambiar cartas
        // atacar pais
        // rearmar pais
        // asignar carta

        // acabar turno
        // jugador (ver quien tiene el turno)
        // describir jugador, pais o continente
        // ver mapa

        // durante el turno se puede atacar tantos paises como se quiera mientras haya
        // ejercitos suficientes en los paises.

        // al acabar el turno, si se ha conquistado algun pais, el jugador recibe una
        // carta de quipamiento vinculada a un pais. Estas cartas se generan automatica
        // y aleatoriamente al acabar el turno
        // ademas, el jugador puede pasar tantos ejercitos como pueda de un pais a otro
        // que le pertenezca, siempre que quede en el pais de origen minimo 1 ejercito.
        // Esto se puede hacer una vez por turno
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
                        } else if (partesComando[1].equals("cartas")) {
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
