package irrgarten;
import java.util.ArrayList;

/**
 * La clase Player representa a un jugador en el juego. Cada jugador tiene un nombre,
 * número, inteligencia, fuerza, salud, posición en el laberinto, y puede poseer armas y escudos.
 * Puede realizar acciones como moverse, atacar, defenderse, recibir recompensas y más.
 * Además, proporciona una representación en cadena de sus atributos.
 *
 * @author Juan Carlos Vílchez Sánchez
 */
public class Player extends LabyrinthCharacter{

    // Constantes de la clase
    private static final int MAX_WEAPONS = 2;
    private static final int MAX_SHIELDS = 3;
    private static final int INITIAL_HEALTH = 10;
    private static final int HITS2LOSE = 3;

    // Variables de instancia
    private char number;
    private int consecutiveHits = 0;
    private ArrayList<Weapon> weapons;
    private ArrayList<Shield> shields;
    private ShieldCardDeck shieldCardDeck;
    private WeaponCardDeck weaponCardDeck;

    /**
     * Construye un nuevo objeto Player con el número, inteligencia y fuerza especificados.
     *
     * @param number Número del jugador.
     * @param intelligence Nivel de inteligencia del jugador.
     * @param strength Nivel de fuerza del jugador.
     */
    public Player(char number, float intelligence, float strength){
        super("Player# " + number, intelligence, strength, INITIAL_HEALTH);
        this.number = number;
        shields = new ArrayList<>();
        weapons = new ArrayList<>();
        weaponCardDeck = new WeaponCardDeck();
        weaponCardDeck.addCards();
        shieldCardDeck = new ShieldCardDeck();
        shieldCardDeck.addCards();
    }
    
    
    public Player(Player other){
        super(other);
        number = other.number;
        shields = other.shields;
        weapons = other.weapons;
        shieldCardDeck = other.shieldCardDeck;
        weaponCardDeck = other.weaponCardDeck;
    }

    /**
     * Resucita al jugador, restableciendo su salud, escudos y armas.
     */
    public void resurrect(){
        setHealth(INITIAL_HEALTH);
        shields.clear();
        weapons.clear();
        consecutiveHits = 0;
    }

    /**
     * Obtiene el número del jugador.
     *
     * @return El número del jugador.
     */
    public char getNumber(){
        return number;
    }

    /**
     * Realiza un movimiento en la dirección especificada, teniendo en cuenta las direcciones válidas.
     *
     * @param direction Dirección en la que se moverá el jugador.
     * @param validMoves Array de direcciones válidas.
     * @return La dirección final en la que se movió el jugador.
     */
    public Directions move(Directions direction, ArrayList<Directions> validMoves){
        int size = validMoves.size();
        boolean contained = validMoves.contains(direction);
        
        if((size > 0) && (!contained)){
            Directions firstElement = validMoves.get(0);
            return firstElement;
        }else{
            return direction;
        }
    }

    /**
     * Realiza un ataque y devuelve la intensidad del ataque.
     *
     * @return La intensidad del ataque basada en la fuerza del jugador y las armas que posee.
     */
    @Override
    public float attack(){
        return sumWeapons() + getStrength();
    }

    /**
     * Intenta defenderse contra un ataque recibido.
     *
     * @param receivedAttack La intensidad del ataque recibido.
     * @return true si el jugador se defiende con éxito, false en caso contrario.
     */
    @Override
    public boolean defend(float receivedAttack){
        return manageHit(receivedAttack);
    }

    /**
     * Recibe una recompensa después de ganar un combate.
     */
    public void receiveReward(){
        int wReward = Dice.weaponsReward();
        int sReward = Dice.shieldsReward();
        
        for(int i = 0; i < wReward; i++){
            Weapon wnew = newWeapon();
            receiveWeapon(wnew);
        }
        
        for(int i = 0; i < sReward; i++){
            Shield snew = newShield();
            receiveShield(snew);
        }
        
        int extraHealth = Dice.healthReward();
        setHealth(getHealth() + extraHealth);
        
    }
    
    @Override
    public String toString() {
        return "Nº: " + number + ", " + super.toString();
    }

    // Métodos privados

    /**
     * Recibe un arma y la agrega a la lista de armas del jugador.
     *
     * @param w Arma recibida.
     */
    private void receiveWeapon(Weapon w){
        for (int i=0; i<weapons.size(); i++){
            boolean discard = weapons.get(i).discard();
            if (discard)
                weapons.remove(weapons.get(i));
        }
        
        int size = weapons.size();
        if (size<MAX_WEAPONS)
            weapons.add(w);
    }

    /**
     * Recibe un escudo y lo agrega a la lista de escudos del jugador.
     *
     * @param s Escudo recibido.
     */
    private void receiveShield(Shield s){
        for (int i=0; i<shields.size(); i++){
            boolean discard = shields.get(i).discard();
            if (discard){
                shields.remove(shields.get(i));
            }
            
            int size = shields.size();
            if (size<MAX_SHIELDS){
                shields.add(s);
            }
        }
    }

    
    private Weapon newWeapon(){
        return weaponCardDeck.nextCard();
    }

    private Shield newShield(){
        return shieldCardDeck.nextCard();
    }
    

    /**
     * Calcula y devuelve la suma de las intensidades de las armas del jugador.
     *
     * @return La suma de las intensidades de las armas del jugador.
     */
    protected float sumWeapons(){
        float suma = 0.0f;
        for(Weapon weapon :  weapons){
            suma += weapon.attack();
        }
        return suma;
    }

    /**
     * Calcula y devuelve la suma de las intensidades de los escudos del jugador.
     *
     * @return La suma de las intensidades de los escudos del jugador.
     */
    protected float sumShields(){
        float suma = 0.0f;
        for(Shield shield : shields){
            suma += shield.protect();
        }
        return suma;
    }

    /**
     * Calcula y devuelve la energía defensiva total del jugador, que incluye la suma de los escudos y la inteligencia.
     *
     * @return La energía defensiva total del jugador.
     */
    protected float defensiveEnergy(){
        return sumShields() + getIntelligence();
    }

    /**
     * Gestiona el impacto recibido durante un combate, determinando si el jugador se defiende con éxito o sufre daño.
     *
     * @param receivedAttack La intensidad del ataque recibido.
     * @return true si el jugador se defiende con éxito, false si sufre daño.
     */
    private boolean manageHit(float receivedAttack){
        float defense = defensiveEnergy();
        
        if(defense < receivedAttack){
            gotWounded();
            incConsecutiveHits();
        }else{
            resetHits();
        }
        
        boolean lose;
        if((consecutiveHits == HITS2LOSE) || dead()){
            resetHits();
            lose = true;
        }else{
            lose=false;
        }
        return lose;
    }

    /**
     * Reinicia el contador de golpes consecutivos.
     */
    private void resetHits(){
        consecutiveHits = 0;
    }

    /**
     * Incrementa el contador de golpes consecutivos recibidos.
     */
    private void incConsecutiveHits(){
       consecutiveHits += 1; 
    }
}

