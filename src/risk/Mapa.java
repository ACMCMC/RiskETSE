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
        // llenamos el Mapa de casillas, todas marítimas en principio
        String linea;
        String[] valores;
        BufferedReader bufferedReader;

        casillas = new HashMap<Coordenadas, Casilla>();

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 11; x++) {
                Casilla casillaInsertar = new Casilla(new Coordenadas(x, y));
                casillas.put(casillaInsertar.getCoordenadas(), casillaInsertar);
            }
        }

        try { // En el mapa, reemplazamos las casillas marítimas donde haya países por casillas con país
            FileReader reader = new FileReader(archivoRelacionPaises);
            bufferedReader = new BufferedReader(reader);
            while ((linea = bufferedReader.readLine()) != null) {
                valores = linea.split(";");
                Casilla casillaPais = new Casilla(new Coordenadas(Integer.valueOf(valores[1]), Integer.valueOf(valores[2])), new Pais(valores[0]));
                casillas.replace(casillaPais.getCoordenadas(), casillaPais);
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            throw ex; // Lanzamos de vuelta la excepción
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Casilla getCasilla(Coordenadas coordenadas) {
        return this.casillas.get(coordenadas);
    }

    public void imprimirMapa() {
        // Hacer dos fors de casillas recorriendo todo el mapa, el for de dentro es el ancho y el for de fuera es el alto
        for (int y = 0; y < 8; y++) {
            System.out.println(new String(new char[11]).replace("\0", "|===========") + "|");
            for (int x = 0; x < 11; x++) {
                System.out.print("| ");
                Casilla casilla = this.getCasilla(new Coordenadas(x,y));
                if (casilla.esMaritima()) { // No podemos imprimir el nombre del país, porque la casilla es marítima
                    System.out.print(new String(new char[10]).replace("\0", " ")); // Imprimimos espacios
                } else {
                    System.out.print(String.format("%-9s ", this.getCasilla(new Coordenadas(x,y)).getPais().getNombre()));
                }
            }
            System.out.println("|");
        }
        System.out.println(new String(new char[11]).replace("\0", "|===========") + "|");
    }
}
