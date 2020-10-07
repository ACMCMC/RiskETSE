package risk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Mapa {

    private Map<Coordenadas, Casilla> casillas;

    Mapa(File archivoRelacionPaises) throws FileNotFoundException {
        // llenamos el Mapa de casillas
        String linea;
        String[] valores;
        BufferedReader bufferedReader;

        casillas = new HashMap<Coordenadas, Casilla>();

        try {
            FileReader reader = new FileReader(archivoRelacionPaises);
            bufferedReader = new BufferedReader(reader);
            while ((linea = bufferedReader.readLine()) != null) {
                valores = linea.split(";");
                Casilla casillaInsertar = new Casilla(new Coordenadas(Integer.valueOf(valores[1]), Integer.valueOf(valores[2])), new Pais(valores[0]));
                casillas.put(casillaInsertar.getCoordenadas(), casillaInsertar);
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            throw ex; // Lanzamos de vuelta la excepción
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<Entry<Coordenadas, Casilla>> iterator = casillas.entrySet().iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next().toString());
        }

        System.out.println(casillas.get(new Coordenadas(0,1)));
    }

    public void imprimirMapa() {
        // Hacer dos fors de casillas recorriendo todo el mapa, el for de dentro es el ancho y el for de fuera es el alto
        
    }
}
