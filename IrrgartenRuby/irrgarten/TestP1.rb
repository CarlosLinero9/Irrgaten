#encoding utf-8

require_relative 'directions'
require_relative 'dice'
require_relative 'game_character'
require_relative 'orientation'
require_relative 'shield'
require_relative 'weapon'
require_relative 'game_state'

class TestP1
    def main
        puts "Probando la practica 1:\n"

        puts "---------------Enum Directions-------------------"
        puts Irrgarten::Directions::LEFT
        puts Irrgarten::Directions::RIGHT
        puts Irrgarten::Directions::UP
        puts Irrgarten::Directions::DOWN

        puts "\n--------------Enum GameCharacter------------------"
        puts Irrgarten::GameCharacter::PLAYER
        puts Irrgarten::GameCharacter::MONSTER

        puts "\n--------------Enum Orientation-----------------"
        puts Irrgarten::Orientation::VERTICAL
        puts Irrgarten::Orientation::HORIZONTAL

        puts "\n--------------Creando instancias de clase-----------------"
        arma1 = Irrgarten::Weapon.new(3.2, 4)
        arma2 = Irrgarten::Weapon.new(1.1, 2)
        shield1 = Irrgarten::Shield.new(2.3, 3)
        shield2 = Irrgarten::Shield.new(1.2, 1)
        juego = Irrgarten::GameState.new("Garden of Bambam", "Mario:vivo, Miguel:vivo, Andrea:muerto", "Minotauro, Manticora",0, false,"Se ha creado el tablero de juego")

        puts "\n--------------Muestra de armas y escudos-----------------"
        puts "Arma 1: " + arma1.to_s
        puts "Arma 2: " + arma2.to_s
        puts "Escudo 1: " + shield1.to_s
        puts "Escudo 2: " + shield2.to_s
        
        puts "\nAtacando con arma 1, poder: " + arma1.attack.to_s
        puts "Compruebo usos: " + arma1.to_s
        puts "¿Tras el ataque se descarta? " + arma1.discard.to_s
        puts "\nProtegiendo con shield1, proteccion: " + shield1.protect.to_s
        puts "Compruebo usos: " + shield1.to_s
        puts "¿Tras proteger se descarta? " + shield1.discard.to_s

        puts "\n--------------Estado actual del juego-----------------"
        puts "labyrinth: " + juego.get_labyrinth
        puts "players: " + juego.get_players
        puts "monsters: " + juego.get_monsters
        puts "currentplayer: " + juego.get_current_player.to_s
        puts "winner: " + juego.get_winner.to_s
        puts "log: " + juego.get_log

        puts "\n----------------Prueba del dado-------------------"
        25.times do
            puts "\nRandom Position: #{Irrgarten::Dice.random_pos(100)}"
            puts "Who Starts: #{Irrgarten::Dice.who_starts(10)}"
            puts "Random Intelligence: #{Irrgarten::Dice.random_intelligence()}"
            puts "Random Strength: #{Irrgarten::Dice.random_strength()}"
            puts "Resurrect Player: #{Irrgarten::Dice.resurrect_player()}"
            puts "Weapons Reward: #{Irrgarten::Dice.weapons_reward()}"
            puts "Shields Reward: #{Irrgarten::Dice.shields_reward()}"
            puts "Health Reward: #{Irrgarten::Dice.health_reward()}"
            puts "Weapon Power: #{Irrgarten::Dice.weapon_power()}"
            puts "Shield Power: #{Irrgarten::Dice.shield_power()}"
            puts "Numero de usos restante: #{Irrgarten::Dice.uses_left()}"
            puts "Intensidad: #{Irrgarten::Dice.intensity(5.0)}"
        end
        
    end
end

test = TestP1.new
test.main