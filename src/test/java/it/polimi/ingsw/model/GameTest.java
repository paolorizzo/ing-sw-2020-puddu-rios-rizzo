package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameTest {
    @Test
    public void ConstructorsTest(){
        Model model = new Model();
        Game game = new Game(model);
        assert(game.getNumPlayers() == -1);
        assert(!game.numPlayersIsSet());

        Game game2 = new Game(model,3);
        assert(game2.getNumPlayers() == 3);
        assert(game2.numPlayersIsSet());
    }
    @Test
    public void numPlayersTest(){
        Model model = new Model();
        Game game = model.game;

        game.setNumPlayers(2);
        assert(game.getNumPlayers() == 2);

    }

    @Test
    public void currentPlayerTest(){
        Model model = new Model();
        Game game = model.game;

        game.setNumPlayers(3);

        assert(game.getCurrentPlayerId() == 1);
        game.nextTurn();
        assert(game.getCurrentPlayerId() == 2);

        game.nextTurn();
        assert(game.getCurrentPlayerId() == 0);

    }

    @Test
    public void cardsTest(){
        Model model = new Model();
        Game game = model.game;

        game.setNumPlayers(3);

        assert(!game.cardsAlreadyChosen());
        assert(!game.isCardPickable(1));

        game.setChosenCards(Arrays.asList(1, 2, 3));

        assert(game.cardsAlreadyChosen());
        assert(game.isCardPickable(1));
        assert(game.isCardPickable(2));
        assert(game.isCardPickable(3));

        Card card1 = game.getChosenCard(1);
        game.removeChosenCard(card1);
        assert(!game.isCardPickable(1));

    }


    @Test
    public void setupTest(){
        Model model = new Model();
        Game game = model.game;

        game.setNumPlayers(3);

        model.addPlayer(new Player(0, "Paolo"));
        model.addPlayer(new Player(1, "Fra"));
        model.addPlayer(new Player(2, "Fede"));

        game.setChosenCards(Arrays.asList(1, 2, 3));

        model.setCardPlayer(0, 1);
        model.setCardPlayer(1, 2);
        model.setCardPlayer(2, 3);

        //id 1 turn

        assert(game.getPossibleSetupActions(0) == null);
        assert(game.getPossibleSetupActions(2) == null);
        assert(game.getPossibleSetupActions(1) != null);

        List<Action> possibleActions = game.getPossibleSetupActions(1);
        Action setup1 = possibleActions.get(0);
        model.executeSetupAction(1, (SetupAction)setup1);

        possibleActions = game.getPossibleSetupActions(1);
        assert(!game.possibleActionsContains(setup1));
        Action setup2 = possibleActions.get(0);
        model.executeSetupAction(1, (SetupAction)setup2);


        assert(game.turnArchive.getLastTurnOf(model.getPlayers().get(1))!=null);
        assert(game.turnArchive.getLastTurnOf(model.getPlayers().get(1)).getActions().size() == 2);
        assert(game.turnArchive.getLastTurnOf(model.getPlayers().get(1)).getActions().get(0).equals(setup1));
        assert(game.turnArchive.getLastTurnOf(model.getPlayers().get(1)).getActions().get(1).equals(setup2));

        //id 2 turn
        assert(game.getCurrentPlayerId() == 2);
        assert(!game.possibleActionsContains(setup1));
        assert(!game.possibleActionsContains(setup2));
        assert(game.getPossibleSetupActions(0) == null);
        assert(game.getPossibleSetupActions(1) == null);
        assert(game.getPossibleSetupActions(2) != null);

        possibleActions = game.getPossibleSetupActions(2);
        setup1 = possibleActions.get(0);

        model.executeSetupAction(2, (SetupAction)setup1);

        possibleActions = game.getPossibleSetupActions(2);
        assert(!game.possibleActionsContains(setup1));
        setup2 = possibleActions.get(0);
        model.executeSetupAction(2, (SetupAction)setup2);

        assert(game.turnArchive.getLastTurnOf(model.getPlayers().get(2))!=null);
        assert(game.turnArchive.getLastTurnOf(model.getPlayers().get(2)).getActions().size() == 2);
        assert(game.turnArchive.getLastTurnOf(model.getPlayers().get(2)).getActions().get(0).equals(setup1));
        assert(game.turnArchive.getLastTurnOf(model.getPlayers().get(2)).getActions().get(1).equals(setup2));

        //id 0 turn
        assert(game.getCurrentPlayerId() == 0);
        assert(!game.possibleActionsContains(setup1));
        assert(!game.possibleActionsContains(setup2));
        assert(game.getPossibleSetupActions(0) != null);
        assert(game.getPossibleSetupActions(1) == null);
        assert(game.getPossibleSetupActions(2) == null);

        possibleActions = game.getPossibleSetupActions(0);
        setup1 = possibleActions.get(0);
        model.executeSetupAction(0, (SetupAction)setup1);

        possibleActions = game.getPossibleSetupActions(0);
        assert(!game.possibleActionsContains(setup1));
        setup2 = possibleActions.get(0);
        model.executeSetupAction(0, (SetupAction)setup2);

        assert(game.turnArchive.getLastTurnOf(model.getPlayers().get(0))!=null);
        assert(!game.possibleActionsContains(setup1));
        assert(!game.possibleActionsContains(setup2));
        assert(game.turnArchive.getLastTurnOf(model.getPlayers().get(0)).getActions().size() == 2);
        assert(game.turnArchive.getLastTurnOf(model.getPlayers().get(0)).getActions().get(0).equals(setup1));
        assert(game.turnArchive.getLastTurnOf(model.getPlayers().get(0)).getActions().get(1).equals(setup2));
    }
    @Test
    public void gameTest(){
        Random rand = new Random(0);
        final int N_TEST = 100;
        for(int i=0;i<N_TEST;i++){
            playRandomCompleteGameTest(rand.nextInt(2)+2, i);
        }
    }
    public void playRandomCompleteGameTest(int numPlayers, int seed){
        System.out.println("game "+numPlayers+" "+seed);
        Random rand = new Random(seed);
        Model model = new Model();
        Game game = model.game;

        game.setNumPlayers(numPlayers);

        model.addPlayer(new Player(0, "Paolo"));
        model.addPlayer(new Player(1, "Fra"));
        if(numPlayers == 3)
            model.addPlayer(new Player(2, "Fede"));

        Deck deck = game.getDeck();
        List<Integer> cards = new ArrayList<>();
        for(int i=0;i<numPlayers;i++){
            int num = deck.getCards().get(rand.nextInt(deck.getCards().size())).getNum();
            while(cards.contains(new Integer(num))){
                num = deck.getCards().get(rand.nextInt(deck.getCards().size())).getNum();
            }
            cards.add(num);
        }

        game.setChosenCards(cards);
        assert (game.getChosenCards().size() == cards.size());
        for(int i=0;i<game.getChosenCards().size();i++){
            assert(cards.contains(game.getChosenCards().get(i).getNum()));
        }

        System.out.println("P0 card: "+cards.get(0));
        model.setCardPlayer(0, cards.get(0));

        System.out.println("P1 card: "+cards.get(1));
        model.setCardPlayer(1, cards.get(1));
        if(numPlayers == 3){
            System.out.println("P2 card: "+cards.get(2));
            model.setCardPlayer(2, cards.get(2));
        }


        //id 1 setup turn
        List<Action> possibleActions = game.getPossibleSetupActions(1);
        Action setup1 = possibleActions.get(rand.nextInt(possibleActions.size()));
        model.executeSetupAction(1, (SetupAction)setup1);

        possibleActions = game.getPossibleSetupActions(1);
        Action setup2 = possibleActions.get(rand.nextInt(possibleActions.size()));
        model.executeSetupAction(1, (SetupAction)setup2);

        //id 2 setup turn
        if(numPlayers == 3){
            possibleActions = game.getPossibleSetupActions(2);
            setup1 = possibleActions.get(rand.nextInt(possibleActions.size()));
            model.executeSetupAction(2, (SetupAction)setup1);

            possibleActions = game.getPossibleSetupActions(2);
            setup2 = possibleActions.get(rand.nextInt(possibleActions.size()));
            model.executeSetupAction(2, (SetupAction)setup2);
        }


        //id 0 setup turn

        possibleActions = game.getPossibleSetupActions(0);
        setup1 = possibleActions.get(rand.nextInt(possibleActions.size()));
        model.executeSetupAction(0, (SetupAction)setup1);

        possibleActions = game.getPossibleSetupActions(0);
        setup2 = possibleActions.get(rand.nextInt(possibleActions.size()));
        model.executeSetupAction(0, (SetupAction)setup2);

        System.out.println("End setup");

        //START GAME HERE
        while(!game.isFinish()){
            int id = game.getCurrentPlayerId();
            System.out.println(id+" TURN");
            possibleActions = game.getPossibleActions(id);
            List<Action> actions = new ArrayList<>();
            while(possibleActions != null){
                if(game.isEndOfTurnPossible() && rand.nextInt(2)==1){
                    System.out.println("I want to end my turn");
                    game.nextTurn();
                    break;
                }
                actions.add(possibleActions.get(0));
                assert(game.possibleActionsContains(actions.get(actions.size()-1)));
                System.out.println(actions.get(actions.size()-1));
                model.executeAction(id, actions.get(actions.size()-1));
                possibleActions = game.getPossibleActions(id); // the last try change current player id and add turn
            }
            assert(game.getPossibleActions(id)==null);
            assert(game.isFinish() || game.getCurrentPlayerId() != id);
            for(int i=0;i<actions.size();i++)
                assert(game.turnArchive.getLastTurnOf(model.getPlayers().get(id)).getActions().get(i).equals(actions.get(i)));
        }

    }
}
