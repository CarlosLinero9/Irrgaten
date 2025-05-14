package irrgarten;

/**
 * La clase Weapon representa un arma en el juego. Cada arma tiene un poder de ataque
 * y una cantidad limitada de usos. Puede ser utilizada por el jugador durante el combate.
 * Además, proporciona una representación en cadena de sus atributos.
 *
 * @author Juan Carlos Vílchez Sánchez
 */
public class Weapon extends CombatElement{

    /**
     * Construye un nuevo objeto Weapon con el poder de ataque y cantidad de usos especificados.
     *
     * @param power Poder de ataque del arma.
     * @param uses Cantidad de usos disponibles del arma.
     */
    public Weapon(float power, int uses){
        super(power, uses);
    }

    /**
     * Realiza un ataque utilizando el arma y devuelve el poder de ataque.
     * Reduce la cantidad de usos disponibles del arma en caso de éxito.
     *
     * @return El poder de ataque del arma si tiene usos disponibles, 0 en caso contrario.
     */
    public float attack(){
        return produceEffect();
    }

    /**
     * Devuelve una representación en cadena de los atributos del arma.
     *
     * @return Una cadena que representa el poder de ataque y la cantidad de usos del arma.
     */
    @Override
    public String toString(){
        return "W" + super.toString();
    }
}
