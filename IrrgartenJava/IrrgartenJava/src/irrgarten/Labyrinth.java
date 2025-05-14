package irrgarten;
import java.util.ArrayList;

/**
 * La clase Labyrinth representa el laberinto en el que se desarrolla el juego.
 * Contiene métodos para gestionar la colocación de jugadores, monstruos, bloques, y otras
 * operaciones relacionadas con la interacción y movimiento en el laberinto.
 * Además, proporciona información sobre el estado actual del laberinto y las posiciones disponibles.
 * 
 * @author Juan Carlos Vílchez Sánchez
 */
public class Labyrinth {

    // Constantes de la clase
    private static final char BLOCK_CHAR = 'X';
    private static final char EMPTY_CHAR = '-';
    private static final char MONSTER_CHAR = 'M';
    private static final char COMBAT_CHAR = 'C';
    private static final char EXIT_CHAR = 'E';
    private static final int ROW = 0;
    private static final int COL = 1;

    // Variables de instancia
    private int nRows;
    private int nCols;
    private int exitRow;
    private int exitCol;
    private Monster[][] monsters;
    private Player[][] players;
    private char[][] labyrinth;

    /**
     * Construye un nuevo laberinto con el número especificado de filas y columnas,
     * y define la posición de salida del laberinto.
     *
     * @param nRows Número de filas del laberinto.
     * @param nCols Número de columnas del laberinto.
     * @param exitRow Fila de la posición de salida.
     * @param exitCol Columna de la posición de salida.
     */
    public Labyrinth(int nRows, int nCols, int exitRow, int exitCol) {
        // Inicialización de variables
        this.nRows = nRows;
        this.nCols = nCols;
        this.exitRow = exitRow;
        this.exitCol = exitCol;

        monsters = new Monster[nRows][nCols];
        players = new Player[nRows][nCols];
        labyrinth = new char[nRows][nCols];

        // Inicialización del laberinto con celdas vacías
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                labyrinth[i][j] = EMPTY_CHAR;
            }
        }
        
        // Definición de la posición de salida en el laberinto
        labyrinth[exitRow][exitCol] = EXIT_CHAR;
    }

    /**
     * Distribuye a los jugadores en el laberinto de manera aleatoria.
     *
     * @param players La lista de jugadores que se distribuirán en el laberinto.
     */
    public void spreadPlayers(ArrayList <Player> players) {
        for(int i = 0; i < players.size(); i++){
            Player p = players.get(i);
            int[] pos = randomEmptyPos();
            
            int oldCol = -1;
            int oldRow = -1;
            putPlayer2D(oldRow, oldCol, pos[ROW], pos[COL], p); 
        }
    }

    /**
     * Verifica si hay un ganador en el laberinto (jugador en la posición de salida).
     *
     * @return true si hay un ganador, false en caso contrario.
     */
    public boolean haveAWinner() {
        return (players[exitRow][exitCol] != null); 
    }

    /**
     * Devuelve una representación en cadena del estado actual del laberinto.
     *
     * @return Una cadena que representa el estado actual del laberinto.
     */
    @Override
    public String toString() {
        String laberinto = "Rows: " + nRows + ", Cols: " + nCols +
                ", Exit: (" + exitRow + ", " + exitCol + ")\n";
        laberinto += "\n";

        // Construcción de la representación en cadena del laberinto
        for (int i = 0; i < nRows; i++) {
            laberinto += "\t";
            for (int j = 0; j < nCols; j++) {
                laberinto += String.format("%-2c" , labyrinth[i][j]);
                laberinto += "   ";
            }
            laberinto += "\n";
        }
        return laberinto;
    }

    /**
     * Agrega un monstruo en la posición especificada del laberinto.
     *
     * @param row Fila en la que se colocará el monstruo.
     * @param col Columna en la que se colocará el monstruo.
     * @param monster El monstruo que se colocará en la posición especificada.
     */
    public void addMonster(int row, int col, Monster monster) {
        if (this.emptyPos(row, col) && this.posOK(row, col)) {
            monsters[row][col] = monster;
            labyrinth[row][col] = MONSTER_CHAR;
            monster.setPos(row, col);
        }
    }

    /**
     * Coloca a un jugador en el laberinto y maneja las interacciones en la nueva posición.
     *
     * @param direction La dirección en la que el jugador se moverá.
     * @param player El jugador que se moverá en la dirección especificada.
     * @return El monstruo con el que el jugador puede entrar en combate, si hay alguno.
     */
    public Monster putPlayer(Directions direction, Player player){
        int oldRow = player.getRow();
        int oldCol = player.getCol();
        
        int[] newPos = dir2Pos(oldRow, oldCol, direction);
        Monster monster = putPlayer2D(oldRow, oldCol, newPos[ROW], 
                          newPos[COL], player);
        
        return monster;
    }
    
            

    /**
     * Agrega un bloque al laberinto en una orientación específica y con una longitud dada.
     *
     * @param orientation La orientación del bloque (horizontal o vertical).
     * @param startRow Fila de inicio del bloque.
     * @param startCol Columna de inicio del bloque.
     * @param length Longitud del bloque.
     */
    public void addBlock(Orientation orientation, int startRow, int startCol, 
           int length) {
        int incRow, incCol;
        if(orientation == Orientation.VERTICAL){
            incRow = 1;
            incCol = 0;
        }else{
            incRow = 0;
            incCol = 1;
        }
        int row = startRow;
        int col = startCol;
        
        while((posOK(row,col)) && (emptyPos(row, col)) && (length > 0)){
            labyrinth[row][col] = BLOCK_CHAR;
            length -= 1;
            row += incRow;
            col += incCol;
        }
    }

    /**
     * Obtiene las direcciones válidas en la posición especificada del laberinto.
     *
     * @param row Fila en la que se verifica la validez de las direcciones.
     * @param col Columna en la que se verifica la validez de las direcciones.
     * @return Un array de direcciones válidas en la posición especificada.
     */
    public ArrayList<Directions> validMoves(int row, int col) {
        ArrayList<Directions> output = new ArrayList<>();
        
        if(canStepOn(row+1, col)){
            output.add(Directions.DOWN);
        }
        if(canStepOn(row-1, col)){
            output.add(Directions.UP);
        }
        if(canStepOn(row, col+1)){
            output.add(Directions.RIGHT);
        }
        if(canStepOn(row, col-1)){
            output.add(Directions.LEFT);
        }
        
       return output; 
    }
    
    public void setFuzzyPlayer(int row, int col, FuzzyPlayer fuzzyPlayer){
        players[row][col] = fuzzyPlayer;
    }

    // Métodos privados

    /**
     * Verifica si una posición está dentro de los límites del laberinto.
     *
     * @param row Fila a verificar.
     * @param col Columna a verificar.
     * @return true si la posición está dentro de los límites, false en caso contrario.
     */
    private boolean posOK(int row, int col) {
        return ((row >= 0 && row < nRows) && (col >= 0 && col < nCols));
    }

    /**
     * Verifica si una posición está vacía en el laberinto.
     *
     * @param row Fila a verificar.
     * @param col Columna a verificar.
     * @return true si la posición está vacía, false en caso contrario.
     */
    private boolean emptyPos(int row, int col) {
        return (posOK(row, col) && labyrinth[row][col] == EMPTY_CHAR);
    }

    /**
     * Verifica si hay un monstruo en la posición especificada del laberinto.
     *
     * @param row Fila a verificar.
     * @param col Columna a verificar.
     * @return true si hay un monstruo en la posición, false en caso contrario.
     */
    private boolean monsterPos(int row, int col) {
        return (posOK(row,col) && monsters[row][col] != null);
    }

    /**
     * Verifica si la posición especificada es la salida del laberinto.
     *
     * @param row Fila a verificar.
     * @param col Columna a verificar.
     * @return true si la posición es la salida, false en caso contrario.
     */
    private boolean exitPos(int row, int col) {
        return (posOK(row,col) && labyrinth[row][col] == EXIT_CHAR);
    }

    /**
     * Verifica si la posición especificada es para el combate en el laberinto.
     *
     * @param row Fila a verificar.
     * @param col Columna a verificar.
     * @return true si la posición es para el combate, false en caso contrario.
     */
    private boolean combatPos(int row, int col) {
        return (posOK(row,col) && labyrinth[row][col] == COMBAT_CHAR);
    }

    /**
     * Verifica si es posible moverse a la posición especificada.
     *
     * @param row Fila a verificar.
     * @param col Columna a verificar.
     * @return true si es posible moverse a la posición, false en caso contrario.
     */
    private boolean canStepOn(int row, int col) {
        return (posOK(row, col) && (emptyPos(row, col) || monsterPos(row, col) 
                || exitPos(row, col)));
    }

    /**
     * Actualiza la posición anterior en el laberinto después de un movimiento.
     *
     * @param row Fila de la posición anterior.
     * @param col Columna de la posición anterior.
     */
    private void updateOldPos(int row, int col) {
        if (posOK(row, col)) {
            if (combatPos(row, col)) {
                labyrinth[row][col] = MONSTER_CHAR;
            } else {
                labyrinth[row][col] = EMPTY_CHAR;
            } 
        }
    }

    /**
     * Convierte la dirección especificada en nuevas coordenadas en el laberinto.
     *
     * @param row Fila actual.
     * @param col Columna actual.
     * @param direction Dirección en la que se moverá.
     * @return Un array de dos elementos con las nuevas coordenadas (fila y columna).
     */
    private int[] dir2Pos(int row, int col, Directions direction) {
        int fila = row;
        int columna = col;

        //Podía usar un switch
        if (direction == Directions.UP) {
            fila -= 1;
        }
        if (direction == Directions.DOWN) {
            fila += 1;
        }
        if (direction == Directions.LEFT) {
            columna -= 1;
        }
        if (direction == Directions.RIGHT) {
            columna += 1;
        }

        return new int[]{fila, columna};
    }

    /**
     * Encuentra una posición vacía y aleatoria en el laberinto.
     *
     * @return Un array de dos elementos con las coordenadas de la posición vacía.
     */
    private int[] randomEmptyPos() {
        int fila;
        int columna;

        do {
            fila = Dice.randomPos(nRows);
            columna = Dice.randomPos(nCols);
        } while (!emptyPos(fila, columna));

        return new int[]{fila, columna};
    }

    /**
     * Coloca a un jugador en el laberinto y maneja las interacciones en la nueva posición.
     *
     * @param oldRow Fila anterior del jugador.
     * @param oldCol Columna anterior del jugador.
     * @param row Nueva fila del jugador.
     * @param col Nueva columna del jugador.
     * @param player El jugador que se está moviendo.
     * @return El monstruo con el que el jugador puede entrar en combate, si hay alguno.
     */
    private Monster putPlayer2D(int oldRow, int oldCol, int row, int col, Player player) {
        Monster output = null;
        
        if(canStepOn(row, col)){
            if(posOK(oldRow, oldCol)){
                Player p = players[oldRow][oldCol];
                if(p == player){
                    updateOldPos(oldRow, oldCol);
                    players[oldRow][oldCol] = null;
                }
            }
            boolean monsterPos = monsterPos(row, col);
            
            if(monsterPos){
                labyrinth[row][col] = COMBAT_CHAR;
                output = monsters[row][col];
            }else{
                char number = player.getNumber();
                labyrinth[row][col] = number;
            }
            players[row][col] = player;
            player.setPos(row, col);
        }
        return output;
    }
}
