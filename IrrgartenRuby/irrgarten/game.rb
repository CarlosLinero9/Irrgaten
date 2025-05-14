# encoding: utf-8

require_relative 'dice'
require_relative 'game_character'
require_relative 'game_state'
require_relative 'monster'
require_relative 'player'
require_relative 'labyrinth'
require_relative 'fuzzy_player'

module Irrgarten
       class Game
	  @@MAX_ROUNDS = 10 #Es de clase pero también constante

	  def initialize(n_players)
	      @current_player_index=Dice.who_starts(n_players)
	      @log=""
	      @players=Array.new
	      @monsters=Array.new
	      n_players.times do |i|
			@players << Player.new(i, Dice.random_intelligence, Dice.random_strength)
		  end

		  n_rows = 10
          n_cols = 10
          exit_row = 4
          exit_col = 3
	      @labyrinth=Labyrinth.new(n_rows, n_cols, exit_row, exit_col)
	      @current_player=@players[@current_player_index]
	      configure_labyrinth
	  end

	  def finished()
	    @labyrinth.have_a_winner
	  end

	  def next_step(preferred_direction)
	      @log=""
	      dead=@current_player.dead()
	      if !dead
			direction=actual_direction(preferred_direction)

			if (direction != preferred_direction)
			log_player_no_orders()
			end
			monster=@labyrinth.put_player(direction,@current_player)

			if monster == nil
			log_no_monster()
			else
			winner=combat(monster)
			manage_reward(winner)
			end
	      else
			manage_resurrection()
	      end
	      endgame=finished()
	      if !endgame
			next_player()
	      end
	      endgame
	  end

	  def game_state()
		player=""
	    monster=""

	    (0..@players.length).each do |i|
			player += @players[i].to_s
	    end

	    (0..@monsters.length).each do |j|
			monster += @monsters[j].to_s
	    end

	    GameState.new(@labyrinth.to_s,player,monster,@current_player_index.to_s,
	      @labyrinth.have_a_winner,@log)
	  end

	  private

	  def configure_labyrinth()
		@labyrinth.add_block(Orientation::HORIZONTAL, 0, 0, 9);
		@labyrinth.add_block(Orientation::HORIZONTAL, 8, 1, 7);
		@labyrinth.add_block(Orientation::HORIZONTAL, 5, 1, 2);
		@labyrinth.add_block(Orientation::HORIZONTAL, 6, 2, 3);
		@labyrinth.add_block(Orientation::HORIZONTAL, 2, 4, 2);
		@labyrinth.add_block(Orientation::HORIZONTAL, 4, 4, 2);
		@labyrinth.add_block(Orientation::VERTICAL, 1, 0, 5);
		@labyrinth.add_block(Orientation::VERTICAL, 2, 2, 3);
		@labyrinth.add_block(Orientation::VERTICAL, 1, 8, 6);
		@labyrinth.add_block(Orientation::VERTICAL, 2, 6, 7);
	   
		monstruo1=Monster.new("MONSTRUO1",100,100)
	    monstruo2=Monster.new("MONSTRUO2",100,100)
	    monstruo3=Monster.new("MONSTRUO3",100,100)

		@labyrinth.add_monster(6,1,monstruo1)
	    @labyrinth.add_monster(2,3,monstruo2)
		@labyrinth.add_monster(7,7,monstruo3)
	    @monsters << monstruo1
	    @monsters << monstruo2
		@monsters << monstruo3

		@labyrinth.spread_players(@players)
	  end

	  def next_player()
	    if @current_player_index == @players.size-1
		@current_player_index=0
	      else
		@current_player_index+=1
	      end
	      @current_player=@players[@current_player_index]
	  end

	  def actual_direction(preferred_direction)
	      current_row=@current_player.row
	      current_col=@current_player.col
	      valid_moves=@labyrinth.valid_moves(current_row,current_col)
	      output=@current_player.move(preferred_direction,valid_moves)
	      output
	  end

	  def combat(monster)
	      rounds=0
	      winner=GameCharacter::PLAYER
	      player_attack=@current_player.attack
	      lose=monster.defend(player_attack)
	      
	      while (!lose && rounds<@@MAX_ROUNDS)
			winner=GameCharacter::MONSTER
			rounds+=1
			monsterattack=monster.attack
			lose=@current_player.defend(monsterattack)

			if !lose
			playerattack=@current_player.attack
			winner=GameCharacter::PLAYER
			lose=monster.defend(playerattack)
			end
	      end

	      log_rounds(rounds,@@MAX_ROUNDS)
	      winner
	  end

	  def manage_reward(winner)
	    if winner==GameCharacter::PLAYER
			@current_player.receive_reward()
			log_player_won()
	    else
			log_monster_won()
	    end
	  end

	  def manage_resurrection()
	      resurrect = Dice.resurrect_player()
	      if resurrect
			@current_player.resurrect()
			fuzzy_player = FuzzyPlayer.new(@current_player)
			@players[@current_player_index] = fuzzy_player
			@labyrinth.set_fuzzy_player(@current_player.row, @current_player.col, fuzzy_player)
			@current_player = fuzzy_player
			log_resurrected()
	      else
			log_player_skip_turn()
	      end
	  end

	  def log_player_won()
	      @log=@log.to_s+" El jugador ha ganado el combate\n"
	  end

	  def log_monster_won()
	    @log=@log.to_s+" El monstruo ha ganado el combate\n"
	  end

	  def log_resurrected()
	    @log=@log.to_s+" El jugador ha resucitado\n"
	  end

	  def log_player_skip_turn()
	    @log=@log.to_s+" El jugador ha perdido el turno\n"
	  end

	  def log_player_no_orders()
	    @log=@log.to_s+" No se han seguido las instrucciones\n"
	  end

	  def log_no_monster()
	    @log=@log.to_s+ " No ha sido posible moverse o se ha movido a una celda vacía\n"
	  end

	  def log_rounds(rounds, max)
	    @log=@log.to_s+ " Se han producido un total de "+rounds.to_s+" rondas de "+max.to_s+" maximas rondas de combate\n"
	  end
	end


end
