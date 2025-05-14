package irrgarten;
import java.util.Random;
import java.util.ArrayList;

/**
 * La clase Dice proporciona métodos para generar valores aleatorios relacionados con el juego.
 * Estos métodos incluyen la generación de posiciones aleatorias, determinar quién comienza primero,
 * generar valores de inteligencia y fuerza, y manejar recompensas y poderes relacionados con el juego.
 * Además, la clase también incluye métodos para la gestión de elementos de juego, como la resurrección de jugadores
 * y la probabilidad de descartar elementos.
 *
 * @author Juan Carlos Vílchez Sánchez
 */
public class Dice {
    // Constantes de la clase
    private static final int MAX_USES = 5;
    private static final float MAX_INTELLIGENCE = 10.0f;
    private static final float MAX_STRENGTH = 10.0f;
    private static final float RESURRECT_PROB = 0.3f;
    private static final int WEAPONS_REWARD = 2;
    private static final int SHIELDS_REWARD = 3;
    private static final int HEALTH_REWARD = 5;
    private static final int MAX_ATTACK = 3;
    private static final int MAX_SHIELD = 2;
    
    // Generador de números aleatorios
    //Entiendo que no es necesario hacerlo final
    private static final Random generator = new Random(); 
    
    /**
     * Genera un número aleatorio dentro del rango [0, max).
     *
     * @param max El valor máximo (no inclusivo) para la generación aleatoria.
     * @return Un número aleatorio dentro del rango [0, max).
     */
    public static int randomPos(int max){
        return generator.nextInt(max);
    }
    
    /**
     * Determina aleatoriamente quién comienza primero entre los jugadores.
     *
     * @param nplayers El número total de jugadores.
     * @return El índice del jugador que comienza primero.
     */
    public static int whoStarts(int nplayers){
        return generator.nextInt(nplayers);
    }
    
    /**
     * Genera un valor de inteligencia aleatorio en el rango [0, MAX_INTELLIGENCE).
     *
     * @return Un valor de inteligencia aleatorio.
     */
    public static float randomIntelligence(){
        return generator.nextFloat() * MAX_INTELLIGENCE;
    }   
           
    /**
     * Genera un valor de fuerza aleatorio en el rango [0, MAX_STRENGTH).
     *
     * @return Un valor de fuerza aleatorio.
     */
    public static float randomStrength(){
        return generator.nextFloat() * MAX_STRENGTH;
    }   
    
    /**
     * Determina aleatoriamente si un jugador debe resucitar basándose en una probabilidad predefinida.
     *
     * @return true si el jugador debe resucitar, false en caso contrario.
     */
    public static boolean resurrectPlayer(){
        /*Entiendo que lo lógico es que tenga menos del 50% 
        de probabilidades de resucitar*/
        return generator.nextFloat() < RESURRECT_PROB;
    }
    
    /**
     * Genera una recompensa aleatoria para armas.
     *
     * @return La cantidad de recompensa para armas.
     */
    public static int weaponsReward(){
        return generator.nextInt(WEAPONS_REWARD + 1);   
    }
    
    /**
     * Genera una recompensa aleatoria para escudos.
     *
     * @return La cantidad de recompensa para escudos.
     */
    public static int shieldsReward(){
        return generator.nextInt(SHIELDS_REWARD + 1); 
    }
    
    /**
     * Genera una recompensa aleatoria para la salud.
     *
     * @return La cantidad de recompensa para la salud.
     */
    public static int healthReward(){
        return generator.nextInt(HEALTH_REWARD + 1); 
    }
    
    /**
     * Genera un poder aleatorio para armas.
     *
     * @return El poder del arma generado.
     */
    public static float weaponPower(){
        return generator.nextFloat() * MAX_ATTACK;
    }
    
    /**
     * Genera un poder aleatorio para escudos.
     *
     * @return El poder del escudo generado.
     */
    public static float shieldPower(){
        return generator.nextFloat() * MAX_SHIELD; 
    }
    
    /**
     * Obtiene la cantidad de usos restantes de un elemento.
     *
     * @return La cantidad de usos restantes.
     */
    public static int usesLeft(){
        return generator.nextInt(MAX_USES + 1);
    }
    
    /**
     * Calcula la intensidad de un elemento basándose en su competencia.
     *
     * @param competence La competencia del elemento.
     * @return La intensidad calculada.
     */
    public static float intensity(float competence){
        return generator.nextFloat() * competence;
    }
    
    /**
     * Determina aleatoriamente si un elemento debe ser descartado basándose 
     * en la cantidad de usos restantes.
     *
     * @param usesLeft La cantidad de usos restantes del elemento.
     * @return true si el elemento debe ser descartado, false en caso contrario.
     */
    public static boolean discardElement(int usesLeft){
        float probability = (float) (1.0 - (float) usesLeft / MAX_USES);
        
        return probability < generator.nextFloat();
    }
    
    
    public static Directions nextStep(Directions preference, 
            ArrayList<Directions> validMoves, float intelligence){
        
        boolean direccionPreferida = 
                (generator.nextFloat()*MAX_INTELLIGENCE < intelligence);
        
        if (direccionPreferida){
            return preference;
        }else{
            int movimiento = generator.nextInt(validMoves.size());
            return validMoves.get(movimiento);
        }
    }
}
