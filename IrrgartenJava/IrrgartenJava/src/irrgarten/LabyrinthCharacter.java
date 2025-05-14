package irrgarten;

/**
 *
 * @author Juan Carlos Vílchez Sánchez
 */
public abstract class LabyrinthCharacter {
    private String name;
    private float intelligence;
    private float strength;
    private float health;
    private int row;
    private int col;
    
    private static final int INVALID_POS = -1;
    
    // Constructor
    public LabyrinthCharacter(String name, float intelligence, float strength, float health) {
        this.name = name;
        this.intelligence = intelligence;
        this.strength = strength;
        this.health = health;
        
        setPos(INVALID_POS, INVALID_POS);
    }
    
    //Revisar
    // Copy Constructor
    public LabyrinthCharacter(LabyrinthCharacter other) {
        this(other.name, other.intelligence, other.strength, other.health);
        this.row = other.row;
        this.col = other.col;
    }
    
    // Getters
    protected float getIntelligence() {
        return intelligence;
    }
    
    protected float getStrength() {
        return strength;
    }
    
    protected float getHealth() {
        return health;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    // Setters
    protected void setHealth(float health) {
        this.health = health;
    }


    /**
     * Establece la posición del personaje en el laberinto.
     *
     * @param row Fila en la que se colocará el monstruo.
     * @param col Columna en la que se colocará el monstruo.
     */
    public void setPos(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    // Other methods
    public boolean dead() {
        return health <= 0;
    }
    
    protected void gotWounded() {
        health --;
    }

    /**
     * Devuelve una representación en cadena de los atributos del personaje.
     *
     * @return Una cadena que representa el nombre, inteligencia, fuerza, salud, fila y columna del monstruo.
     */
    @Override
    public String toString() {
        return "N: " + name + ", I: " + intelligence + 
                ", S: " + strength + ", H: " + health + ", R: " 
                + row + ", C: " + col + "\n";
    }
    
    public abstract float attack();
    
    public abstract boolean defend(float attack);
}
