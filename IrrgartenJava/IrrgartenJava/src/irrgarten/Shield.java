package irrgarten;

/**
 * La clase Shield representa un escudo en el juego. Cada escudo tiene un nivel de protección
 * y una cantidad limitada de usos. Puede ser utilizado para proteger al jugador durante el combate.
 * Además, proporciona una representación en cadena de sus atributos.
 *
 * @author Juan Carlos Vílchez Sánchez
 */
public class Shield extends CombatElement {

    /**
     * Construye un nuevo objeto Shield con el nivel de protección y cantidad de usos especificados.
     *
     * @param protection Nivel de protección del escudo.
     * @param uses Cantidad de usos disponibles del escudo.
     */
    public Shield(float protection, int uses){
        super(protection, uses);
    }

    /**
     * Protege al jugador utilizando el escudo y devuelve el nivel de protección.
     * Reduce la cantidad de usos disponibles del escudo en caso de éxito.
     *
     * @return El nivel de protección del escudo si tiene usos disponibles, 0 en caso contrario.
     */
    public float protect(){
        return produceEffect();
    }

    /**
     * Devuelve una representación en cadena de los atributos del escudo.
     *
     * @return Una cadena que representa el nivel de protección y la cantidad de usos del escudo.
     */
    @Override
    public String toString(){
        return "S" + super.toString();
    }
}
