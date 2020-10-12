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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Mapa {

    private static Mapa mapaSingleton = new Mapa(); // A Singleton for the Mapa
    private static boolean isMapaCreado = false; // Will be false at first, until the asignarPaises() method gets executed

    private final int SIZE_X = 11;
    private final int SIZE_Y = 8;

    private Map<Coordenadas, Casilla> casillas;
    private Map<String, Pais> paises;
    private Map<String, Continente> continentes;
    private Set<Frontera> fronteras;

    /**
     * Crea un mapa lleno de casillas marítimas
     * @param archivoRelacionPaises
     * @throws FileNotFoundException
     */
    private Mapa() {

        casillas = new HashMap<Coordenadas, Casilla>();
        paises = new HashMap<String, Pais>();
        continentes = new HashMap<String, Continente>();
        fronteras = new HashSet<Frontera>();

        // llenamos el Mapa de casillas, todas marítimas en principio
        for (int y = 0; y < getSizeY(); y++) {
            for (int x = 0; x < getSizeX(); x++) {
                Casilla casillaInsertar = new Casilla(new Coordenadas(x, y));
                casillas.put(casillaInsertar.getCoordenadas(), casillaInsertar);
            }
        }

    }

    /**
     * Devuelve el tamaño horizontal del mapa
     * @return
     */
    public int getSizeX() {
        return this.SIZE_X;
    }

    /**
     * Devuelve el tamaño vertical del mapa
     * @return
     */
    public int getSizeY() {
        return this.SIZE_Y;
    }

    /**
     * Creates the Mapa singleton
     * 
     * @throws FileNotFoundException
     * @throws RiskException
     */
    public static void crearMapa(File file) throws FileNotFoundException, RiskException {
        if (isMapaCreado == true) { // Si el mapa ya está creado, lanzamos una excepción para el error
            throw new RiskException(RiskException.RiskExceptionEnum.MAPA_YA_CREADO);
        }

        mapaSingleton.asignarPaisesACasillas(file); // Could throw a FileNotFoundException, but we leave exception handling to the caller

        mapaSingleton.anadirFronterasDirectas();

        isMapaCreado = true;
    }

    /**
     * Returns the Mapa singleton
     * @return
     * @throws RiskException
     */
    public static Mapa getMapa() throws RiskException {
        if (isMapaCreado == false) {
            throw new RiskException(RiskException.RiskExceptionEnum.MAPA_NO_CREADO);
        }
        return mapaSingleton;
    }

    /**
     * Reemplaza las casillas del mapa por las casillas con el país que se indique en el archivo, por cada una de las entradas del archivo. El archivo tiene que tener formato [nombreHumanoPais];[codigoPais];[nombreHumanoContinente];[codigoContinente];[X];[Y]
     * @param archivoPaises
     * @throws FileNotFoundException
     */
    private void asignarPaisesACasillas(File archivoRelacionPaises) throws FileNotFoundException {

        String linea;
        String[] valores;
        BufferedReader bufferedReader;

        try { // Reemplazamos las casillas del mapa por las que nos pone el archivo
            bufferedReader = new BufferedReader(new FileReader(archivoRelacionPaises));
            while ((linea = bufferedReader.readLine()) != null) {
                valores = linea.split(";");

                String nombreHumanoPais, codigoPais, nombreHumanoContinente, codigoContinente, posX, posY;
                nombreHumanoPais = valores[0];
                codigoPais = valores[1];
                nombreHumanoContinente = valores[2];
                codigoContinente = valores[3];
                posX = valores[4];
                posY = valores[5];

                Continente continenteDelPais = getContinente(codigoContinente);
                if (continenteDelPais == null) {
                    // Creamos el continente, porque no está en la lista
                    continenteDelPais = new Continente(codigoContinente, nombreHumanoContinente);
                    continentes.put(continenteDelPais.getCodigo(), continenteDelPais);
                }
                // La casilla que estaba en el mapa antes la vamos a reemplazar por una nueva casilla con país
                Casilla casillaPais = new Casilla(new Coordenadas(Integer.valueOf(posX), Integer.valueOf(posY)), new Pais(codigoPais, nombreHumanoPais, continenteDelPais));
                casillas.replace(casillaPais.getCoordenadas(), casillaPais);
                paises.put(casillaPais.getPais().getCodigo(), casillaPais.getPais()); // Insertamos el país en la lista de países
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            throw ex; // Lanzamos de vuelta la excepción
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recorre el mapa, añadiendo las fronteras entre los países que tienen conexión directa.
     * 
     * Para ser más eficientes, el mapa se recorre de derecha a izquierda, saltando cada dos líneas, así:
     * 
     * |x| |x| |x| | (1er pass)
     * | |x| |x| |x| (1er pass)
     * |x| |x| |x| | (2do pass)
     * | |x| |x| |x| (2do pass)
     * |x| |x| |x| | (3er pass) ...
     */
    private void anadirFronterasDirectas() {
        for (int y = 0; y < (getSizeY()); y += 2) { // Recorremos en vertical saltando una línea, hasta una línea antes del final
            for (int x = 0; x < getSizeX(); x++) {
                int yModificado = y + (x%2); // Si x es impar, y se aumenta una unidad (así conseguimos recorrer el mapa en zigzag)
                if (!getCasilla(new Coordenadas(x, yModificado)).esMaritima()) { // Solo vamos a mirar las fonteras de esta casilla si tiene un país (no es marítima)

                    // Vamos mirando las casillas en contacto directo con esta, y si no son marítimas, las añadimos a la lista de fronteras
                    if (x > 0) { // No miramos la casilla de la izquierda en la que ya está a la izquierda
                        if (!getCasilla(new Coordenadas(x-1, yModificado)).esMaritima()) {
                            addFrontera(getCasilla(new Coordenadas(x, yModificado)).getPais(), getCasilla(new Coordenadas(x-1, yModificado)).getPais());
                        }
                    }
                    if ( (x + 1) < getSizeX()) { // Tampoco miramos la de la de la derecha si esta casilla ya es la de la derecha del todo
                        if (!getCasilla(new Coordenadas(x+1, yModificado)).esMaritima()) {
                            addFrontera(getCasilla(new Coordenadas(x, yModificado)).getPais(), getCasilla(new Coordenadas(x+1, yModificado)).getPais());
                        }
                    }
                    if (yModificado > 0) { // No comprobamos las de más arriba de arriba de todo del mapa
                        if (!getCasilla(new Coordenadas(x, yModificado-1)).esMaritima()) {
                            addFrontera(getCasilla(new Coordenadas(x, yModificado)).getPais(), getCasilla(new Coordenadas(x, yModificado-1)).getPais());
                        }
                    }
                    if ((yModificado + 1) < getSizeY()) {// No comprobamos las de más abajo de abajo de todo del mapa
                        if (!getCasilla(new Coordenadas(x, yModificado+1)).esMaritima()) {
                            addFrontera(getCasilla(new Coordenadas(x, yModificado)).getPais(), getCasilla(new Coordenadas(x, yModificado+1)).getPais());
                        }
                    }
                }
            }
        }
    }

    /**
     * Devuelve la Casilla en las coordenadas especificadas
     * @param coordenadas
     * @return
     */
    public Casilla getCasilla(Coordenadas coordenadas) {
        return this.casillas.get(coordenadas);
    }

    /**
     * Devuelve el Continente con el código especificado
     * @param codigo
     * @return
     */
    public Continente getContinente(String codigo) {
        return this.continentes.get(codigo);
    }

    /**
     * Devuelve la Frontera entre dos países, o {@code null} si no existe
     */
    public Frontera getFrontera(Pais paisA, Pais paisB) {
        return (this.fronteras.contains(new Frontera(paisA, paisB)) ? new Frontera(paisA, paisB) : null);
    }

    /**
     * Devuelve las Fronteras de un Pais
     * @param pais
     * @return
     */
    public Set<Frontera> getFronteras(Pais pais) {
        return (this.fronteras.parallelStream().filter((Frontera f) -> {
            if (f.getPaises().contains(pais)) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toSet()));
    }

    /**
     * Añade una Frontera entre dos países. Si ya existe la Frontera, no hace nada
     * @param paisA
     * @param paisB
     */
    private void addFrontera(Pais paisA, Pais paisB) {
        this.fronteras.add(new Frontera(paisA, paisB));
    }

    /**
     * Devuelve el país con el código especificado
     * @param codigo
     * @return
     */
    public Pais getPais(String codigo) {
        return this.paises.get(codigo);
    }

    public void imprimirMapa() {
        // Hacer dos fors de casillas recorriendo todo el mapa, el for de dentro es el ancho y el for de fuera es el alto
        for (int y = 0; y < getSizeY(); y++) {
            System.out.println(new String(new char[getSizeX()]).replace("\0", "|===========") + "|");
            for (int x = 0; x < getSizeX(); x++) {
                System.out.print("| ");
                Casilla casilla = this.getCasilla(new Coordenadas(x,y));
                if (casilla.esMaritima()) { // No podemos imprimir el nombre del país, porque la casilla es marítima
                    System.out.print(new String(new char[9]).replace("\0", " ")); // Imprimimos espacios
                } else {
                    System.out.print(this.getCasilla(new Coordenadas(x,y)).getPais().getContinente().getColor().getSecFondo());
                    System.out.print(String.format("%-9s", this.getCasilla(new Coordenadas(x,y)).getPais().getCodigo()));
                    System.out.print(Color.getSecColorReset());
                }
                System.out.print(" "); // Imprimimos un espacio al final para separar
            }
            System.out.println("|");
        }
        System.out.println(new String(new char[getSizeX()]).replace("\0", "|===========") + "|");
    }
}
