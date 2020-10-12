/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

public class Risk {
    /**
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        new Menu();

        try {
            FileOutputHelper.printToOutput(OutputBuilder
                    .buildFromObjectGetters(Mapa.getMapa().getFronteras(Mapa.getMapa().getPais("Alberta")), 2));
        } catch (RiskException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
