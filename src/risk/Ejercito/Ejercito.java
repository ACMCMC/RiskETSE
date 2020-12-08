package risk.ejercito;

import risk.Dado;

/**
 * Es una clase abstracta que se especializa en tipos de Ejércitos
 */
public abstract class Ejercito {
    public abstract Dado[] ataque(Dado[] dados);
}
