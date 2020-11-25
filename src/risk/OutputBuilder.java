/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OutputBuilder {

    private static final int nivelSangrado = 4; // El nivel de sangrado por defecto
    private static final String NEW_LINE = System.getProperty("line.separator");
    private final int DEPTH_LEVEL; // Nivel de recursividad para imprimir objetos
    private final boolean separateLines; // Si se introduce una línea nueva por cada elemento de una lista, o no

    /**
     * @deprecated Como en este proyecto no hay campos públicos, no nos va a servir.
     *             Usar en su lugar buildFromObjectGetters Recorre todos los campos
     *             públicos de un objeto, e imprime un JSON con su valor asociado
     * 
     * @param obj
     * @return
     */
    @Deprecated
    public static String buildFromObject(Object obj, int depthLevel) {
        StringBuilder stringBuilder = new StringBuilder(depthLevel); // Lo usaremos para construir la cadena JSON
        stringBuilder.append("{").append(NEW_LINE); // La llave de apertura del JSON
        for (Field f : obj.getClass().getFields()) { // Recorremos todos los campos del objeto
            try {
                stringBuilder.append("  ").append(f.getName()).append(": \"").append(f.get(obj).toString()).append("\"")
                        .append(NEW_LINE);
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
    public static String buildFromObjectGetters(Object obj, int depthLevel) {

        if (obj.getClass().isPrimitive()) { // Condición de parada
            return String.valueOf(obj);
        }
        if (obj.getClass().equals(String.class)) { // Condición de parada
            return ((String) obj);
        }

        OutputBuilder outputBuilder = new OutputBuilder(depthLevel);

        for (Method m : obj.getClass().getMethods()) { // Por cada método del objeto...
            if (!m.getName().toLowerCase().contains("class") && m.getName().toLowerCase().startsWith("get")
                    && (m.getParameterCount() == 0)) { // El método tiene que empezar por get. Aparte, el método no
                                                       // puede requerir argumentos (por ejemplo, si la clase tiene una
                                                       // lista, que el getter pida el índice de la lista para extraer,
                                                       // ya que en ese caso lo que querríamos sería obtener la lista
                                                       // entera). La excepción es el método getClass
                outputBuilder.addVariableUsingGetter(obj, m);
            }
        }

        return outputBuilder.build();
    }

    /**
     * Función recursiva privada, que acepta un nivel de sangrado
     * 
     * @param obj
     * @param cantidadDeSangrado
     * @return
     */
    private static String buildFromObjectGetters(Object obj, int depthLevel, int cantidadDeSangrado) {

        StringBuilder stringBuilder = new StringBuilder(); // Lo usaremos para construir la cadena JSON
        stringBuilder.append("{").append(NEW_LINE); // La llave de apertura del JSON
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

                    stringBuilder.append(new String(new char[cantidadDeSangrado]).replace('\0', ' '))
                            .append(nombreObjeto).append(": "); // Añadimos el nombre del objeto al
                    // JSON

                    String objeto;
                    if (Iterable.class.isAssignableFrom(m.getReturnType())) {
                        Object objetoIterable = m.invoke(obj);
                        if (objetoIterable instanceof Iterable<?>) {
                            // Esta es una comprobación totalmente redundante, solo es para que no sale un warning del compilador, porque no detecta que ya comprobamos que el objeto es iterable más arriba

                            // El tipo de objeto devuelto es iterable, por lo que vamos a tratarlo como una lista
                            stringBuilder.append("[ ");
                            int cantidadDeSangradoLocal = stringBuilder.toString()
                                    .split(NEW_LINE)[stringBuilder.toString().split(NEW_LINE).length - 1].length();
                            // Vamos a mirar dónde empezaba la última línea para saber con cuántos espacios
                            // tenemos que hacer el sangrado
                            Iterator<Object> iterator = ((Iterable) objetoIterable).iterator();
                            while (iterator.hasNext()) {
                                stringBuilder.append("\"").append(
                                        OutputBuilder.buildFromObjectGetters(iterator.next(), cantidadDeSangradoLocal))
                                        .append("\"");
                                if (iterator.hasNext()) { // Si este no es el último elemento, preparamos la siguiente línea
                                    stringBuilder.append(",").append(NEW_LINE); // Terminamos la
                                                                                // línea
                                    stringBuilder.append(new String(new char[cantidadDeSangradoLocal]).replace('\0', ' '));
                                    // Añadimos una nueva línea toda de espacios
                                }
                            }
                            stringBuilder.append(NEW_LINE); // Nueva línea
                            stringBuilder.append(new String(new char[cantidadDeSangradoLocal - 2]).replace('\0', ' '));
                            // 2 caracteres de sangrado menos
                            stringBuilder.append("]");
                        }
                    } else {
                        try {
                            objeto = m.invoke(obj).toString();
                        } catch (NullPointerException e) {
                            // El método nos ha devuelto un NULL, así que añadimos eso al JSON
                            objeto = "NULL";
                        }
                        stringBuilder.append("\"").append(objeto).append("\"");
                    }
                    stringBuilder.append(",").append(NEW_LINE); // Añadimos una nueva línea
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    Logger.getLogger(OutputBuilder.class.getName()).log(Level.WARNING,
                            "Excepción al obtener el valor de {0}", m.getName());
                }
            }
        }
        stringBuilder.append("}"); // Cerramos el JSON
        stringBuilder.append(NEW_LINE);
        return stringBuilder.toString();
    }

    private List<String> variables; // Cada variable es un elemento de esta lista. Es una lista porque nos importa
                                    // el orden de adición (no es esencial, pero de esta forma es más predecible)

    /**
     * Si queremos construir el objeto JSON manualmente, usamos un OutputBuilder
     */
    public OutputBuilder() {
        variables = new ArrayList<String>();
        DEPTH_LEVEL = 0; // Por defecto, no se desencapsula nada
        separateLines = false;
    }

    /**
     * Un OutputBuilder que buscará hasta la profundidad indicada
     * 
     * @param depthLevel
     */
    private OutputBuilder(int depthLevel) {
        variables = new ArrayList<String>();
        DEPTH_LEVEL = depthLevel;
        separateLines = false;
    }

    /**
     * Adds a variable from an object using an unparametrized getter method
     */
    private OutputBuilder addVariableUsingGetter(Object obj, Method m) {
        int sangrado;
        String nombreObjeto;
        Object valorObjeto;

        // Convertimos el nombre del objeto al formato adecuado, lo extraemos del nombre
        // del getter
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

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(nombreObjeto);
        stringBuilder.append(": ");

        sangrado = stringBuilder.toString().length();

        try {
            valorObjeto = m.invoke(obj);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            valorObjeto = null;
        }

        if (valorObjeto == null) { // Si el objeto devuelto es null, lo convertimos en una cadena de texto que lo
                                   // represente
            valorObjeto = "NULL";
        }

        if (Iterable.class.isAssignableFrom(m.getReturnType())) {
            String lista = getListFromIterable((Iterable<Object>) valorObjeto);
            lista = anadirSangrado(lista, sangrado + 2); // Sangrado + 2, porque en la primera línea de la lista estamos
                                                         // metiendo ": ", que son 2 caracteres más
            stringBuilder.append(lista);
        } else { // La variable no es iterable
            if (valorObjeto.getClass().equals(String.class)) { // Lleva comillas de apertura solo si es un string
                stringBuilder.append("\"");
            }
            if (DEPTH_LEVEL > 0) { // Si DEPTH_LEVEL es > 0, seguimos desencapsulando
                stringBuilder.append(
                        anadirSangrado(OutputBuilder.buildFromObjectGetters(valorObjeto, DEPTH_LEVEL - 1), sangrado)); // Sangrado
                                                                                                                       // +
                                                                                                                       // 2,
                                                                                                                       // porque
                                                                                                                       // en
                                                                                                                       // la
                                                                                                                       // primera
                                                                                                                       // línea
                                                                                                                       // de
                                                                                                                       // la
                                                                                                                       // lista
                                                                                                                       // estamos
                                                                                                                       // metiendo
                                                                                                                       // ":
                                                                                                                       // ",
                                                                                                                       // que
                                                                                                                       // son
                                                                                                                       // 2
                                                                                                                       // caracteres
                                                                                                                       // más
            } else { // Si DEPTH_LEVEL NO es > 0, así que usamos toString()
                stringBuilder.append(valorObjeto.toString());
            }
            if (valorObjeto.getClass().equals(String.class)) { // Lleva comillas de cierre solo si es un string
                stringBuilder.append("\"");
            }

        }

        variables.add(stringBuilder.toString()); // Añadimos esta variable

        return this;
    }

    /**
     * Añade un sangrado determinado en cada línea nueva
     * 
     * @param stringOriginal
     * @param cantidadDeSangrado
     * @return
     */
    private String anadirSangrado(String stringOriginal, int cantidadDeSangrado) {
        return stringOriginal.replace(NEW_LINE, NEW_LINE + new String(new char[cantidadDeSangrado]).replace('\0', ' '));
    }

    /**
     * Añade una variable que es Iterable
     * 
     * @return
     */
    private String getListFromIterable(Iterable<Object> iterable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ ");
        Iterator<Object> iterator = iterable.iterator();
        while (iterator.hasNext()) {

            Object currentObject = iterator.next();
            if (currentObject.getClass().equals(String.class)) { // Lleva comillas de cierre solo si es un string
                stringBuilder.append("\"");
            }

            if (DEPTH_LEVEL > 0) { // Si DEPTH_LEVEL es > 0, seguimos desencapsulando
                stringBuilder.append(OutputBuilder.buildFromObjectGetters(currentObject, DEPTH_LEVEL - 1));
            } else { // Si DEPTH_LEVEL NO es > 0, así que usamos toString()
                stringBuilder.append(currentObject.toString());
            }

            if (currentObject.getClass().equals(String.class)) { // Lleva comillas de cierre solo si es un string
                stringBuilder.append("\"");
            }

            if (iterator.hasNext()) { // Si este no es el último elemento, preparamos la siguiente línea
                if (separateLines) {
                    stringBuilder.append(",").append(NEW_LINE); // Carácter de coma, y de espacio
                    stringBuilder.append(new String(new char[2]).replace('\0', ' ')); // Añadimos los espacios del
                                                                                      // principio
                    // de la línea siguiente
                } else {
                    stringBuilder.append(",").append(" "); // Carácter de coma, y de espacio
                }
            }
        }

        stringBuilder.append(" "); // Nueva línea
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    /**
     * Añade manualmente un parámetro
     * 
     * @param key
     * @param valor
     * @return
     */
    public OutputBuilder manualAddString(String key, String valor) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(key);
        stringBuilder.append(": \"");
        stringBuilder.append(valor);
        stringBuilder.append("\"");

        variables.add(stringBuilder.toString());
        return this;
    }

    /**
     * Añade automáticamente un parámetro, detectando su tipo y tratándolo
     * adecuadamente
     * 
     * @param key
     * @param obj
     * @return
     */
    public OutputBuilder autoAdd(String key, Object obj) {
        if (obj.getClass().equals(String.class)) { // Si el objeto es un String, lo adjuntamos tal cual
            return manualAddString(key, (String) obj);
        }

        // El objeto no es un String

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(key);
        stringBuilder.append(": ");

        int sangrado = key.length() + 2; // +2 porque estamos poniendo también ": "

        if (Iterable.class.isAssignableFrom(obj.getClass())) { // Si el objeto es iterable, lo tratamos como una
                                                               // lista...
            String lista = getListFromIterable((Iterable<Object>) obj);

            lista = anadirSangrado(lista, sangrado);
            stringBuilder.append(lista);
        } else if (obj.getClass().isPrimitive()) { // El objeto es un primitivo
            stringBuilder.append(String.valueOf(obj));
        } else { // Genéricamente, como no sabemos el tipo del objeto, usamos toString()
            stringBuilder.append(obj.toString());
        }

        variables.add(stringBuilder.toString());
        return this;
    }

    /**
     * Begins the build of an Output
     * 
     * @return
     */
    public static OutputBuilder beginBuild() {
        return new OutputBuilder();
    }

    /**
     * Construye la salida en formato JSON
     * 
     * @return
     */
    public String build() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{").append(NEW_LINE);

        Iterator<String> iterator = variables.iterator(); // Recorremos toda la lista de variables y las vamos añadiendo
                                                          // al String final

        while (iterator.hasNext()) {

            stringBuilder.append(new String(new char[nivelSangrado]).replace('\0', ' '));
            stringBuilder.append(anadirSangrado(iterator.next(), nivelSangrado)); // Añadimos el valor asociado, pero
                                                                                  // sangrando todas las líneas a partir
                                                                                  // de la primera (si son varias
                                                                                  // líneas, el sangrado no aparece
                                                                                  // automáticamente)

            if (iterator.hasNext()) {
                stringBuilder.append(","); // Ponemos la coma si hay más elementos
            }

            stringBuilder.append(NEW_LINE);
        }

        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return this.build();
    }

}
