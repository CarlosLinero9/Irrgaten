/**
 *
 * @author Juan Carlos Vílchez Sánchez
 */

package main;

import irrgarten.Game;
import irrgarten.UI.GraphicalUI;
import irrgarten.controller.Controller;


public class MainGUI{
    
public static void main(String[] args) throws InterruptedException {

    GraphicalUI vista = new GraphicalUI();
    Game game = new Game(1);
    Controller controller = new Controller (game, vista);
    controller.play();
    
    }
}
