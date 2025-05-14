
package irrgarten.UI;

import irrgarten.Directions;
import irrgarten.GameState;

/**
 *
 * @author Juan Carlos Vílchez Sánchez
 */
public interface UI {
    public Directions nextMove();
    public void showGame(GameState gameState);
}
