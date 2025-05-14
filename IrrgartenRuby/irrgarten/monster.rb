#encoding: utf-8
require_relative 'dice'
require_relative 'labyrinth_character'

module Irrgarten
	class Monster < LabyrinthCharacter

	  @@INITIAL_HEALTH=5


	  def initialize(name,intelligence,strength)
	      super(name, intelligence, strength, @@INITIAL_HEALTH)
	  end

	  def attack()
	    Dice.intensity(get_strength)
	  end
	  
	  def defend(received_attack)
	    is_dead = dead()
	    
	    if (!is_dead)
	    	defensive_energy = Dice.intensity(get_intelligence())
	    	
	    	if (defensive_energy < received_attack)
	    		got_wounded()
	    		is_dead = dead()
	    	end
	    end
	    is_dead
	  end
	 
	end
end
