/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

public class Risk {
    /**
     * @param args argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        IOHelperFactory.setType(IOHelperFile.class);
        new Menu();
    }
}
