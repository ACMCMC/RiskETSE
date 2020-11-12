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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

public class Mapa {

    private static final File FILE_COLORES_CONTINENTES = new File("coloresContinentes.csv");
    private static final String NEW_LINE = System.getProperty("line.separator");

    private static final Mapa mapaSingleton = new Mapa(); // A Singleton for the Mapa
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


    //TODO:_ cambiar
    /**
     * 
     * 
     * @throws FileNotFoundException
     * @throws RiskException
     */
    public static void crearMapa(File file) throws FileNotFoundException {
        if (isMapaCreado == true) { // Si el mapa ya está creado, lanzamos una excepción para el error
            FileOutputHelper.printToErrOutput(new RiskException(RiskException.RiskExceptionEnum.MAPA_YA_CREADO).toString());
            return;
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
     * Devuelve un Set de los Paises del Mapa
     * @return
     */
    public Set<Pais> getPaises() {
        return (this.paises.entrySet().parallelStream().map((entrada) -> {
            return (entrada.getValue());
        }).collect(Collectors.toSet()));
    }

    /**
     * Devuelve el Set de las Fronteras de Paises de este continente con Paises de otros continentes
     * @param c
     * @return
     */
    public Set<Frontera> getFronterasIntercontinentales(Continente c) {
        return (this.fronteras.parallelStream()
        .filter(frontera -> frontera.getPaises().stream().anyMatch(pais -> pais.getContinente().equals(c)))
        .filter(frontera -> !frontera.getPaises().stream().allMatch(pais -> pais.getContinente().equals(c))).collect(Collectors.toSet()));
    }

    /**
     * Devuelve el número de Paises de otros Continentes que tienen alguna Frontera con el continente {@code c}. Si varios Paises de este Continente tocan el mismo Pais de otro Continente, solo se cuenta una vez.
     * @param c
     * @return
     */
    public int getNumFronterasIntercontinentales(Continente c) {
        return getFronterasIntercontinentales(c).stream().collect(Collectors.groupingBy(frontera -> frontera.getPaises().stream().filter(pais -> !pais.getContinente().equals(c)).findFirst().get())).size();
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

        List<Casilla> ruta = buscarRuta(casillaInicio, casillaFin);

        procesarRuta(ruta);

        this.addFrontera(paisA, paisB);
    }

    private void procesarRuta(List<Casilla> ruta) {
        for (int i = 0; i < (ruta.size() - 1); i++) {
            Coordenadas coordsActuales = ruta.get(i).getCoordenadas();
            Coordenadas coordsSiguientes = ruta.get(i+1).getCoordenadas();
            int deltaX = coordsSiguientes.getX() - coordsActuales.getX();
            int deltaY = coordsSiguientes.getY() - coordsActuales.getY();

            if (deltaX < 0) { // Como el mapa es un toro (si llegamos a la derecha de todo volvemos a la izquierda, podría darse el caso de que la siguiente casilla esté mas a la izquierda y por eso deltaX sea negativo. En ese caso, lo que hacemos es sumarle el tamaño en X del mapa)
                deltaX = deltaX + getSizeX();
            }
            
            if (deltaX == 0) {

                if (!getCasilla(coordsActuales).getBorde().equals(Casilla.BordeCasilla.VERTICAL_LEFT)) {
                    getCasilla(coordsActuales).setBorde(Casilla.BordeCasilla.VERTICAL);
                }

            } else if (deltaX == 1) { // No debería darse el caso de que deltaX < 0, porque en la ruta siempre nos movemos hacia la derecha
                if (deltaY < 0) { // La siguiente casilla está arriba a la derecha
                    if (!getCasilla(coordsActuales).getBorde().equals(Casilla.BordeCasilla.LEFT_BOTTOM_HORIZONTAL) && !getCasilla(coordsActuales).getBorde().equals(Casilla.BordeCasilla.LEFT_TOP_HORIZONTAL) ) {
                        getCasilla(coordsActuales).setBorde(Casilla.BordeCasilla.HORIZONTAL);
                    }
                    getCasilla(new Coordenadas((coordsActuales.getX()+1) % getSizeX(), coordsActuales.getY())).setBorde(Casilla.BordeCasilla.LEFT_TOP);
                    getCasilla(new Coordenadas((coordsActuales.getX()+1) % getSizeX(), (coordsActuales.getY() - 1) % getSizeY())).setBorde(Casilla.BordeCasilla.LEFT_BOTTOM_HORIZONTAL);
                } else if (deltaY == 0) { // La siguiente casilla está justo a la derecha
                    if ( !getCasilla(coordsActuales).getBorde().equals(Casilla.BordeCasilla.LEFT_BOTTOM_HORIZONTAL) && !getCasilla(coordsActuales).getBorde().equals(Casilla.BordeCasilla.LEFT_TOP_HORIZONTAL)) {
                        getCasilla(coordsActuales).setBorde(Casilla.BordeCasilla.HORIZONTAL);
                    }
                } else { // La siguiente casilla está abajo a la derecha
                    if (!getCasilla(coordsActuales).getBorde().equals(Casilla.BordeCasilla.LEFT_BOTTOM_HORIZONTAL) && !getCasilla(coordsActuales).getBorde().equals(Casilla.BordeCasilla.LEFT_TOP_HORIZONTAL) ) {
                        getCasilla(coordsActuales).setBorde(Casilla.BordeCasilla.HORIZONTAL);
                    }
                    getCasilla(new Coordenadas((coordsActuales.getX()+1) % getSizeX(), coordsActuales.getY())).setBorde(Casilla.BordeCasilla.LEFT_BOTTOM);
                    getCasilla(new Coordenadas((coordsActuales.getX()+1) % getSizeX(), (coordsActuales.getY()+1) % getSizeY())).setBorde(Casilla.BordeCasilla.LEFT_TOP_HORIZONTAL);
                }
            }
        }
    }
    
    /**
     * Busca la mejor ruta entre dos casillas. Traza una línea imaginaria, y luego la convierte en coordenadas. El mapa se trata como un toro (empieza donde acaba)
     */
    private List<Casilla> buscarRuta(Casilla inicio, Casilla fin) {
        List<Casilla> ruta; // La lista de las Casillas que componen nuestra ruta
    
        int difX = fin.getCoordenadas().getX() - inicio.getCoordenadas().getX(); // La diferencia de altura entre las casillas
        int difY = fin.getCoordenadas().getY() - inicio.getCoordenadas().getY(); // La diferencia de anchura entre las casillas
        
        if (difX < 0) { // Si la casilla de fin está a la izquierda de la de inicio, llamamos a esta misma función pero con los parámetros intercambiados (para buscar la ruta de izquierda a derecha). Eso sí, solo lo hacemos si la distancia en X entre las casillas no es más que la mitad del mapa (si es más que la mitad del mapa, es mejor que se le de la vuelta)
            if (!(Math.abs(difX) < (getSizeX()/2))) { // No intercambiamos sino que damos la vuelta al mapa
                difX = difX + getSizeX();
            } else { // Vamos a intercambiar los parámetros de búsqueda, porque la distancia entre las dos casillas es menor que la mitad del mapa
                ruta = buscarRuta(fin, inicio);
                Collections.reverse(ruta); // Le damos la vuelta a la ruta, porque la hemos buscado al revés
                return ruta;
            }
        }
    
        ruta = new ArrayList<>(); // Creamos una lista vacía para guardar la ruta

        /**
         * Vamos a dibujar una línea entre el borde izquierdo de la casilla de inicio y el derecho de la del final (es decir, difX + 1).  La altura será desde el borde superior o inferior del inicio, hasta el superior o inferior del final (difY +1). Después, calcularemos por dónde corta la línea a cada casilla.
         */

        double difAbsY = Math.abs(difY);

        double tangenteAbs = ((double) (difAbsY+1))/((double)(difX+1)); // El valor absoluto de la tangente

        for (int x = 0; x <= difX; x++ ) {
            // Calculamos el punto de corte en la izquierda y en la derecha de la casilla, para saber cómo corta la recta
            double yIzquierda = x * tangenteAbs; // El valor de Y en la recta, en el borde izquierdo de la casilla
            double yDerecha = (x+1) * tangenteAbs; // El valor de Y en la recta, en el borde derecho de la casilla

            double deltaY = yDerecha - yIzquierda;

            int positivo; // Si tenemos que ir hacia arriba, positivo será -1 (ya que cuanto más arriba, Y vale menos)
            if (difY < 0) {
                positivo = -1;
            } else {
                positivo = 1;
            }

            for (int i = 0; i < Math.ceil(deltaY); i++) { // Si en una unidad de la línea en X subimos más de una unidad en Y, añadimos todas esas casillas
                Coordenadas coordenadasCasilla = new Coordenadas((inicio.getCoordenadas().getX() + x) % getSizeX(), inicio.getCoordenadas().getY() + (positivo) * (((int)(yIzquierda)) + i)); // X es módulo el tamaño en X del mapa, porque puede que le demos la vuelta (acabar en la derecha y seguir en la izquierda).
                
                ruta.add(Mapa.getMapa().getCasilla(coordenadasCasilla));
            }

        }

        return ruta;
    }

    /**
     * Ejecuta un algoritmo sencillo para buscar la ruta más corta entre dos Casillas. SIEMPRE nos movemos hacia la derecha al buscar.
     * @param inicio
     * @param fin
     * @return La lista de las casillas por las que pasa la ruta
     */
    private List<Casilla> buscarRutaIterativo(Casilla inicio, Casilla fin) {
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
     * Devuelve el Pais con el código especificado
     * @param codigo
     * @return
     */
    public Pais getPais(String codigo) {
        return this.paises.get(codigo);
    }

    /**
     * Devuelve un Set de todos los Continentes del mapa
     */
    public Set<Continente> getContinentes() {
        return this.continentes.entrySet().parallelStream().map(entry -> entry.getValue()).collect(Collectors.toSet());
    }

    /**
     * Imprime el Mapa por pantalla
     */
    public void imprimirMapa() {
        System.out.print(toString());
    }

    /**
     * Devuelve una representación del Mapa como String
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        // Hacer dos fors de casillas recorriendo todo el mapa, el for de dentro es el ancho y el for de fuera es el alto
        for (int y = 0; y < getSizeY(); y++) {
            for (int x = 0; x < getSizeX(); x++) {
                Casilla casilla = this.getCasilla(new Coordenadas(x,y));
                if (casilla.getBorde().equals(Casilla.BordeCasilla.LEFT_TOP)) {
                    stringBuilder.append(Color.ROJO.getSecTexto());
                    stringBuilder.append("|");
                    stringBuilder.append(Color.getSecColorReset());
                } else {
                    stringBuilder.append("|");
                }
                stringBuilder.append("===========");
            }
            stringBuilder.append("|");
            stringBuilder.append(NEW_LINE);

            for (int x = 0; x < getSizeX(); x++) {
                Casilla casilla = this.getCasilla(new Coordenadas(x,y));
                if (casilla.getBorde().equals(Casilla.BordeCasilla.LEFT_TOP) || casilla.getBorde().equals(Casilla.BordeCasilla.LEFT_TOP_HORIZONTAL) || casilla.getBorde().equals(Casilla.BordeCasilla.LEFT_BOTTOM_HORIZONTAL)) {
                    stringBuilder.append(Color.ROJO.getSecTexto());
                    stringBuilder.append("|");
                    stringBuilder.append(Color.getSecColorReset());
                } else {
                    stringBuilder.append("|");
                }
                if (casilla.esMaritima()) { // No podemos imprimir el nombre del país, porque la casilla es marítima
                    if (casilla.getBorde().equals(Casilla.BordeCasilla.HORIZONTAL) || casilla.getBorde().equals(Casilla.BordeCasilla.LEFT_BOTTOM_HORIZONTAL) || casilla.getBorde().equals(Casilla.BordeCasilla.LEFT_TOP_HORIZONTAL)) {
                        stringBuilder.append(Color.ROJO.getSecTexto());
                        stringBuilder.append(new String(new char[11]).replace("\0", "-")); // Imprimimos espacios
                        stringBuilder.append(Color.getSecColorReset());
                    } else if (casilla.getBorde().equals(Casilla.BordeCasilla.VERTICAL)) {
                        stringBuilder.append(new String(new char[5]).replace("\0", " "));
                        stringBuilder.append(Color.ROJO.getSecTexto());
                        stringBuilder.append("|");
                        stringBuilder.append(Color.getSecColorReset());
                        stringBuilder.append(new String(new char[5]).replace("\0", " "));
                    } else {
                        stringBuilder.append(new String(new char[11]).replace("\0", " ")); // Imprimimos espacios
                    }
                } else {
                    stringBuilder.append(" "); // Imprimimos un espacio al final para separar
                    stringBuilder.append(casilla.getPais().getContinente().getColor().getSecFondo());
                    stringBuilder.append(String.format("%-9s", casilla.getPais().getCodigo()));
                    stringBuilder.append(Color.getSecColorReset());
                    stringBuilder.append(" "); // Imprimimos un espacio al final para separar
                }
            }
            stringBuilder.append("|");
            stringBuilder.append(NEW_LINE);

            for (int x = 0; x < getSizeX(); x++) { // Imprimimos los ejércitos de cada jugador
                Casilla casilla = this.getCasilla(new Coordenadas(x,y));
                if (casilla.getBorde().equals(Casilla.BordeCasilla.LEFT_BOTTOM) || casilla.getBorde().equals(Casilla.BordeCasilla.LEFT_BOTTOM_HORIZONTAL)) {
                    stringBuilder.append(Color.ROJO.getSecTexto());
                    stringBuilder.append("|");
                    stringBuilder.append(Color.getSecColorReset());
                } else {
                    stringBuilder.append("|");
                }
                if (casilla.esMaritima()) { // No podemos imprimir el número de ejércitos, porque la casilla es marítima, o porque no tiene asignado un jugador
                    if (casilla.getBorde().equals(Casilla.BordeCasilla.VERTICAL)) {
                        stringBuilder.append(new String(new char[5]).replace("\0", " "));
                        stringBuilder.append(Color.ROJO.getSecTexto());
                        stringBuilder.append("|");
                        stringBuilder.append(Color.getSecColorReset());
                        stringBuilder.append(new String(new char[5]).replace("\0", " "));
                    } else {
                        stringBuilder.append(new String(new char[11]).replace("\0", " "));
                    }
                } else if (!casilla.getPais().getJugador().isPresent()) { // No podemos imprimir el número de ejércitos, porque no tiene asignado un jugador
                    stringBuilder.append(new String(new char[11]).replace("\0", " "));
                } else {
                    stringBuilder.append(" ");
                    stringBuilder.append(casilla.getPais().getJugador().get().getColor().getSecTexto());
                    stringBuilder.append(String.format("%-9s", casilla.getPais().getNumEjercitos()));
                    stringBuilder.append(Color.getSecColorReset());
                    stringBuilder.append(" "); // Imprimimos un espacio al final para separar
                }
                
            }
            stringBuilder.append("|");
            stringBuilder.append(NEW_LINE);
        }
        stringBuilder.append(new String(new char[getSizeX()]).replace("\0", "|===========") + "|");
        stringBuilder.append(NEW_LINE);
        return stringBuilder.toString();
    }
}
