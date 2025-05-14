#encoding: utf-8
require_relative 'dice'
require_relative 'directions'
require_relative 'orientation'


module Irrgarten
class Labyrinth
  @@BLOCK_CHAR = 'X'
  @@EMPTY_CHAR = '-'
  @@MONSTER_CHAR = 'M'
  @@COMBAT_CHAR = 'C'
  @@EXIT_CHAR = 'E'
  @@ROW = 0
  @@COL = 1

  def initialize(n_rows, n_cols, exit_row, exit_col)
    @n_rows = n_rows
    @n_cols = n_cols
    @exit_row = exit_row
    @exit_col = exit_col
    @monsters=Array.new(@n_rows) {Array.new(@n_cols)}
    @labyrinth = Array.new(@n_rows) { Array.new(@n_cols, @@EMPTY_CHAR) }
    @players=Array.new(@n_rows) {Array.new(@n_cols)}
    
    @labyrinth[exit_row][exit_col] = @@EXIT_CHAR
  end

  def spread_players(players) 
    players.each do |p|
    	pos = random_empty_pos()
      old_row = -1
      old_col = -1
    	put_player_2D(old_row, old_col, pos[@@ROW], pos[@@COL], p)
    end
  end

  def have_a_winner()
      ganador=false
      if @players[@exit_row][@exit_col]!= nil
        ganador=true
      end
      ganador
  end

  def to_s()
      lab=""
      (0...@n_rows).each do |fil|
        (0...@n_cols).each do |col|
          lab=lab+@labyrinth[fil][col].to_s()
          lab=lab + " "
        end
        lab=lab + "\n"
      end
      lab
  end

  def add_monster(row, col, monster)
     if empty_pos(row, col) && pos_ok(row, col)
      @labyrinth[row][col]=@@MONSTER_CHAR
      @monsters[row][col]=monster
      monster.set_pos(row,col)
    end
  end


  def put_player(direction, player) 
     old_row = player.row
     old_col = player.col
     
     new_pos = dir_2_pos(old_row, old_col, direction)
     monster = put_player_2D(old_row, old_col, new_pos[@@ROW],new_pos[@@COL], player)
     monster
  end



  def add_block(orientation, start_row, start_col, length) 
    if (orientation == Orientation::VERTICAL)
    	inc_row = 1
    	inc_col = 0
    else
    	inc_row = 0
    	inc_col = 1
    end
    
    row = start_row
    col = start_col
    
    while (pos_ok(row, col) && empty_pos(row, col) && length > 0)
    	@labyrinth[row][col] = @@BLOCK_CHAR
    	length -= 1
    	row += inc_row
    	col += inc_col
    end	
  end



  def valid_moves(row, col) 
    output = Array.new
    if can_step_on(row + 1,col)
        output << Directions::DOWN
    end
    if can_step_on(row - 1,col)
        output << Directions::UP
    end
    if can_step_on(row,col + 1)
        output << Directions::RIGHT
    end
    if can_step_on(row,col - 1)
        output << Directions::LEFT
    end
    output
  end

  #Metodo que uso en el manage _resurrection para poner el 
  #fuzzy_player en el laberinto
  def set_fuzzy_player(row, col, fuzzy)
  	@players[row][col] = fuzzy
  end


  private



  def pos_ok(row, col)
    (row>=0 && row<@n_rows) && (col>=0 && col<@n_cols)
  end



  def empty_pos(row, col)
    @labyrinth[row][col]==@@EMPTY_CHAR
  end



  def monster_pos(row, col)
    @monsters[row][col]!=nil
  end



  def exit_pos(row, col)
    @labyrinth[row][col]==@@EXIT_CHAR
  end



  def combat_pos(row, col)
    @labyrinth[row][col]==@@COMBAT_CHAR
  end



  def can_step_on(row, col)
    pos_ok(row,col) && (empty_pos(row,col)||monster_pos(row,col)||exit_pos(row,col))
  end



  def update_old_pos(row, col)
      if pos_ok(row,col)
        if combat_pos(row,col)
          @labyrinth[row][col]=@@MONSTER_CHAR
        else
          @labyrinth[row][col]=@@EMPTY_CHAR
        end
      end
  end



  def dir_2_pos(row, col, direction)
      nueva_fila=row
      nueva_columna=col
      if direction==Directions::UP
        nueva_fila-=1
      end
      if direction==Directions::DOWN
        nueva_fila+=1
      end
      if direction==Directions::LEFT
        nueva_columna-=1
      end
      if direction==Directions::RIGHT
        nueva_columna+=1
      end
      [nueva_fila,nueva_columna]
  end



  def random_empty_pos()
    row = Dice.random_pos(@n_rows)
    col = Dice.random_pos(@n_cols)
    while(!empty_pos(row,col))
      row = Dice.random_pos(@n_rows)
      col = Dice.random_pos(@n_cols)
    end
    return [row, col]
  end



    def put_player_2D(old_row, old_col, row, col, player)
      output=nil
        if can_step_on(row,col)
          if pos_ok(old_row,old_col)
            p=@players[old_row][old_col]
            if p==player
              update_old_pos(old_row,old_col)
              @players[old_row][old_col]=nil
            end
          end
          monsterpos=monster_pos(row,col)
          if monsterpos
            @labyrinth[row][col]=@@COMBAT_CHAR
            output=@monsters[row][col]
          else
            number=player.number
            @labyrinth[row][col]=number
          end
            @players[row][col]=player
            player.set_pos(row,col)
        end
        output
    end

    #Tampoco es necesario esto de aquí
    private :pos_ok, :empty_pos,
    :monster_pos, :exit_pos, :combat_pos, 
    :can_step_on, :update_old_pos, :dir_2_pos, 
    :random_empty_pos, :put_player_2D
  end
end
