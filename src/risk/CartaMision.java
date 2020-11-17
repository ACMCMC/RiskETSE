package risk;

public class CartaMision {
    private enum EnumCartaMision {
        M1("Conquistar 24 países de la preferencia del jugador"), M2("M2"), M31("Conquistar Asia y América del Sur"),
        M32("Conquistar Asia y África"), M33("Conquistar América del Norte y África"),
        M34("Conquistar América del Norte y Oceanía"), M41("Destruir el ejército AMARILLO"),
        M42("Destruir el ejército AZUL"), M43("Destruir el ejército CYAN"), M44("Destruir el ejército ROJO"),
        M45("Destruir el ejército VERDE"), M46("Destruir el ejército VIOLETA");

        private String descripcion;

        EnumCartaMision(String descripcion) {
            this.descripcion = descripcion;
        }

        String getDescripcion() {
            return this.descripcion;
        }
    }
}
