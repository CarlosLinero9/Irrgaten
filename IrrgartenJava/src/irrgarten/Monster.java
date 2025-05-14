
package irrgarten;

/**
 * La clase Monster representa a un monstruo en el juego. Cada monstruo tiene un nombre,
 * inteligencia, fuerza, salud, y posición en el laberinto.
 * Puede realizar acciones como atacar, defenderse, verificar si está muerto y actualizar su posición.
 * Además, proporciona una representación en cadena de sus atributos.
 *
 * @author Juan Carlos Vílchez Sánchez
 */
public class Monster extends LabyrinthCharacter{
    
    //Variable de clase
    private static final int INITIAL_HEALTH = 5;
    
    /**
     * Construye un nuevo objeto Monster con el nombre, inteligencia y fuerza especificados.
     *
     * @param name Nombre del monstruo.
     * @param intelligence Nivel de inteligencia del monstruo.
     * @param strength Nivel de fuerza del monstruo.
     */
    public Monster(String name, float intelligence, float strength){
        super(name, intelligence, strength, INITIAL_HEALTH);
    }    

    /**
     * Realiza un ataque y devuelve la intensidad del ataque.
     *
     * @return La intensidad del ataque basada en la fuerza del monstruo.
     */
    @Override
    public float attack(){
        return Dice.intensity(getStrength());
    }

    /**
     * Intenta defenderse contra un ataque recibido.
     *
     * @param receivedAttack La intensidad del ataque recibido.
     * @return true si el monstruo se defiende con éxito, false en caso contrario.
     */
    @Override
    public boolean defend(float receivedAttack){
        boolean isDead = dead();
        
        if(!isDead){
            float defensiveEnergy = Dice.intensity(getIntelligence());
            if(defensiveEnergy < receivedAttack){
                gotWounded();
                isDead = dead();
            }
        }
        return isDead;
    }    
}
