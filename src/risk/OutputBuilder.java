package risk;

import java.awt.List;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OutputBuilder {

    /**
     * Recorre todos los campos públicos de un objeto, e imprime un JSON con su
     * valor asociado
     * 
     * @param obj
     * @return
     */
    @Deprecated
    public static String buildFromObject(Object obj) {
        StringBuilder stringBuilder = new StringBuilder(); // Lo usaremos para construir la cadena JSON
        stringBuilder.append("{").append(System.getProperty("line.separator")); // La llave de apertura del JSON
        for (Field f : obj.getClass().getFields()) { // Recorremos todos los campos del objeto
            try {
                stringBuilder.append("  ").append(f.getName()).append(": \"").append(f.get(obj).toString()).append("\"")
                        .append(System.getProperty("line.separator"));
            } catch (IllegalArgumentException | IllegalAccessException e) { // En caso de que haya un error...
                Logger.getLogger(OutputBuilder.class.getName()).log(Level.WARNING,
                        "Excepción al convertir el campo {0} a un String", f.getName());
            }
        }
        stringBuilder.append("}"); // Cerramos el JSON
        return stringBuilder.toString();
    }

    /**
     * Recorre todos los métodos que empiezan por get del objeto y el valor que
     * devuelven, y genera un JSON con el resultado
     * 
     * @param obj
     * @return
     */
    public static String buildFromObjectGetters(Object obj) {
        StringBuilder stringBuilder = new StringBuilder(); // Lo usaremos para construir la cadena JSON
        stringBuilder.append("{").append(System.getProperty("line.separator")); // La llave de apertura del JSON
        for (Method m : obj.getClass().getMethods()) { // Por cada método del objeto...
            if (!m.getName().equals("getClass") && m.getName().toLowerCase().startsWith("get")
                    && (m.getParameterCount() == 0)) { // Si el método
                // empieza por get,
                // entonces obtenemos
                // el
                // valor que devuelve y lo añadimos al JSON. La excepción es el método getClass,
                // para el cual hay una excepción. Aparte, el método no puede requerir
                // argumentos (por ejemplo, si la clase tiene una lista, que el getter pida el
                // índice de la lista para extraer, ya que en ese caso lo que querríamos sería
                // obtener la lista entera)
                try {
                    String nombreObjeto;
                    try { // Tratamos de construir una cadena con el nombre del getter en formato humano.
                          // getNombreMetodo -> nombreMetodo
                        nombreObjeto = m.getName().substring(3);
                        StringBuilder builderNombreObjeto = new StringBuilder();
                        builderNombreObjeto.append(Character.toString(nombreObjeto.charAt(0)).toLowerCase());
                        builderNombreObjeto.append(nombreObjeto.substring(1));
                        nombreObjeto = builderNombreObjeto.toString(); // Ahora el nombre es el nombre en formato humano
                    } catch (IndexOutOfBoundsException e) {
                        // En este caso, hacemos el append como el nombre del método tal cual
                        nombreObjeto = m.getName();
                    }

                    stringBuilder.append("  ").append(nombreObjeto).append(": "); // Añadimos el nombre del objeto al
                                                                                  // JSON

                    String objeto;
                    if (Iterable.class.isAssignableFrom(m.getReturnType())) {
                        // El tipo de objeto devuelto es iterable, por lo que vamos a tratarlo como una
                        // lista
                        stringBuilder.append("[ ");
                        int cantidadDeSangrado = stringBuilder.toString()
                                .split(System.getProperty("line.separator"))[stringBuilder.toString()
                                        .split(System.getProperty("line.separator")).length - 1].length(); // Vamos a
                                                                                                           // mirar
                                                                                                           // dónde
                                                                                                           // empezaba
                                                                                                           // la última
                                                                                                           // línea para
                                                                                                           // saber con
                                                                                                           // cuántos
                                                                                                           // espacios
                                                                                                           // tenemos
                                                                                                           // que hacer
                                                                                                           // el
                                                                                                           // sangrado
                        Iterator<Object> iterator = ((Iterable<Object>) m.invoke(obj)).iterator();
                        while (iterator.hasNext()) {
                            stringBuilder.append("\"").append(iterator.next().toString()).append("\"");
                            if (iterator.hasNext()) { // Si este no es el último elemento, preparamos la siguiente línea
                                stringBuilder.append(",").append(System.getProperty("line.separator")); // Terminamos la
                                                                                                        // línea
                                stringBuilder.append(new String(new char[cantidadDeSangrado]).replace('\0', ' ')); // Añadimos
                                                                                                                   // una
                                                                                                                   // nueva
                                                                                                                   // línea
                                                                                                                   // toda
                                                                                                                   // de
                                                                                                                   // espacios
                            }
                        }
                        stringBuilder.append(System.getProperty("line.separator")); // Nueva línea
                        stringBuilder.append(new String(new char[cantidadDeSangrado - 2]).replace('\0', ' ')); // 2
                                                                                                                        // caracteres
                                                                                                                        // de
                                                                                                                        // sangrado
                                                                                                                        // menos
                        stringBuilder.append("]");
                    } else {
                        try {
                            objeto = m.invoke(obj).toString();
                        } catch (NullPointerException e) {
                            // El método nos ha devuelto un NULL, así que añadimos eso al JSON
                            objeto = "NULL";
                        }
                        stringBuilder.append("\"").append(objeto).append("\"");
                    }
                    stringBuilder.append(",").append(System.getProperty("line.separator")); // Añadimos una nueva línea
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    Logger.getLogger(OutputBuilder.class.getName()).log(Level.WARNING,
                            "Excepción al obtener el valor de {0}", m.getName());
                }
            }
        }
        stringBuilder.append("}"); // Cerramos el JSON
        stringBuilder.append(System.getProperty("line.separator"));
        return stringBuilder.toString();
    }

    private Map<String, String> parametros;

    /**
     * Si queremos construir el objeto JSON manualmente, usamos un OutputBuilder
     */
    public OutputBuilder() {
        parametros = new HashMap<String, String>();
    }

    public OutputBuilder addParametro(String nombre, String valor) {
        parametros.put(nombre, valor);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{").append(System.getProperty("line.separator"));
        for (Entry<String, String> entry : parametros.entrySet()) {
            stringBuilder.append(entry.getKey()).append(": \"").append(entry.getValue()).append("\"")
                    .append(System.getProperty("line.separator"));
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

}
