package com.org.kalah.service;

import com.org.kalah.domain.Game;
import com.org.kalah.domain.Player;
import com.org.kalah.domain.Status;
import com.org.kalah.exceptions.InvalidPitNumberException;
import com.org.kalah.exceptions.KalahException;
import com.org.kalah.service.impl.KalahServiceHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class KalahServiceHelperTest {

    @InjectMocks
    private KalahServiceHelper kalahServiceHelper;

    @Value("${kalah.game.url}")
    private String kalahURL;

    Map<Integer, Integer> boardStatus = null;
    Game game = null;

    @Before
    public void init(){
        boardStatus = Stream.of(new Integer[][] {
                { 1, 6}, { 2, 6 },{ 3, 6 },{ 4, 6 },{ 5, 6 },{ 6, 6 },{ 7, 0 },{ 8, 6 },{ 9, 6 },{ 10, 6 },{ 11, 6 },{ 12, 6 },{ 13, 6 },{ 14, 0 }
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        game = new Game();
    }

    @Test
    public void addStonesIfLastOwnPitEmptyTest(){
        boardStatus.replace(2,1);
        game.setKalahBoard(boardStatus);
        kalahServiceHelper.addStonesIfLastOwnPitEmpty(2,game);
        assertEquals(7,boardStatus.get(7).intValue());
    }

    @Test
    public void gameIsTerminatedTest(){
        boardStatus.replace(1,0);
        boardStatus.replace(2,0);
        boardStatus.replace(3,0);
        boardStatus.replace(4,0);
        boardStatus.replace(5,0);
        boardStatus.replace(6,0);
        boardStatus.replace(7,4);
        game.setKalahBoard(boardStatus);
        assertTrue(kalahServiceHelper.gameIsTerminated(game));
    }

    @Test
    public void gameIsNotTerminatedTest(){
        assertFalse(kalahServiceHelper.gameIsTerminated(game));
    }

    @Test
    public void firtPlayerWinnerTest(){
        boardStatus.replace(7,38);
        boardStatus.replace(14,34);
        game.setKalahBoard(boardStatus);
        assertEquals(Status.FIRST_PLAYER_WIN,kalahServiceHelper.findTheWinner(game));
    }

    @Test
    public void secondPlayerWinnerTest(){
        boardStatus.replace(7,34);
        boardStatus.replace(14,38);
        game.setKalahBoard(boardStatus);
        assertEquals(Status.SECOND_PLAYER_WIN,kalahServiceHelper.findTheWinner(game));
    }
    @Test
    public void drawGameTest(){
        boardStatus.replace(7,36);
        boardStatus.replace(14,36);
        game.setKalahBoard(boardStatus);
        assertEquals(Status.DRAW,kalahServiceHelper.findTheWinner(game));
    }

    @Test
    public void successfulValidatePitNumberTest(){
        kalahServiceHelper.validatePitNumber(2,game);
        assertEquals(game.getKalahBoard().size(),14);
    }

    @Test
    public void negativeTestKalahPitNumberForMakeMove(){
        Assertions.assertThrows(InvalidPitNumberException.class, () -> {
            kalahServiceHelper.validatePitNumber(7,game);
            kalahServiceHelper.validatePitNumber(14,game);
        });
    }

    @Test
    public void negativeTestOutOfBoundPitNumberForMakeMove(){
        Assertions.assertThrows(InvalidPitNumberException.class, () -> {
            kalahServiceHelper.validatePitNumber(15,game);
        });
    }

    @Test
    public void negativeTestWrongPlayerForMakeMove(){
        Assertions.assertThrows(InvalidPitNumberException.class, () -> {
            kalahServiceHelper.validatePitNumber(9,game);
        });
    }

    @Test
    public void negativeTestEmptyPitValueNumberForMakeMove(){
        boardStatus.replace(1,0);
        game.setKalahBoard(boardStatus);
        Assertions.assertThrows(InvalidPitNumberException.class, () -> {
            kalahServiceHelper.validatePitNumber(1,game);
        });
    }

    @Test
    public void lastPitWasOwnEmptyPitTest(){
        boardStatus.replace(1,1);
        game.setKalahBoard(boardStatus);
        assertTrue(kalahServiceHelper.lastPitWasOwnEmptyPit(1,game));
    }

    @Test
    public void lastPitWasNotOwnEmptyPitTest(){
        boardStatus.replace(11,1);
        game.setKalahBoard(boardStatus);
        assertFalse(kalahServiceHelper.lastPitWasOwnEmptyPit(11,game));
    }

    @Test
    public void checkIfGameStatusIsInProgressTest(){
        kalahServiceHelper.checkIfGameStatusIsInProgress(game);
        assertTrue(game.getStatus().equals(Status.IN_PROGRESS));
    }

    @Test
    public void negativeTestIfGameStatusIsInNotInProgress(){
        game.setStatus(Status.FIRST_PLAYER_WIN);
        Assertions.assertThrows(KalahException.class, () -> {
            kalahServiceHelper.checkIfGameStatusIsInProgress(game);
        });
    }

    @Test
    public void isUserPitTest(){
        assertTrue(kalahServiceHelper.isUserPit(1,game.getPlayer()));
    }

    @Test
    public void getOppositePitTest(){
        assertEquals(13,kalahServiceHelper.getOppositePit(1));
    }

    @Test
    public void playerWonAnotherTurnTest(){
        assertTrue(kalahServiceHelper.playerWonAnotherTurn(7,game.getPlayer()));
    }

    @Test
    public void addStonesToPitTest(){
        kalahServiceHelper.addStonesToPit(1,boardStatus,1);
        assertEquals(7,boardStatus.get(1).intValue());
    }

    @Test
    public void emptyPitTest(){
        kalahServiceHelper.emptyPit(1,boardStatus);
        assertEquals(0,boardStatus.get(1).intValue());
    }


    @Test
    public void addAllRemainedStonesToKalahTest(){
        Player player = game.getPlayer();
        kalahServiceHelper.addAllRemainedStonesToKalah(player,boardStatus);
        assertEquals(36,boardStatus.get(7).intValue());
    }


    @Test
    public void performMoveTest(){
        kalahServiceHelper.performMove(new Game(),1);
    }
}
