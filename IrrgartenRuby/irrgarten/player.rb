# encoding: utf-8
require_relative 'dice'
require_relative 'directions'
require_relative 'shield'
require_relative 'weapon'
require_relative 'labyrinth_character'

module Irrgarten
  class Player < LabyrinthCharacter
    @@MAX_WEAPONS = 2
    @@MAX_SHIELDS = 3
    @@INITIAL_HEALTH = 10
    @@HITS_TO_LOSE = 3

    attr_reader :number, :row, :col, :weapons, :shields, :consecutive_hits

    def initialize(number, intelligence, strength)
      super("Player #" + number.to_s, intelligence, strength, @@INITIAL_HEALTH)
      @number = number
      reset_hits
      @weapons = Array.new
      @shields = Array.new
    end

    #Falta el constructor de copia
    def copia(other)
      super(other)
      @weapons=other.weapons
      @shields=other.shields
      @consecutive_hits=other.consecutive_hits
      @number=other.number
    end
    
    def resurrect
    	@health = @@INITIAL_HEALTH
    	@@weapons = Array.new
      @shields = Array.new
      reset_hits
    end

    def get_number()
      @number
    end
    
    def move(direction, valid_moves)
      size = valid_moves.size
      contained = valid_moves.include?(direction)
      if (size > 0 && !contained)
          return valid_moves[0]
      else
          return direction
      end
    end


    def attack
      @strength + sum_weapons
    end

    def defend(received_attack)
      manage_hit(received_attack)
    end

    def receive_reward
      w_reward = Dice.weapons_reward
      s_reward = Dice.shields_reward

      w_reward.times do
        w_new = new_weapon
        receive_weapon(w_new)
      end

      s_reward.times do
        s_new = new_shield
        receive_shield(s_new)
      end

      extra_health = Dice.health_reward
      @health += extra_health
    end

    def to_s
      "Nº: #{@number}, Hits: #{@consecutive_hits}, " + super
    end

    private

    def receive_weapon(w)
      #lo borro si no es un arma y discard ==true
      @weapons.delete_if { |wi| wi.is_a?(Weapon) && wi.discard } 
      
      #Lo añado si hay espacio y es un arma
      if @weapons.size < @@MAX_WEAPONS && w.is_a?(Weapon)
        @weapons << w 
      end
    end

    def receive_shield(s)
      #lo borro si no es un escudo y discard ==true
      @shields.delete_if { |si| si.is_a?(Shield) && si.discard }
      
      #Lo añado si hay espacio y es un escudo
      if @shields.size < @@MAX_SHIELDS && s.is_a?(Shield)
        @shields << s 
      end
    end
    
    def new_weapon()
    	Weapon.new(Dice.weapon_power, Dice.uses_left)
    end
    
    def new_shield()
    	Shield.new(Dice.shield_power, Dice.uses_left)
    end

    def manage_hit(received_attack)
      defense = defensive_energy()
      if defense < received_attack
        got_wounded()
        inc_consecutive_hits()
      else
        reset_hits()
      end
      if (@consecutive_hits == @@HITS_TO_LOSE || dead())
        reset_hits()
        true
      else
        false
      end
    end

    def reset_hits
      @consecutive_hits = 0
    end

    def inc_consecutive_hits
      @consecutive_hits += 1
    end

    protected
    def sum_weapons
      fuerza = 0
      @weapons.each do |w|
        fuerza += w.attack
      end
      fuerza
    end

    def sum_shields
      proteccion = 0
      @shields.each do |s|
        proteccion += s.protect
      end
      proteccion
    end

    def defensive_energy
      @intelligence + sum_shields
    end

  end
end

