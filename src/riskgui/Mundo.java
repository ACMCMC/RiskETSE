package riskgui;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import javafx.scene.shape.SVGPath;

public class Mundo {

    private Properties props;
    Map<String, SVGPath> paths;

    public Mundo() {
        cargarMundo();
        procesarPaths();
    }

    public Map<String, SVGPath> getPaths() {
        return this.paths;
    }
    
    private void procesarPaths() {
        paths = new HashMap<>();
        for (Entry<Object, Object> entrada : props.entrySet()) {
            SVGPath svgpath = new SVGPath();
            svgpath.setContent(entrada.getValue().toString());
            paths.put(entrada.getKey().toString(), svgpath);
        }
    }

    private void cargarMundo() {
        props = new Properties();
        try (InputStream streamProps = Thread.currentThread().getContextClassLoader().getResourceAsStream("riskgui/paises.properties")) {
            props.load(streamProps);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
