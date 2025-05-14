#encoding utf-8

require_relative 'dice'

module Irrgarten
    class FuzzyPlayer < Player
        #Como al final creo uno a partir de un jugador resucitado, 
        #he decidido implementar la copia en el constructor
        def initialize (other)
            copia(other)
        end

        def move(direction, valid_moves)
            direction_preference = super(direction, valid_moves)
            return Dice.next_step(direction_preference, valid_moves, get_intelligence)
        end

        def attack
            return Dice.intensity(@strength + sum_weapons)
        end

        def to_s
            return "Fuzzy " + super
        end

        protected
        def defensive_energy
          return Dice.intensity(@intelligence + sum_shields)
        end
    end
end