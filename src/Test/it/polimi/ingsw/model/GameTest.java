package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
    private final Game game = new Game();
    private Player player1 = new Player("test1", God.CHARON, game);
    private Player player2 = new Player("test2", God.APOLLO, game);
    private Player player3 = new Player("test3", God.HEPHAESTUS, game);
    private Worker myWorker = new Worker(player1);
    private Worker oppWorker = new Worker(player2);
    private Worker otherWorker = new Worker(player3);


    //********************//
    //Test su isFreeToMove//
    //********************//

    @Test
    public void freeToMoveTrue() {
        myWorker.setSpace(game.getSpace(2, 2));
        game.getSpace(2, 2).setWorker(myWorker);
        assertTrue(game.isFreeToMove(myWorker));
        game.getConstraint().setAthena(true);
        assertTrue(game.isFreeToMove(myWorker));

        player1 = new Player("test1", God.APOLLO, game);
        myWorker = new Worker(player1);
        myWorker.setSpace(game.getSpace(2, 2));
        game.getSpace(2, 2).setWorker(myWorker);
        player2 = new Player("test2", God.CHARON, game);
        oppWorker = new Worker(player2);
        oppWorker.setSpace(game.getSpace(3, 2));
        game.getSpace(3, 2).setWorker(oppWorker);
        game.getConstraint().setAthena(false);
        game.getSpace(3, 1).setWorker(null);
        game.getSpace(2,1).setWorker(null);
        game.getSpace(1, 1).setWorker(null);
        game.getSpace(1, 2).setWorker(null);
        game.getSpace(1, 3).setWorker(null);
        game.getSpace(2, 3).setWorker(null);
        game.getSpace(3, 3).setWorker(null);
        game.getSpace(3, 1).setHeight(2);
        game.getSpace(2,1).setHeight(2);
        game.getSpace(1, 1).setHeight(2);
        game.getSpace(1, 2).setHeight(2);
        game.getSpace(1, 3).setHeight(2);
        game.getSpace(2, 3).setHeight(2);
        game.getSpace(3, 3).setHeight(2);

        assertTrue(game.isFreeToMove(myWorker));
    }

    @Test
    public void apolloNotFreeToMoveTrue() {
        player1 = new Player("test1", God.APOLLO, game);
        myWorker = new Worker(player1);
        myWorker.setSpace(game.getSpace(2, 2));
        game.getSpace(2, 2).setWorker(myWorker);
        player2 = new Player("test2", God.CHARON, game);
        oppWorker = new Worker(player2);
        oppWorker.setSpace(game.getSpace(3, 2));
        game.getSpace(3, 2).setWorker(oppWorker);
        game.getConstraint().setAthena(true);
        game.getSpace(3, 1).setWorker(null);
        game.getSpace(2,1).setWorker(null);
        game.getSpace(1, 1).setWorker(null);
        game.getSpace(1, 2).setWorker(null);
        game.getSpace(1, 3).setWorker(null);
        game.getSpace(2, 3).setWorker(null);
        game.getSpace(3, 3).setWorker(null);
        game.getSpace(3, 1).setHeight(1);
        game.getSpace(2,1).setHeight(1);
        game.getSpace(1, 1).setHeight(1);
        game.getSpace(1, 2).setHeight(1);
        game.getSpace(1, 3).setHeight(1);
        game.getSpace(2, 3).setHeight(1);
        game.getSpace(3, 3).setHeight(1);

        assertFalse(game.isFreeToMove(myWorker));
    }

    @Test
    public void prometheusFreeToMoveTrue() {
        player1 = new Player("test1", God.PROMETHEUS, game);
        myWorker = new Worker(player1);
        myWorker.setSpace(game.getSpace(2, 2));
        game.getSpace(2, 2).setWorker(myWorker);
        assertTrue(game.isFreeToMove(myWorker));
        game.getConstraint().setAthena(true);
        assertTrue(game.isPrometheusFreeToMove(myWorker));
    }

    @Test
    public void prometheusNotFreeToMoveTrue() {
        player1 = new Player("test1", God.PROMETHEUS, game);
        myWorker = new Worker(player1);
        myWorker.setSpace(game.getSpace(2, 2));
        game.getSpace(2, 2).setWorker(myWorker);
        game.getSpace(1, 1).setDome();
        game.getSpace(1, 2).setDome();
        game.getSpace(1, 3).setDome();
        game.getSpace(2, 1).setDome();
        game.getSpace(2, 3).setDome();
        game.getSpace(3, 1).setDome();
        game.getSpace(3, 2).setDome();
        game.getSpace(3, 3).setDome();
        assertFalse(game.isPrometheusFreeToMove(myWorker));
    }


    @Test
    public void notFreeToMoveTrue() {
        myWorker.setSpace(game.getSpace(2, 2));
        game.getSpace(2, 2).setWorker(myWorker);
        game.getSpace(1, 1).setDome();
        game.getSpace(1, 2).setDome();
        game.getSpace(1, 3).setDome();
        game.getSpace(2, 1).setDome();
        game.getSpace(2, 3).setDome();
        game.getSpace(3, 1).setDome();
        game.getSpace(3, 2).setDome();
        game.getSpace(3, 3).setDome();
        assertFalse(game.isFreeToMove(myWorker));

    }
    //*******************//
    //Test su charonPower//
    //*******************//

    @Test
    public void negativeXCharon() {
        myWorker.setSpace(game.getSpace(0, 0));
        oppWorker.setSpace(game.getSpace(1, 0));
        assertFalse(game.charonPower(myWorker, oppWorker));
    }

    @Test
    public void unboundedXCharon() {
        myWorker.setSpace(game.getSpace(4, 0));
        oppWorker.setSpace(game.getSpace(3, 0));
        assertFalse(game.charonPower(myWorker, oppWorker));
    }

    @Test
    public void negativeYCharon() {
        myWorker.setSpace(game.getSpace(0, 0));
        oppWorker.setSpace(game.getSpace(0, 1));
        assertFalse(game.charonPower(myWorker, oppWorker));
    }

    @Test
    public void unboundedYCharon() {
        myWorker.setSpace(game.getSpace(0, 4));
        oppWorker.setSpace(game.getSpace(0, 3));
        assertFalse(game.charonPower(myWorker, oppWorker));
    }

    @Test
    public void domedSpaceCharon() {
        myWorker.setSpace(game.getSpace(1, 1));
        oppWorker.setSpace(game.getSpace(1, 0));
        game.getSpace(1, 2).setDome();
        game.charonPower(myWorker, oppWorker);
        assertFalse(game.charonPower(myWorker, oppWorker));
    }

    @Test
    public void occupiedByWorkerSpaceCharon() {
        myWorker.setSpace(game.getSpace(1, 1));
        oppWorker.setSpace(game.getSpace(1, 0));
        otherWorker.setSpace(game.getSpace(1, 2));
        assertFalse(game.charonPower(myWorker, oppWorker));

        myWorker.setSpace(game.getSpace(1, 1));
        oppWorker.setSpace(game.getSpace(0, 0));
        otherWorker.setSpace(game.getSpace(2, 2));
        assertFalse(game.charonPower(myWorker, oppWorker));

        myWorker.setSpace(game.getSpace(1, 1));
        oppWorker.setSpace(game.getSpace(0, 2));
        otherWorker.setSpace(game.getSpace(2, 0));
        assertFalse(game.charonPower(myWorker, oppWorker));
    }

    @Test
    public void charonHasWorked() {
        myWorker.setSpace(game.getSpace(1, 1));
        oppWorker.setSpace(game.getSpace(1, 0));
        assertNotEquals(oppWorker.getSpace(), game.getSpace(1, 2));
        assertTrue(game.charonPower(myWorker, oppWorker));
        assertEquals(oppWorker.getSpace(), game.getSpace(1, 2));
        assertEquals(myWorker.getSpace(), game.getSpace(1, 1));
    }

    @Test
    public void charonHasWorked2() {
        myWorker.setSpace(game.getSpace(1, 1));
        oppWorker.setSpace(game.getSpace(2, 2));
        assertNotEquals(oppWorker.getSpace(), game.getSpace(0, 0));
        assertTrue(game.charonPower(myWorker, oppWorker));
        assertEquals(oppWorker.getSpace(), game.getSpace(0, 0));
        assertEquals(myWorker.getSpace(), game.getSpace(1, 1));
    }

    //isFreeToMove

    @Test
    public void isFreeToBuild() {
        //myWorker.setSpace(game.getSpace(5, 5));
        //game.getSpace(5, 5).setWorker(myWorker);
        //assertEquals(game.isFreeToBuild(myWorker),false);
        myWorker.setSpace(game.getSpace(2, 2));
        game.getSpace(2, 2).setWorker(myWorker);
        assertEquals(game.isFreeToBuild(myWorker), true);

        game.getSpace(1,1).setDome();
        game.getSpace(1,2).setDome();
        game.getSpace(1,3).setDome();
        game.getSpace(2,1).setDome();
        game.getSpace(3,1).setDome();
        game.getSpace(3,2).setDome();
        game.getSpace(3,3).setDome();
        game.getSpace(2,3).setDome();
        assertFalse(game.isFreeToBuild(myWorker));
    }

    //*********************//
    //Test su minotaurPower//
    //*********************//

    @Test
    public void negativeXMinotaur() {
        myWorker.setSpace(game.getSpace(1, 0));
        oppWorker.setSpace(game.getSpace(0, 0));
        assertFalse(game.minotaurPower(myWorker, oppWorker));
    }

    @Test
    public void unboundedXMinotaur() {
        myWorker.setSpace(game.getSpace(3, 0));
        oppWorker.setSpace(game.getSpace(4, 0));
        assertFalse(game.minotaurPower(myWorker, oppWorker));
    }

    @Test
    public void negativeYMinotaur() {
        myWorker.setSpace(game.getSpace(0, 1));
        oppWorker.setSpace(game.getSpace(0, 0));
        assertFalse(game.minotaurPower(myWorker, oppWorker));
    }

    @Test
    public void unboundedYMinotaur() {
        myWorker.setSpace(game.getSpace(0, 3));
        oppWorker.setSpace(game.getSpace(0, 4));
        assertFalse(game.minotaurPower(myWorker, oppWorker));
    }

    @Test
    public void domedSpaceMinotaur() {
        myWorker.setSpace(game.getSpace(0, 0));
        oppWorker.setSpace(game.getSpace(0, 1));
        game.getSpace(0, 2).setDome();
        assertFalse(game.minotaurPower(myWorker, oppWorker));
    }

    @Test
    public void occupiedSpaceByWorkerMinotaur() {
        myWorker.setSpace(game.getSpace(0, 0));
        oppWorker.setSpace(game.getSpace(0, 1));
        otherWorker.setSpace(game.getSpace(0, 2));
        assertFalse(game.minotaurPower(myWorker, oppWorker));

        oppWorker.setSpace(game.getSpace(1, 1));
        otherWorker.setSpace(game.getSpace(2, 2));
        assertFalse(game.minotaurPower(myWorker, oppWorker));

        oppWorker.setSpace(game.getSpace(1, 0));
        otherWorker.setSpace(game.getSpace(2, 0));
        assertFalse(game.minotaurPower(myWorker, oppWorker));

    }

    @Test
    public void minotaurHasWorked() {
        myWorker.setSpace(game.getSpace(0, 0));
        oppWorker.setSpace(game.getSpace(0, 1));
        assertNotEquals(oppWorker.getSpace(), game.getSpace(0, 2));
        assertTrue(game.minotaurPower(myWorker, oppWorker));
        assertEquals(oppWorker.getSpace(), game.getSpace(0, 2));
        assertEquals(myWorker.getSpace(), game.getSpace(0, 0));
    }

    @Test
    public void setterLiteGame() {
        // setPlayers()
        game.setPlayers(player1, player2, null);
        assertEquals(game.getLiteGame().getGod1(), player1.getGod());
        assertEquals(game.getLiteGame().getGod2(), player2.getGod());
        assertNull(game.getLiteGame().getGod3());
        game.setPlayers(player1, player2, player3);
        assertEquals(game.getLiteGame().getGod1(), player1.getGod());
        assertEquals(game.getLiteGame().getGod2(), player2.getGod());
        assertEquals(game.getLiteGame().getGod3(), player3.getGod());

        //setCurrPlayer()
        game.setCurrPlayer(player1);
        assertEquals(game.getLiteGame().getCurrPlayer(), "test1");

        //setCurrentWorker()
        player1.getWorker1().setSpace(new Space(5, 5));
        game.setCurrWorker(player1.getWorker1());
        assertEquals(game.getLiteGame().getCurrWorker()[0], 5);
        assertEquals(game.getLiteGame().getCurrWorker()[1], 5);
        game.setCurrWorker(null);
        assertNull(game.getLiteGame().getCurrWorker());

        //setWinner()
        game.setWinner(true);
        assertTrue(game.getLiteGame().isWinner());

        //refreshLiteGame()

        game.refreshLiteGame();
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++) {
                assertEquals(game.getLiteGame().getTable()[i][j], "V0N");
            }
    }

    @Test
    public void getterLiteGame() {
        // setPlayers()
        game.setPlayers(player1, player2, null);
        assertEquals(game.getLiteGame().getGod1(), player1.getGod());
        assertEquals(game.getLiteGame().getGod2(), player2.getGod());
        assertNull(game.getLiteGame().getGod3());
        game.setPlayers(player1, player2, player3);
        assertEquals(game.getLiteGame().getGod1(), player1.getGod());
        assertEquals(game.getLiteGame().getGod2(), player2.getGod());
        assertEquals(game.getLiteGame().getGod3(), player3.getGod());

        //setCurrPlayer()
        game.setCurrPlayer(player1);
        assertEquals(game.getLiteGame().getCurrPlayer(), "test1");

        //setCurrentWorker()
        player1.getWorker1().setSpace(new Space(1, 1));
        game.setCurrWorker(player1.getWorker1());

        //refreshLiteGame()

        game.refreshLiteGame();

        SerializableLiteGame serializableLiteGame = game.getLiteGame().makeSerializable();
        assertTrue(serializableLiteGame.isPerimetralSpace(new int[]{0, 0}));
        assertEquals(serializableLiteGame.getStringValue(0,0),"V0N");
        assertEquals(0, serializableLiteGame.getHeight(new int[]{1, 1}));
        assertEquals(1,serializableLiteGame.getCurrWorker()[0]);
        assertEquals(1,serializableLiteGame.getCurrWorker()[1]);
    }

    @Test
    public void cloneLiteGame() {
        game.refreshLiteGame();
        myWorker.setSpace(game.getSpace(0, 0));
        oppWorker.setSpace(game.getSpace(0, 1));
        otherWorker.setSpace(game.getSpace(0, 2));
        game.getLiteGame().setGod1(player1.getGod());
        game.getLiteGame().setGod2(player2.getGod());
        game.getLiteGame().setGod3(player3.getGod());
        game.getLiteGame().setName1("test1");
        game.getLiteGame().setName2("test2");
        game.getLiteGame().setName3("test3");
        game.getLiteGame().setCurrPlayer("test1");
        game.getLiteGame().setCurrWorker(0, 0);
        LiteGame test = new LiteGame();
        test = game.getLiteGame().cloneLG();
        assertEquals(game.getLiteGame().getName1(), test.getName1());
        assertEquals(game.getLiteGame().getName2(), test.getName2());
        assertEquals(game.getLiteGame().getName3(), test.getName3());
        assertEquals(game.getLiteGame().getCurrPlayer(), test.getCurrPlayer());
        assertEquals(game.getLiteGame().getCurrWorker()[0], test.getCurrWorker()[0]);
        assertEquals(game.getLiteGame().getCurrWorker()[1], test.getCurrWorker()[1]);

        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++) {
                assertEquals(game.getLiteGame().getTable()[i][j], test.getTable()[i][j]);
            }
    }

    @Test
    public void equalsLiteGame() {
        game.refreshLiteGame();
        myWorker.setSpace(game.getSpace(0, 0));
        oppWorker.setSpace(game.getSpace(0, 1));
        otherWorker.setSpace(game.getSpace(0, 2));
        game.getLiteGame().setGod1(player1.getGod());
        game.getLiteGame().setGod2(player2.getGod());
        game.getLiteGame().setName1("test1");
        game.getLiteGame().setName2("test2");
        game.getLiteGame().setCurrPlayer("test1");
        game.getLiteGame().setCurrWorker(0, 0);
        LiteGame test = new LiteGame();
        test = game.getLiteGame().cloneLG();
        assertTrue(test.equalsLG(game.getLiteGame()));

        game.getLiteGame().setName3("test3");
        game.getLiteGame().setGod3(player3.getGod());
        game.refreshLiteGame();
        test = game.getLiteGame().cloneLG();
        assertTrue(test.equalsLG(game.getLiteGame()));

        test.setName3("test4");
        assertFalse(test.equalsLG(game.getLiteGame()));

    }

    @Test
    public void makeSerializable() {
        game.refreshLiteGame();
        myWorker.setSpace(game.getSpace(0, 0));
        oppWorker.setSpace(game.getSpace(0, 1));
        otherWorker.setSpace(game.getSpace(0, 2));
        game.setLevel1(1);
        game.setLevel2(1);
        game.setLevel3(1);
        game.setDome(1);
        game.getLiteGame().setGod1(player1.getGod());
        game.getLiteGame().setGod2(player2.getGod());
        game.getLiteGame().setGod3(player3.getGod());
        game.getLiteGame().setName1("test1");
        game.getLiteGame().setName2("test2");
        game.getLiteGame().setName3("test3");
        game.getLiteGame().setCurrPlayer("test1");
        game.getLiteGame().setCurrWorker(0, 0);
        SerializableLiteGame test = new SerializableLiteGame();
        test = game.getLiteGame().makeSerializable();

        assertEquals(game.getLiteGame().getName1(), test.getName1());
        assertEquals(game.getLiteGame().getName2(), test.getName2());
        assertEquals(game.getLiteGame().getName3(), test.getName3());
        assertEquals(game.getLiteGame().getGod1(), test.getGod1());
        assertEquals(game.getLiteGame().getGod2(), test.getGod2());
        assertEquals(game.getLiteGame().getGod3(), test.getGod3());
        assertEquals(game.getLiteGame().getCurrPlayer(), test.getCurrPlayer());
        assertEquals(game.getLiteGame().getLevel1(), test.getLevel1());
        assertEquals(game.getLiteGame().getLevel2(), test.getLevel2());
        assertEquals(game.getLiteGame().getLevel3(), test.getLevel3());
        assertEquals(game.getLiteGame().getDome(), test.getDome());
        assertEquals(game.getLiteGame().isWinner(), test.isWinner());
        assertEquals(game.getLiteGame().getTable(), test.getTable());
    }

    @Test
    public void equalsSLG() {
        game.refreshLiteGame();
        myWorker.setSpace(game.getSpace(0, 0));
        oppWorker.setSpace(game.getSpace(0, 1));
        otherWorker.setSpace(game.getSpace(0, 2));
        game.getLiteGame().setGod1(player1.getGod());
        game.getLiteGame().setGod2(player2.getGod());
        game.getLiteGame().setGod3(player3.getGod());
        game.getLiteGame().setName1("test1");
        game.getLiteGame().setName2("test2");
        game.getLiteGame().setName3("test3");
        game.getLiteGame().setCurrPlayer("test1");
        game.getLiteGame().setCurrWorker(0, 0);
        SerializableLiteGame test = new SerializableLiteGame();
        test = game.getLiteGame().makeSerializable();

        game.getLiteGame().setName3("test4");
        assertFalse(test.equalsSLG(game.getLiteGame().makeSerializable()));
    }

    @Test
    public void equalsSLG2Players() {
        player3 = null;
        game.refreshLiteGame();
        myWorker.setSpace(game.getSpace(0, 0));
        oppWorker.setSpace(game.getSpace(0, 1));
        game.getLiteGame().setGod1(player1.getGod());
        game.getLiteGame().setGod2(player2.getGod());
        game.getLiteGame().setName1("test1");
        game.getLiteGame().setName2("test2");
        game.getLiteGame().setCurrPlayer("test1");
        game.getLiteGame().setCurrWorker(0, 0);
        SerializableLiteGame test = new SerializableLiteGame();
        test = game.getLiteGame().makeSerializable();

        assertTrue(test.equalsSLG(game.getLiteGame().makeSerializable()));
        game.getLiteGame().setGod1(null);
        test.setGod1(null);
        assertFalse(test.equalsSLG(game.getLiteGame().makeSerializable()));
    }

    @Test
    public void removedPlayer(){
        game.refreshLiteGame();
        SerializableLiteGame slg = game.getLiteGame().makeSerializable();
        slg.setCurrWorker(-1,0);
        assertNull(slg.getCurrWorker());
    }

    @Test
    public void testMoveWorkerPlayer() {
        myWorker.setSpace(game.getSpace(0, 0));
        assertTrue(myWorker.getPlayer().moveWorker(myWorker, game.getSpace(0,1)));
    }

    @Test
    public void testBuildWorkerPlayer() {
        myWorker.setSpace(game.getSpace(0, 0));
        assertTrue(myWorker.getPlayer().buildSpace(myWorker, game.getSpace(0,1),1));
    }

    @Test
    public void constraintsTest(){
        game.getConstraint().setHypnus(true);
        game.getConstraint().setAthena(true);
        assertTrue(game.getConstraint().athenaBlocks());
        assertTrue(game.getConstraint().hypnusBlocks());
    }

    @Test
    public void godTest(){
        assertEquals(God.TRITON.toString(),"TRITON");
        assertEquals(God.TRITON.getPower(),"Your Move: Each time your Worker moves into a perimeter space, it may immediately move again.");

    }

    @Test
    public void testGetOtherWorker() {
        game.setConstraint(game.getConstraint());
        assertEquals(player1.getWorker2(),player1.getOtherWorker(player1.getWorker1()));
    }

    @Test
    public void noLevel1ToBuild(){
        Space toBuild = game.getSpace(1,2);
        game.setLevel1(0);
        assertFalse(game.buildSwitch(toBuild,1,false));
    }

    @Test
    public void noLevel2ToBuild(){
        Space toBuild = game.getSpace(1,2);
        game.setLevel2(0);
        assertFalse(game.buildSwitch(toBuild,2,false));
    }

    @Test
    public void noLevel3ToBuild(){
        Space toBuild = game.getSpace(1,2);
        game.setLevel3(0);
        assertFalse(game.buildSwitch(toBuild,3,false));
    }

    @Test
    public void noDomeToBuild(){
        Space toBuild = game.getSpace(1,2);
        game.setDome(0);
        assertFalse(game.buildSwitch(toBuild,4,false));
    }

    @Test
    public void thereIsLevel3ToBuild(){
        Space toBuild = game.getSpace(1,2);
        game.setLevel3(20);
        assertTrue(game.buildSwitch(toBuild,3,false));
    }

    @Test
    public void thereIsDomeToBuild(){
        Space toBuild = game.getSpace(1,2);
        game.setDome(20);
        assertTrue(game.buildSwitch(toBuild,4,false));
    }

    @Test
    public void wrongLevelToBuild(){
        Space toBuild = game.getSpace(1,2);
        assertFalse(game.buildSwitch(toBuild,5,false));
    }

    @Test
    public void wrongSpaceToGet(){
        assertNull(game.getSpace(5,1));
    }


    @Test
    public void getWorker2Test() {
        assertEquals(player1.getWorker1(),player1.getOtherWorker(player1.getWorker2()));
    }

    @Test
    public void hypnusIsMyGod() {
        player1.setGod(God.HYPNUS);
        assertTrue(game.getConstraint().hypnusBlocks());
    }

    @Test
    public void nullSpaceToSet(){
        myWorker.setSpace(null);
        assertNull(myWorker.getSpace());
    }
}