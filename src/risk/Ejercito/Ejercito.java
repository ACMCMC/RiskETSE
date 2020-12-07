package risk.ejercito;

import risk.Dado;

/**
 * Es una clase abstracta que se especializa en tipos de Ejércitos
 */
public abstract class Ejercito {
    abstract Dado[] ataque(Dado[] dado);
}
