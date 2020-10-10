package risk;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OutputBuilder {

    private static final int nivelSangrado = 2; // El nivel de sangrado por defecto
    private static final String NEW_LINE = System.getProperty("line.separator");

    /**
     * @deprecated Como en este proyecto no hay campos públicos, no nos va a servir.
     *             Usar en su lugar buildFromObjectGetters Recorre todos los campos
     *             públicos de un objeto, e imprime un JSON con su valor asociado
     * 
     * @param obj
     * @return
     */
    @Deprecated
    public static String buildFromObject(Object obj) {
        StringBuilder stringBuilder = new StringBuilder(); // Lo usaremos para construir la cadena JSON
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
    public static String buildFromObjectGetters(Object obj) {

        if (obj.getClass().isPrimitive()) { // Condición de parada
            return String.valueOf(obj);
        }
        if (obj.getClass().equals(String.class)) { // Condición de parada
            return ((String) obj);
        }

        OutputBuilder outputBuilder = new OutputBuilder();

        for (Method m : obj.getClass().getMethods()) { // Por cada método del objeto...
            if (!m.getName().equals("getClass") && m.getName().toLowerCase().startsWith("get")
                    && (m.getParameterCount() == 0)) { // El método tiene que empezar por get. Aparte, el método no
                                                       // puede requerir argumentos (por ejemplo, si la clase tiene una
                                                       // lista, que el getter pida el índice de la lista para extraer,
                                                       // ya que en ese caso lo que querríamos sería obtener la lista
                                                       // entera). La excepción es el método getClass
                outputBuilder.addVariable(obj, m);
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
    private static String buildFromObjectGetters(Object obj, int cantidadDeSangrado) {

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
                        // El tipo de objeto devuelto es iterable, por lo que vamos a tratarlo como una
                        // lista
                        stringBuilder.append("[ ");
                        int cantidadDeSangradoLocal = stringBuilder.toString()
                                .split(NEW_LINE)[stringBuilder.toString().split(NEW_LINE).length - 1].length(); // Vamos
                                                                                                                // a
                                                                                                                // mirar
                                                                                                                // dónde
                                                                                                                // empezaba
                                                                                                                // la
                                                                                                                // última
                                                                                                                // línea
                                                                                                                // para
                                                                                                                // saber
                                                                                                                // con
                                                                                                                // cuántos
                                                                                                                // espacios
                                                                                                                // tenemos
                                                                                                                // que
                                                                                                                // hacer
                                                                                                                // el
                                                                                                                // sangrado
                        Iterator<Object> iterator = ((Iterable<Object>) m.invoke(obj)).iterator();
                        while (iterator.hasNext()) {
                            stringBuilder.append("\"").append(
                                    OutputBuilder.buildFromObjectGetters(iterator.next(), cantidadDeSangradoLocal))
                                    .append("\"");
                            if (iterator.hasNext()) { // Si este no es el último elemento, preparamos la siguiente línea
                                stringBuilder.append(",").append(NEW_LINE); // Terminamos la
                                                                            // línea
                                stringBuilder.append(new String(new char[cantidadDeSangradoLocal]).replace('\0', ' ')); // Añadimos
                                // una
                                // nueva
                                // línea
                                // toda
                                // de
                                // espacios
                            }
                        }
                        stringBuilder.append(NEW_LINE); // Nueva línea
                        stringBuilder.append(new String(new char[cantidadDeSangradoLocal - 2]).replace('\0', ' ')); // 2
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

    private Set<String> variables; // Cada variable es un elemento de este conjunto

    /**
     * Si queremos construir el objeto JSON manualmente, usamos un OutputBuilder
     */
    public OutputBuilder() {
        variables = new HashSet<String>();
    }

    public OutputBuilder addVariable(Object obj, Method m) {
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
            anadirSangrado(lista, sangrado);
        } else { // La variable no es iterable
            stringBuilder.append("\"");
            System.out.println("OBJ:" + obj);
            stringBuilder.append(OutputBuilder.buildFromObjectGetters(obj));
            stringBuilder.append("\"");
        }

        variables.add(stringBuilder.toString()); // Añadimos esta variable

        return this;
    }

    private void anadirSangrado(String stringOriginal, int cantidadDeSangrado) {
        stringOriginal.replace(NEW_LINE, NEW_LINE + new String(new char[cantidadDeSangrado]).replace('\0', ' '));
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
            stringBuilder.append("\"").append(OutputBuilder.buildFromObjectGetters(iterator.next())).append("\"");
            if (iterator.hasNext()) { // Si este no es el último elemento, preparamos la siguiente línea
                stringBuilder.append(",").append(NEW_LINE); // Terminamos la línea
                stringBuilder.append(new String(new char[2]).replace('\0', ' ')); // Añadimos una nueva línea toda de
                                                                                  // espacios
            }
        }

        stringBuilder.append(NEW_LINE); // Nueva línea
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    /**
     * Añade manualmente un parámetro
     * 
     * @param nombre
     * @param valor
     * @return
     */
    public OutputBuilder addParametro(String nombre, String valor) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(nombre);
        stringBuilder.append(": ");
        stringBuilder.append(valor);

        variables.add(stringBuilder.toString());
        return this;
    }

    /**
     * Construye la salida en formato JSON
     * 
     * @return
     */
    public String build() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{").append(NEW_LINE);

        Iterator<String> iterator = variables.iterator();

        while (iterator.hasNext()) {

            stringBuilder.append(new String(new char[nivelSangrado]).replace('\0', ' '));
            stringBuilder.append(iterator.next());

            if (iterator.hasNext()) {
                stringBuilder.append(",");
            }

            stringBuilder.append(NEW_LINE);
        }

        stringBuilder.append("}").append(NEW_LINE);

        return stringBuilder.toString();
    }

}
