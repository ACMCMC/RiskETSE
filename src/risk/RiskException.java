/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

public class RiskException {

    final int codigo;
    final String codigoTexto;

    RiskException(int codigo, String codigoTexto) {
        this.codigo = codigo;
        this.codigoTexto = codigoTexto;
    }

    RiskException(RiskExceptionEnum ex) {
        this.codigo = ex.getCodigo();
        this.codigoTexto = ex.getCodigoTexto();
    }

    private int getCodigo() {
        return this.codigo;
    }

    private String getCodigoTexto() {
        return this.codigoTexto;
    }

    @Override
    public String toString() {
        return OutputBuilder.beginBuild().autoAdd("código de error", String.valueOf(this.getCodigo()))
                .autoAdd("descripción", this.getCodigoTexto()).build();
    }

    public String getMessage() {
        return OutputBuilder.beginBuild().autoAdd("código de error", this.getCodigo())
                .autoAdd("descripción", this.getCodigoTexto()).build();
    }

    public static enum RiskExceptionEnum {
        COMANDO_NO_PERMITIDO(99, "Comando no permitido en este momento"), COLOR_NO_PERMITIDO(100, "Color no permitido"),
        COMANDO_INCORRECTO(101, "Comando incorrecto"), CONTINENTE_NO_EXISTE(102, "El continente no existe"),
        JUGADOR_NO_EXISTE(103, "El jugador no existe"), JUGADOR_YA_EXISTE(104, "El jugador ya existe"),
        JUGADORES_NO_CREADOS(105, "Los jugadores no están creados"), MAPA_NO_CREADO(106, "El mapa no está creado"),
        MAPA_YA_CREADO(107, "El mapa ya ha sido creado"), PAIS_NO_EXISTE(109, "El país no existe"),
        PAIS_NO_PERTENECE_JUGADOR(110, "El país no pertenece al jugador"),
        PAIS_PERTENECE_JUGADOR(111, "El país pertenece al jugador"),
        PAISES_NO_SON_FRONTERA(112, "Los países no son frontera"), PAIS_YA_ASIGNADO(113, "El país ya está asignado"),
        COLOR_YA_ASIGNADO(114, "El color ya está asignado"), MISION_YA_ASIGNADA(115, "La misión ya está asignada"),
        MISION_NO_EXISTE(116, "La misión no existe"),
        JUGADOR_YA_TIENE_MISION(117, "El jugador ya tiene asignada una misión"),
        MISIONES_NO_ASIGNADAS(118, "Las misiones no están asignadas"),
        EJERCITOS_NO_DISPONIBLES(119, "Ejércitos no disponibles"),
        NO_HAY_CARTAS_SUFICIENTES(120, "No hay cartas suficientes"),
        NO_HAY_CONFIG_CAMBIO(121, "No hay configuración de cambio"),
        CARTAS_NO_PERTENECEN_JUGADOR(122, "Algunas cartas no pertenecen al jugador"),
        CARTAS_NO_EXISTEN(123, "Algunas cartas no existen"),
        NO_HAY_EJERCITOS_SUFICIENTES(124, "No hay ejércitos suficientes"),
        IDENTIFICADOR_INCORRECTO(125, "El identificador no sigue el formato correcto"),
        CARTA_YA_ASIGNADA(126, "Carta de equipamiento ya asignada");

        final int codigo;
        final String codigoTexto;

        private RiskExceptionEnum(int codigo, String codigoTexto) {
            this.codigo = codigo;
            this.codigoTexto = codigoTexto;
        }

        private int getCodigo() {
            return this.codigo;
        }

        private String getCodigoTexto() {
            return this.codigoTexto;
        }
    }
}
