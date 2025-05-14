
module Irrgarten
    class LabyrinthCharacter
        @@INVALID_POS = -1;
    
        def initialize(name, intelligence, strength, health)
            @name = name
            @intelligence = intelligence
            @strength = strength
            @health = health
            
            set_pos(@@INVALID_POS, @@INVALID_POS)
        end
        
        def copia(other)
            @name = other.get_name
            @intelligence = other.get_intelligence
            @strength = other.get_strength
            @health = other.get_health
            @row = other.get_row
            @col = other.get_col
        end
        
        protected

        def get_name
            @name
        end
        
        def get_intelligence
            @intelligence
        end
        
        def get_strength 
            @strength
        end
        
        def get_health
            @health
        end
        
        def set_health(health)
            @health = health
        end

        def got_wounded
            @health -= 1
        end

        public 

        def dead
            @health <= 0;
        end

        def get_row
            @row
        end
        
        def get_col
            @col
        end
        
        def set_pos(row, col)
            @row = row
            @col = col
        end
        
        def to_s
            "N: #{@name}, I: #{@intelligence}, S: #{@strength}, H: #{@health}, R: #{@row}, C: #{@col}\n"
        end
        
        #Se implementan de manera independiente en las clases player y monster
        #def attack
        #    raise NotImplementedError, "#{self.class} has not implemented method "
        #end
        
        #def defend (attack)
        #    raise NotImplementedError, "#{self.class} has not implemented method " 
        #end
    
    end 
end

    
