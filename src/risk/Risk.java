/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.util.Set;

public class Risk {
    /**
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        new Menu();
        Continente continentePrueba = new Continente("Prueba", Color.CYAN);
        continentePrueba.addPais(new Pais("1", continentePrueba));
        continentePrueba.addPais(new Pais("2", continentePrueba));
        continentePrueba.addPais(new Pais("3", continentePrueba));
        continentePrueba.addPais(new Pais("4", continentePrueba));
        FileOutputHelper.printToOutput(OutputBuilder.buildFromObjectGetters(continentePrueba));
    }
}
