/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Mapa {

    private static Mapa mapaSingleton = new Mapa(); // A Singleton for the Mapa
    private static boolean isMapaCreado = false; // Will be false at first, until the asignarPaises() method gets executed

    private Map<Coordenadas, Casilla> casillas;
    private Map<String, Pais> paises;

    /**
     * Crea un mapa lleno de casillas marítimas
     * @param archivoRelacionPaises
     * @throws FileNotFoundException
     */
    private Mapa() {

        casillas = new HashMap<Coordenadas, Casilla>();
        paises = new HashMap<String, Pais>();

        // llenamos el Mapa de casillas, todas marítimas en principio
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 11; x++) {
                Casilla casillaInsertar = new Casilla(new Coordenadas(x, y));
                casillas.put(casillaInsertar.getCoordenadas(), casillaInsertar);
            }
        }

    }

    /**
     * Creates the Mapa singleton
     * 
     * @throws FileNotFoundException
     */
    public static void crearMapa(File file) throws FileNotFoundException, IllegalStateException {
        if (isMapaCreado == true) { // Si el mapa ya está creado, lanzamos una excepción para el error
            throw new IllegalStateException(Error.MAPA_YA_CREADO.getJSON());
        }

        mapaSingleton.asignarPaises(file); // Could throw a FileNotFoundException, but we leave exception handling to the caller

        isMapaCreado = true;
    }

    /**
     * Returns the Mapa singleton
     * @return
     * @throws IllegalStateException
     */
    public static Mapa getMapa() throws IllegalStateException {
        if (isMapaCreado == false) {
            throw new IllegalStateException(Error.MAPA_NO_CREADO.getJSON());
        }
        return mapaSingleton;
    }

    /**
     * Reemplaza las casillas del mapa por las casillas con el país que se indique en el archivo, por cada una de las entradas del archivo. El archivo tiene que tener formato [nombrePais];[X];[Y]
     * @param archivoPaises
     * @throws FileNotFoundException
     */
    private void asignarPaises(File archivoRelacionPaises) throws FileNotFoundException {

        String linea;
        String[] valores;
        BufferedReader bufferedReader;

        Continente continente = new Continente("Asia", Color.VERDE);

        try { // Reemplazamos las casillas del mapa por las que nos pone el archivo
            FileReader reader = new FileReader(archivoRelacionPaises);
            bufferedReader = new BufferedReader(reader);
            while ((linea = bufferedReader.readLine()) != null) {
                valores = linea.split(";");
                Casilla casillaPais = new Casilla(new Coordenadas(Integer.valueOf(valores[1]), Integer.valueOf(valores[2])), new Pais(valores[0], continente));
                casillas.replace(casillaPais.getCoordenadas(), casillaPais);
                paises.put(casillaPais.getPais().getNombre(), casillaPais.getPais()); // Insertamos el país en la lista de países
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

    public Pais getPais(String nombre) {
        return this.paises.get(nombre);
    }

    public void imprimirMapa() {
        // Hacer dos fors de casillas recorriendo todo el mapa, el for de dentro es el ancho y el for de fuera es el alto
        for (int y = 0; y < 8; y++) {
            System.out.println(new String(new char[11]).replace("\0", "|===========") + "|");
            for (int x = 0; x < 11; x++) {
                System.out.print("| ");
                Casilla casilla = this.getCasilla(new Coordenadas(x,y));
                if (casilla.esMaritima()) { // No podemos imprimir el nombre del país, porque la casilla es marítima
                    System.out.print(new String(new char[9]).replace("\0", " ")); // Imprimimos espacios
                } else {
                    System.out.print(this.getCasilla(new Coordenadas(x,y)).getPais().getContinente().getColor().getSecFondo());
                    System.out.print(String.format("%-9s", this.getCasilla(new Coordenadas(x,y)).getPais().getNombre()));
                    System.out.print(Color.getSecColorReset());
                }
                System.out.print(" "); // Imprimimos un espacio al final para separar
            }
            System.out.println("|");
        }
        System.out.println(new String(new char[11]).replace("\0", "|===========") + "|");
    }
}
