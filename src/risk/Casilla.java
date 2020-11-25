package risk;

public abstract class Casilla {

  /**
   * Representa los bordes de la casilla. Se usa después para representar el mapa
   */
  enum BordeCasilla {
    TOP,
    VERTICAL,
    VERTICAL_LEFT,
    LEFT_TOP, // Cuando la ruta pasa por la casilla de la izquierda y va a la de arriba a la derecha, pero no pasa por esta
    LEFT_BOTTOM, // Se usa en las casillas de la derecha que no pertenecen a la ruta
    LEFT_BOTTOM_HORIZONTAL,
    LEFT_TOP_HORIZONTAL, // La frontera empieza arriba, y baja por la izquierda y se convierte en horizontal
    HORIZONTAL,
    NONE,
  }

  private BordeCasilla borde; // Parámetro auxiliar para pintar los bordes

  private Coordenadas coordenadas;

  Casilla(Coordenadas coordenadas) {
    setCoordenadas(coordenadas);
    setBorde(BordeCasilla.NONE);
  }

  Casilla(Coordenadas coordenadas, BordeCasilla tipoBorde) {
    setCoordenadas(coordenadas);
    setBorde(tipoBorde);
  }

  Casilla(Coordenadas coordenadas, Pais pais) {
    setCoordenadas(coordenadas);
    setBorde(BordeCasilla.NONE);
  }

  Casilla(Coordenadas coordenadas) {
    setCoordenadas(coordenadas);
    setBorde(BordeCasilla.NONE);
    setPais(null); // Esta casilla no tiene asociado un país, es una casilla marítima
  }

  Casilla(Coordenadas coordenadas, BordeCasilla tipoBorde) {
    setCoordenadas(coordenadas);
    setBorde(tipoBorde);
    setPais(null); // Esta casilla no tiene asociado un país, es una casilla marítima
  }

  Casilla(Coordenadas coordenadas, Pais pais) {
    setCoordenadas(coordenadas);
    setBorde(BordeCasilla.NONE);
    setPais(pais);
  }

  public void setBorde(BordeCasilla borde) {
    this.borde = borde;
  }

  @Override
  public boolean equals(Object casilla) {
    if (this == casilla) {
      return true;
    }
    if (casilla == null) {
      return false;
    }
    if (getClass() != casilla.getClass()) {
      return false;
    }
    final Casilla other = (Casilla) casilla;
    if (!this.getCoordenadas().equals(other.getCoordenadas())) {
      return false;
    }

    return true;
  }

  @Deprecated
  public abstract boolean esMaritima();
}
