package risk;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class OutputBuilder {

    private Map<String, String> parametros;

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
