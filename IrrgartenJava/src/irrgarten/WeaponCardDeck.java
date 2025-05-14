package irrgarten;

/**
 *
 * @author Juan Carlos Vílchez Sánchez
 */
public class WeaponCardDeck extends CardDeck<Weapon> {
    
    @Override
    protected void addCards(){
        final int numeroCartas = 20;
        for(int i = 0; i < numeroCartas; i++){
            Weapon arma = new Weapon(Dice.weaponPower(), Dice.usesLeft());
            addCard(arma);
        }
    }
}
