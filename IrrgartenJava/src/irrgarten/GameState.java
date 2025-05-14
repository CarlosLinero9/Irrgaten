package irrgarten;

/**
 * La clase GameState representa el estado actual del juego, incluyendo información sobre el laberinto,
 * jugadores, monstruos, jugador actual, estado de victoria, y un registro de eventos del juego.
 * Puede utilizarse para consultar y mantener información sobre el progreso y estado del juego.
 *
 * @author Juan Carlos Vílchez Sánchez
 */
public class GameState {
    // Variables de instancia
    private String labyrinthv;
    private String players;
    private String monsters;
    private int currentPlayer;
    private boolean winner;
    private String log;
    
    /**
     * Construye un nuevo objeto GameState con la información proporcionada.
     *
     * @param labyrinthv Representación en cadena del estado del laberinto.
     * @param players Representación en cadena de los jugadores.
     * @param monsters Representación en cadena de los monstruos.
     * @param currentPlayer Índice del jugador actual.
     * @param winner true si hay un ganador, false en caso contrario.
     * @param log Registro de eventos del juego en forma de cadena.
     */
    public GameState(String labyrinthv, String players, String monsters, 
            int currentPlayer, boolean winner, String log){
        // Inicialización de variables
        this.labyrinthv = labyrinthv; 
        this.players = players;
        this.monsters = monsters;
        this.currentPlayer = currentPlayer;
        this.winner = winner;
        this.log = log;
    }
    
    /**
     * Obtiene la representación en cadena del estado del laberinto.
     *
     * @return La representación en cadena del estado del laberinto.
     */
    public String getLabyrinthv(){
        return labyrinthv;
    }
    
    /**
     * Obtiene la representación en cadena de los jugadores.
     *
     * @return La representación en cadena de los jugadores.
     */
    public String getPlayers(){
        return players;
    }
    
    /**
     * Obtiene la representación en cadena de los monstruos.
     *
     * @return La representación en cadena de los monstruos.
     */
    public String getMonsters(){
        return monsters;
    }
    
    /**
     * Obtiene el índice del jugador actual.
     *
     * @return El índice del jugador actual.
     */
    public int getCurrentPlayer(){
        return currentPlayer;
    }
    
    /**
     * Verifica si hay un ganador en el juego.
     *
     * @return true si hay un ganador, false en caso contrario.
     */
    public boolean getWinner(){
        return winner;
    }
    
    /**
     * Obtiene el registro de eventos del juego en forma de cadena.
     *
     * @return El registro de eventos del juego en forma de cadena.
     */
    public String getLog(){
        return log;
    }
}
