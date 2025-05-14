
module Irrgarten
  class GameState
    def initialize(labyrinth,players,monsters,current_player,winner,log)
      @labyrinth=labyrinth
      @players=players
      @monsters=monsters
      @current_player=current_player
      @winner=winner
      @log=log
    end
    attr_reader :current_player, :log, :labyrinth, :winner, :players, :monsters
  
    #Los nombro usando get para distinguir de los otros
    #Estos metodos son redundantes
    def get_labyrinth
      @labyrinth
    end
    
    def get_players
        @players
    end
    
    def get_monsters
        @monsters
    end
    
    def get_current_player
        @current_player
    end
    
    def get_winner
        @winner
    end
    
    def get_log
        @log
    end
  end
end
