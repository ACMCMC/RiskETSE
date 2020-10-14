/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

public class Mapa {

    private static final File FILE_COLORES_CONTINENTES = new File("coloresContinentes.csv");

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
    public static void crearMapa(File file) throws FileNotFoundException {
        if (isMapaCreado == true) { // Si el mapa ya está creado, lanzamos una excepción para el error
            //throw new RiskException(RiskException.RiskExceptionEnum.MAPA_YA_CREADO);
        }

        mapaSingleton.asignarPaisesACasillas(file); // Could throw a FileNotFoundException, but we leave exception handling to the caller

        mapaSingleton.anadirFronterasDirectas();

        mapaSingleton.asignarColoresContinentes(FILE_COLORES_CONTINENTES);

        isMapaCreado = true;
    }

    /**
     * Returns the Mapa singleton
     * @return
     * @throws RiskException
     */
    public static Mapa getMapa() {
        return mapaSingleton;
    }

    private void addContinente(Continente continente) {
        this.continentes.put(continente.getCodigo(), continente);
    }

    /**
     * Lee un archivo con los colores de los continentes y les asigna ese color
     * @param archivoColores
     * @throws RiskException
     */
    public void asignarColoresContinentes(File archivoColores) {
        String linea;
        String[] valores;
        BufferedReader bufferedReader;


            try {
            bufferedReader = new BufferedReader(new FileReader(archivoColores));
        
            while ((linea = bufferedReader.readLine()) != null) {
                valores = linea.split(";");
                if (getContinente(valores[0]) != null ) {
                    getContinente(valores[0]).setColor(Color.getColorByString(valores[1]));
                } else {
                    addContinente(new Continente(valores[0], valores[0], Color.getColorByString(valores[1]))); // En el archivo no sale el nombre humano del continente, así que ponemos que el nombre humano sea el del código
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
     * Añade una frontera indirecta al mapa. Busca una ruta entre los dos países, y pinta los bordes de las casillas de acuerdo con la ruta.
     */
    public void anadirFronteraIndirecta(Pais paisA, Pais paisB) {
        Casilla casillaInicio = getCasillaPais(paisA);
        Casilla casillaFin = getCasillaPais(paisB);

        FileOutputHelper.printToOutput(OutputBuilder.beginBuild().autoAdd("casillas", buscarRuta(casillaInicio, casillaFin)).build());
    }

    /**
     * Ejecuta un algoritmo sencillo para buscar la ruta más corta entre dos Casillas
     * @param inicio
     * @param fin
     * @return La lista de las casillas por las que pasa la ruta
     */
    private List<Casilla> buscarRuta(Casilla inicio, Casilla fin) {
        List<Casilla> ruta; // La lista de las Casillas que componen nuestra ruta

        int difX = fin.getCoordenadas().getX() - inicio.getCoordenadas().getX(); // La diferencia de altura entre las casillas
        int difY; // La diferencia de anchura entre las casillas
        
        if (difX < 0) { // Si la casilla de fin está a la izquierda de la de inicio, llamamos a esta misma función pero con los parámetros intercambiados (para buscar la ruta de izquierda a derecha)
            ruta = buscarRuta(fin, inicio);
            Collections.reverse(ruta); // Le damos la vuelta a la ruta, porque la hemos buscado al revés
            return ruta;
        }

        ruta = new ArrayList<>(); // Creamos una lista vacía para guardar la ruta
        ruta.add(inicio); // Salimos de la primera casilla
        
        double discriminante_45grados = (Math.PI / 4); // 45 grados, en readianes
        double direccion;// Calculamos el arcotangente de la tangente (X/Y)
        
        Coordenadas coordenadasActuales = inicio.getCoordenadas(); // Salimos de las coordenadas iniciales
        Coordenadas coordenadasSiguientes; // Las coordenadas de la casilla a la que nos vamos a mover
        
        while (!coordenadasActuales.equals(fin.getCoordenadas()) && (coordenadasActuales.getX() < 20) && (coordenadasActuales.getY() < 20)){ // Repetimos el bucle hasta llegar a las coordenadas finales
            /**
             * Si el arcotangente es mayor de 45 grados, nos dirigiremos en dirección Y; si es menor, nos moveremos en X (ya que la dirección hasta el fin es más plana). Si es igual, nos moveremos simultáneamente en X e Y.
             */
            System.out.println("X: " + coordenadasActuales.getX() + ", Y: " + coordenadasActuales.getY());
            
            difX = fin.getCoordenadas().getX() - coordenadasActuales.getX(); // Volvemos a calcular la diferencia de altura entre las casillas
            difY = fin.getCoordenadas().getY() - coordenadasActuales.getY(); // Volvemos a calcular la diferencia de anchura entre las casillas
            
            direccion = Math.atan(((double)difY)/((double)difX)); // La dirección es el arcotangente de la tangente, es decir, los grados de la línea que une donde estamos con el final
            
            System.out.println(difX + " [difX], " + difY + "[difY]");
            System.out.println(direccion + ", " + discriminante_45grados);
            
            int comparacion = Double.compare(Math.abs(direccion), discriminante_45grados); // Comparamos la dirección con el discriminante. Comparamos el valor absoluto de la dirección solamente, ya que queremos saber si es más o menos de 45 grados.
            
            if (comparacion == 0) { // Si la dirección hacia el final es 45 grados, subimos o bajamos simultáneamente en X e Y
                
                if (direccion > 0) { //Nos movemos hacia arriba
                    coordenadasSiguientes = new Coordenadas(coordenadasActuales.getX() + 1, coordenadasActuales.getY() + 1);
                } else { // Nos movemos hacia abajo
                    coordenadasSiguientes = new Coordenadas(coordenadasActuales.getX() + 1, coordenadasActuales.getY() - 1);
                }
                
            } else if (comparacion > 0) { // La dirección es mayor de 45 grados, nos movemos en Y
                
                if (direccion > 0) { //Nos movemos hacia arriba
                    coordenadasSiguientes = new Coordenadas(coordenadasActuales.getX(), coordenadasActuales.getY() + 1);
                } else { // Nos movemos hacia abajo
                    coordenadasSiguientes = new Coordenadas(coordenadasActuales.getX(), coordenadasActuales.getY() - 1);
                }
                
            } else { // La dirección es menor de 45 grados, nos movemos hacia la derecha en X
                
                coordenadasSiguientes = new Coordenadas(coordenadasActuales.getX() + 1, coordenadasActuales.getY());

            }

            ruta.add(getCasilla(coordenadasSiguientes)); // Añadimos la casilla siguiente a la ruta


            coordenadasActuales = coordenadasSiguientes; // Ahora estamos en las siguientes coordenadas
        }

        return ruta;
    }

    /**
     * Devuelve la Casilla asociada a un Pais
     * @param pais
     * @return
     */
    public Casilla getCasillaPais(Pais pais) {
        try {
            return (this.casillas.entrySet().parallelStream().filter((entrada) -> {
                if (!entrada.getValue().esMaritima()) {
                    return (entrada.getValue().getPais().equals(pais));
                } else { // La casilla es marítima, no puede tener asociado un país
                    return false;
                }
            }).findFirst().get().getValue());
        } catch (NoSuchElementException ex) {
            return null;
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

    /**
     * Imprime el mapa por consola
     */
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
