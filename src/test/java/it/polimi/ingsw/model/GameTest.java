package it.polimi.ingsw.model;

import org.junit.Test;

public class GameTest {
    @Test
    public void ConstructorTest(){
        Game game = new Game();
        assert(game.getNumPlayers() == -1);
        assert(!game.numPlayersIsSet());
    }
    @Test
    public void ConstructorWithParamsTest(){
        Game game = new Game(3);
        assert(game.getNumPlayers() == 3);
        assert(game.numPlayersIsSet());
    }
}
