package irrgarten;

import java.util.ArrayList;

/**
 * La clase Game representa un juego en un laberinto con jugadores y monstruos.
 * Proporciona métodos para gestionar el progreso del juego, como avanzar a la siguiente etapa,
 * verificar si el juego ha terminado y obtener el estado actual del juego.
 * Además, incluye métodos internos para configurar el laberinto, manejar turnos de jugadores, 
 * gestionar combates y recompensas, y llevar un registro de eventos relevantes del juego.
 * 
 * @author Juan Carlos Vílchez Sánchez
 */
public class Game {

    // Constante de la clase
    private static final int MAX_ROUNDS = 10;

    // Variables de instancia
    private int currentPlayerIndex;
    private String log;
    private Player currentPlayer;
    private final Labyrinth labyrinth;
    private final ArrayList<Monster> monsters;
    private final ArrayList<Player> players;

    /**
     * Construye un nuevo juego con el número especificado de jugadores.
     *
     * @param nPlayers El número de jugadores en el juego.
     */
    public Game(int nPlayers) {
        // Inicialización de variables
        log = "";
        currentPlayerIndex = Dice.whoStarts(nPlayers);
        players = new ArrayList<>();
        monsters = new ArrayList<>();

        // Creación de jugadores
        for (int i = 0; i < nPlayers; i++) {
            Player jugador = new Player((char) ('0' + i), Dice.randomIntelligence(), 
                             Dice.randomStrength());
            players.add(jugador);
        }
        currentPlayer = players.get(currentPlayerIndex);

        // Creación y configuración del laberinto
        int nRows = 10;
        int nCols = 10;
        int exitRow = 4;
        int exitCol = 3;
        labyrinth = new Labyrinth(nRows, nCols, exitRow, exitCol); 
        configureLabyrinth();
    }

    /**
     * Verifica si el juego ha terminado.
     *
     * @return true si el juego ha terminado, false en caso contrario.
     */
    public boolean finished() {
        return labyrinth.haveAWinner();
    }

    /**
     * Realiza la siguiente acción en el juego, basada en la dirección preferida por el jugador actual.
     *
     * @param preferredDirection La dirección preferida por el jugador actual.
     * @return true si la acción se realizó con éxito, false en caso contrario.
     */
    public boolean nextStep(Directions preferredDirection) {
        log = "";
        if(!currentPlayer.dead()){
            Directions direction = actualDirection(preferredDirection);
            
            if(direction != preferredDirection){
                logPlayerNoOrders();
            }
            Monster monster = labyrinth.putPlayer(direction, currentPlayer);
            
            if(monster == null){
                logNoMonster();
            }else{
                GameCharacter winner = combat(monster);
                manageReward(winner);
            } 
        }else{
            manageResurrection();
        }
        boolean endGame = finished();
        
        if(!endGame){
            nextPlayer();
        }
        return endGame;
    }

    /**
     * Obtiene el estado actual del juego.
     *
     * @return El estado actual del juego encapsulado en un objeto GameState.
     */
    public GameState getGameState() {       
        String namesplayers = "";
        String namesmonsters = "";
        for(Player player : players){
            namesplayers += player.toString();
        }
        for(Monster monster : monsters){
            namesmonsters += monster.toString();
        }
        return new GameState(labyrinth.toString(), namesplayers, namesmonsters,
                currentPlayerIndex, this.finished(),log);
    }

    // Métodos privados

    /**
     * Configura el laberinto, añadiendo bloques y colocando monstruos en posiciones específicas.
     */
    private void configureLabyrinth() {
        labyrinth.addBlock(Orientation.HORIZONTAL, 0, 0, 9);
        labyrinth.addBlock(Orientation.HORIZONTAL, 8, 1, 7);
        labyrinth.addBlock(Orientation.HORIZONTAL, 5, 1, 2);
        labyrinth.addBlock(Orientation.HORIZONTAL, 6, 2, 3);
        labyrinth.addBlock(Orientation.HORIZONTAL, 2, 4, 2);
        labyrinth.addBlock(Orientation.HORIZONTAL, 4, 4, 2);
        labyrinth.addBlock(Orientation.VERTICAL, 1, 0, 5);
        labyrinth.addBlock(Orientation.VERTICAL, 2, 2, 3);
        labyrinth.addBlock(Orientation.VERTICAL, 1, 8, 6);
        labyrinth.addBlock(Orientation.VERTICAL, 2, 6, 7);

        // Colocación de monstruos en posiciones específicas
        
        
        int newpos1[] = {6,1};
        Monster monstruo_1 = new Monster ("monstruo_1", 
                Dice.randomIntelligence(), Dice.randomStrength());
        monsters.add(monstruo_1);
        labyrinth.addMonster(newpos1[0], newpos1[1], monsters.get(0));

        int newpos2[] = {2,3};
        Monster monstruo_2 = new Monster ("monstruo_2", 
                100, 100);
        monsters.add(monstruo_2);
        labyrinth.addMonster(newpos2[0], newpos2[1], monsters.get(1));
        
        int newpos3[] = {7,7};
        Monster monstruo_3 = new Monster ("monstruo_3", 
                Dice.randomIntelligence(), Dice.randomStrength());
        monsters.add(monstruo_3);
        labyrinth.addMonster(newpos3[0], newpos3[1], monsters.get(2));
        
        labyrinth.spreadPlayers(players);
    }

