package irrgarten;
import java.util.ArrayList;

/**
 *
 * @author Juan Carlos Vílchez Sánchez
 */

public class FuzzyPlayer extends Player {
    public FuzzyPlayer(Player other){
        super(other);
    }
    
    @Override
    public Directions move(Directions direction, ArrayList<Directions> validMoves){
        Directions directionReturned = super.move(direction, validMoves);
        return Dice.nextStep(directionReturned, validMoves, getIntelligence());
    }
    
    @Override
    public float attack(){
        return Dice.intensity(getStrength()) + this.sumWeapons();
    }
    
    @Override
    protected float defensiveEnergy(){
        return Dice.intensity(getIntelligence()) + this.sumShields();
    }
    
    @Override
    public String toString(){
        return "Fuzzy " + super.toString();
    }
}
