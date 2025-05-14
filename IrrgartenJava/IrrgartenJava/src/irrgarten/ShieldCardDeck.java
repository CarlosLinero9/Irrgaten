package irrgarten;

/**
 *
 * @author Juan Carlos Vílchez Sánchez
 */
public class ShieldCardDeck extends CardDeck<Shield> {
    
    @Override
    protected void addCards(){
        final int numeroCartas = 20;
        for(int i = 0; i < numeroCartas; i++){
            Shield escudo = new Shield(Dice.shieldPower(), Dice.usesLeft());
            addCard(escudo);
        }
    }
}