    /**
     * Cambia al siguiente jugador en la secuencia de turnos.
     */
    private void nextPlayer() {
        if (currentPlayerIndex == players.size()-1){
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex += 1;
        }
        currentPlayer = players.get(currentPlayerIndex);
    }

    /**
     * Determina la dirección actual basándose en la preferencia del jugador.
     *
     * @param preferredDirection La dirección preferida por el jugador.
     * @return La dirección actualizada basada en la preferencia del jugador.
     */
    private Directions actualDirection(Directions preferredDirection) {
        int currentRow = currentPlayer.getRow();
        int currentCol = currentPlayer.getCol();
        ArrayList<Directions> validMoves = labyrinth.validMoves(currentRow, currentCol);
        Directions output = currentPlayer.move(preferredDirection, validMoves);
        
        return output;
    }

    /**
     * Simula un combate entre el jugador actual y un monstruo.
     *
     * @param monster El monstruo con el que se realiza el combate.
     * @return El personaje ganador del combate.
     */
    private GameCharacter combat(Monster monster) {
        int rounds = 0;
        GameCharacter winner = GameCharacter.PLAYER;
        float playerAttack = currentPlayer.attack();
        boolean lose = monster.defend(playerAttack);
        
        
        while((!lose) && (rounds < MAX_ROUNDS)){
            rounds += 1;
            winner = GameCharacter.MONSTER;
            float monsterAttack = monster.attack();
            lose = currentPlayer.defend(monsterAttack);
            
            if(!lose){
                playerAttack = currentPlayer.attack();
                winner = GameCharacter.PLAYER;
                lose = monster.defend(playerAttack);
            }
        }
        logRounds(rounds, MAX_ROUNDS);
        
        return winner;  
    }

    /**
     * Gestiona las recompensas después de un combate.
     *
     * @param winner El personaje que ganó el combate.
     */
    private void manageReward(GameCharacter winner) {
        if(winner == GameCharacter.PLAYER){
            currentPlayer.receiveReward();
            logPlayerWon();
        }else{
            logMonsterWon();
        }
    }

    /**
     * Gestiona la resurrección de jugadores después de ciertas condiciones.
     */
    private void manageResurrection() {
        boolean resurrect = Dice.resurrectPlayer();
        
        if(resurrect){
            currentPlayer.resurrect();
            FuzzyPlayer fuzzyPlayer = new FuzzyPlayer(currentPlayer);
            players.set(currentPlayerIndex, fuzzyPlayer);
            labyrinth.setFuzzyPlayer(currentPlayer.getRow(), 
                    currentPlayer.getCol(), fuzzyPlayer);
            currentPlayer = fuzzyPlayer;
            logResurrected();
        }else{
            logPlayerSkipTurn();
        }
    }

    // Métodos de registro

    private void logPlayerWon() {
        log = log + "El jugador ha ganado el combate.\n";
    }

    private void logMonsterWon() {
        log = log + "El monstruo ha ganado el combate.\n";
    }

    private void logResurrected() {
        log = log + "El jugador ha resucitado.\n";
    }

    private void logPlayerSkipTurn() {
        log = log + "El jugador ha perdido el turno por estar muerto.\n";
    }

    private void logPlayerNoOrders() {
        log = log + "El jugador no ha seguido las órdenes del jugador humano "
                  + "(no fue posible).\n";
    }

    private void logNoMonster() {
        log = log + "El jugador se ha movido a una celda vacia o no se ha"
                  + " podido mover.\n";
    }

    private void logRounds(int rounds, int max) {
        log = log + "Se han producido un total de " + rounds 
                  + " de un máximo de " + max + "ronda/s.\n";
    }
}

