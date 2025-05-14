  require_relative '../irrgarten/game'
  require_relative '../UI/text_UI'
  require_relative '../controller/controller'

    juego=Irrgarten::Game.new(1)
    vista=UI::TextUI.new
    c=Control::Controller.new(juego,vista)
    c.play
