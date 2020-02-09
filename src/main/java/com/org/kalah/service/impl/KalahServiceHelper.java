package com.org.kalah.service.impl;


import com.org.kalah.config.KalahGameConfiguration;
import com.org.kalah.controller.KalahController;
import com.org.kalah.domain.Game;
import com.org.kalah.domain.Player;
import com.org.kalah.domain.Status;
import com.org.kalah.exceptions.InvalidPitNumberException;
import com.org.kalah.exceptions.KalahException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;





/**
 * @author Mithun Kini
 *
 * This class conatins all the business functionalities pertaining to Kalah game.
 */
@Service
public class KalahServiceHelper {

    private static final Logger logger = LoggerFactory.getLogger(KalahController.class);


    /**
     *
     * This method checks the game status, validates the pit number, performs move and declares the winner/draw.
     *
     * @param game
     * @param pitId
     * @throws Exception
     */
    public void performMove(Game game, int pitId) {
        logger.info("Inside KalahServiceHelper.performMove with game id "+game.getGameId() +" and pit id "+pitId );
        logger.debug("Inside KalahServiceHelper.performMove with game details "+game +" and pit id "+pitId );
        checkIfGameStatusIsInProgress(game);
        validatePitNumber(pitId, game);

        Map<Integer, Integer> kalahBoard = game.getKalahBoard();
        int previousPosition = pitId + kalahBoard.get(pitId);
        int previousPit = previousPosition;
        emptyPit(pitId, game.getKalahBoard());

        for (int currentPosition = pitId + 1; currentPosition <= previousPosition; currentPosition++) {
            int currentPit = currentPosition;
            if (currentPosition == KalahGameConfiguration.LAST_PIT_INDEX && previousPosition != KalahGameConfiguration.LAST_PIT_INDEX) {
                previousPosition = previousPosition - currentPosition;
                currentPosition = 0;
            }

            //Check if Current pit belongs to opposite player's Kalah pit
            if (game.getPlayer().getOppositePlayer().getKalahId() != currentPit) {
                logger.trace("Current pit does not belongs to opposite player's Kalah pit, so adding stone to current pit" );
                addStonesToPit(currentPit, kalahBoard, 1);
            } else {
                logger.trace("Current pit belongs to opposite player's Kalah pit" );
                if (currentPosition != KalahGameConfiguration.LAST_PIT_INDEX) {
                    previousPosition++;
                }else {
                    previousPosition = KalahGameConfiguration.FIRST_PIT_INDEX;
                    currentPosition = 0;
                }
            }
        }
        previousPit = previousPit > KalahGameConfiguration.LAST_PIT_INDEX ? previousPosition : previousPit;
        //Check the lastpit to identify if it was user's own pit and landed to an empty pit
        addStonesIfLastOwnPitEmpty(previousPit, game);

        if (!playerWonAnotherTurn(previousPit, game.getPlayer())) {
            game.setPlayer(game.getPlayer().getOppositePlayer());
        }
        if (gameIsTerminated(game)) {
            Status winner = findTheWinner(game);
            game.setStatus(winner);
        }
        logger.info("Completed KalahServiceHelper.performMove " );
    }

    /**
     *
     * Checks if the last stone landed in an empty pit which belongs to a current
     * player. If true then it collects a stone from the last pit and the opposite
     * pit and put them in current player Kalah
     *
     * @param lastPit
     * @param game
     */
    public void addStonesIfLastOwnPitEmpty(int lastPit, Game game) {
        logger.info("Inside KalahServiceHelper.checkIfLastOwnPit with game id "+game.getGameId() +" and last pit "+lastPit );
        logger.debug("Inside KalahServiceHelper.checkIfLastOwnPit with game details "+game +" and last pit "+lastPit );
        if (lastPitWasOwnEmptyPit(lastPit, game)) {
            logger.trace("Last pit was own empty pit");
            int oppositePit = getOppositePit(lastPit);
            int oppositePitAmount = game.getKalahBoard().get(oppositePit);
            if (oppositePitAmount != 0) {
                emptyPit(oppositePit, game.getKalahBoard());
                emptyPit(lastPit, game.getKalahBoard());
                addStonesToPit(game.getPlayer().getKalahId(), game.getKalahBoard(), oppositePitAmount + 1);
            }
        }
        logger.info("Completed KalahServiceHelper.checkIfLastOwnPit");
    }

    /**
     * This method checks if all the pits except Kalah of either player is
     * empty or not. If empty then it collects all the stones of the respective player's pit
     * and put in the respective player's Kalah
     *
     * @param game
     * @return true if the game is terminated.
     */
    public boolean gameIsTerminated(Game game) {
        logger.info("Inside KalahServiceHelper.gameIsTerminated with game id "+game.getGameId() );
        logger.debug("Inside KalahServiceHelper.checkLastPit with game details "+game  );
        Player player = game.getPlayer();
        List<Integer> pits = player.getPits();
        Map<Integer, Integer> board = game.getKalahBoard();

        boolean playerPitsAreEmpty = pits.stream()
                .map(board::get)
                .allMatch(stoneNumbers -> stoneNumbers == 0);
        logger.trace("Is current palyer's pit is empty "+playerPitsAreEmpty);

        boolean oppositePlayerPitsAreEmpty = player.getOppositePlayer().getPits().stream()
                .map(board::get)
                .allMatch(stoneNumbers -> stoneNumbers == 0);
        logger.trace("Is opposite palyer's pit is empty "+oppositePlayerPitsAreEmpty);

        //If empty then it collects all the stones of the respective player's pit and put in the respective player Kalah.
        if (playerPitsAreEmpty || oppositePlayerPitsAreEmpty) {
            addAllRemainedStonesToKalah(player, board);
            addAllRemainedStonesToKalah(player.getOppositePlayer(), board);
            logger.info("Completed KalahServiceHelper.gameIsTerminated with response true" );
            return true;
        }
        logger.info("Completed KalahServiceHelper.gameIsTerminated with response false" );
        return false;
    }

    /**
     * This method collects stones from all the pits of the respective player
     * and accumulates in the respective players's Kalah
     *
     * @param player
     * @param board
     */
    public void addAllRemainedStonesToKalah(Player player, Map<Integer, Integer> board ) {
        logger.info("Inside KalahServiceHelper.addAllRemainedStonesToKalah " );
        logger.debug("Inside KalahServiceHelper.addAllRemainedStonesToKalah with player details "+player+" and board +"+board  );
        player.getPits().forEach(pit -> {
            int amount = board.get(pit);
            if (amount != 0) {
                int kalahId = player.getKalahId();
                board.replace(kalahId, board.get(kalahId) + amount);
                emptyPit(pit, board);
            }
        });
        logger.debug("Completed KalahServiceHelper.addAllRemainedStonesToKalah. Board details "+board  );
        logger.info("Completed KalahServiceHelper.addAllRemainedStonesToKalah " );
    }

    /**
     *
     * This method  counts all the stones present in the respective players Kalah
     * and declares with either of the follwoing status
     * FIRST_PLAYER_WIN, SECOND_PLAYER_WIN, DRAW
     *
     * @param game
     * @return
     */
    public Status findTheWinner(Game game) {
        logger.info("Inside KalahServiceHelper.findTheWinner with game id "+game.getGameId() );
        logger.debug("Inside KalahServiceHelper.findTheWinner with game details "+game );

        Map<Integer, Integer> board = game.getKalahBoard();
        int firstPlayerStones = board.get(Player.FIRST_PLAYER.getKalahId());
        int secondPlayerStones = board.get(Player.SECOND_PLAYER.getKalahId());
        logger.trace("First player stones "+firstPlayerStones+ " and Second player stones "+secondPlayerStones);

        if (firstPlayerStones > secondPlayerStones) {
            logger.trace("First palyer is the winner" );
            return Status.FIRST_PLAYER_WIN;
        }else if (firstPlayerStones < secondPlayerStones) {
            logger.trace("Second palyer is the winner" );
            return Status.SECOND_PLAYER_WIN;
        }else {
            logger.trace("Game end in a draw" );
            return Status.DRAW;
        }
    }

    /**
     *
     * This method validates the following
     * Checks if pit number is other than Kalah pit.
     * Checks if pit number is between 1 to 14.
     * Checks if the pit number belongs to current player or not to validate the player's turn.
     * Cheks if pit empty or not.
     *
     * @param pitId
     * @param game
     */
    public void validatePitNumber(int pitId, Game game) {
        logger.info("Inside KalahServiceHelper.validatePitNumber with game id "+game.getGameId() );
        logger.debug("Inside KalahServiceHelper.validatePitNumber with game details "+game+" and pit id "+pitId );
        Player player = game.getPlayer();
        if (pitId == player.getKalahId() || pitId == player.getOppositePlayer().getKalahId()) {
            logger.error("Invalid Pit id "+pitId);
            throw new InvalidPitNumberException("Please select pit number other than Kalah pit.");
        }

        if (pitId < KalahGameConfiguration.FIRST_PIT_INDEX || pitId > KalahGameConfiguration.LAST_PIT_INDEX) {
            logger.error("Invalid Pit id "+pitId);
            throw new InvalidPitNumberException("Please select pit number between 1 to 14");
        }

        if (!isUserPit(pitId, player)) {
            logger.error("Invalid Pit id "+pitId+" since its "+player.name()+"'s turn");
            throw new InvalidPitNumberException("It is "+player.name()+"'s turn");
        }
        if (game.getKalahBoard().get(pitId) == 0) {
            logger.error("Invalid Pit id "+pitId+" since pit is empty");
            throw new InvalidPitNumberException("Please select pit which is not empty.");
        }
        logger.info("Completed KalahServiceHelper.validatePitNumber with successfull validation of pit numbers" );
    }

    /**
     * This method checks that the given pit belongs to the current player and its empty or not.
     *
     * @param lastPitId
     * @param game
     * @return
     */
    public boolean lastPitWasOwnEmptyPit(int lastPitId, Game game) {
        logger.info("Inside KalahServiceHelper.lastPitWasOwnEmptyPit with game id "+game.getGameId());
        logger.debug("Inside KalahServiceHelper.lastPitWasOwnEmptyPit with game id "+game.getGameId()+" and lastPitId "+lastPitId);
        Map<Integer, Integer> board = game.getKalahBoard();
        boolean isLastPitWasOwnEmptyPit = board.get(lastPitId) == 1 && isUserPit(lastPitId, game.getPlayer());
        logger.trace("Is last own pit empty "+isLastPitWasOwnEmptyPit);
        return isLastPitWasOwnEmptyPit;
    }

    /**
     * This method checks if the game is still in progress or already terminated.
     *
     * @param game
     */
    public void checkIfGameStatusIsInProgress(Game game) {
        logger.info("Inside KalahServiceHelper.checkIfGameStatusIsInProgress with game id "+game.getGameId());
        logger.debug("Inside KalahServiceHelper.checkIfGameStatusIsInProgress with game details "+game);
        Status status = game.getStatus();
        if (status != Status.IN_PROGRESS) {
            logger.error("Game has been already been terminated with status:" + status+" for game id "+game.getGameId());
            throw new KalahException("Game has been already been terminated with status:" + status);
        }
        logger.info("Completed KalahServiceHelper.checkIfGameStatusIsInProgress ");
    }

    /**
     * This method checks that the given pit id belongs to the current player or not.
     *
     * @param pitId
     * @param player
     * @return
     */
    public boolean isUserPit(int pitId, Player player) {
        logger.info("Inside KalahServiceHelper.isUserPit with pit id "+pitId);
        logger.debug("Inside KalahServiceHelper.isUserPit with pit id "+pitId+" and player details "+player);
        logger.trace("isUserPit "+player.getPits().contains(pitId));
        return player.getPits().contains(pitId);
    }

    /**
     * This method fetches the opposite pit id from the respective pit id
     *
     * @param pitId
     * @return
     */
    public int getOppositePit(int pitId) {
        logger.info("Inside KalahServiceHelper.getOppositePit with pit id "+pitId);
        logger.trace("getOppositePit response " + (KalahGameConfiguration.LAST_PIT_INDEX - pitId));
        return KalahGameConfiguration.LAST_PIT_INDEX - pitId;
    }

    /**
     * This method is to check if the player has won the extra turn
     *
     * @param lastPitId
     * @param player
     * @return
     */
    public boolean playerWonAnotherTurn(int lastPitId, Player player) {
        logger.info("Inside KalahServiceHelper.playerWonAnotherTurn of player  "+player.name());
        logger.debug("Inside KalahServiceHelper.playerWonAnotherTurn of player  "+player+" and last pit id "+lastPitId);
        logger.trace("playerWonAnotherTurn response "+ (player.getKalahId() == lastPitId));
        return player.getKalahId() == lastPitId;
    }

    /**
     * This pit add stones to the respective pit
     *
     * @param pitId
     * @param board
     * @param quantity
     */
    public void addStonesToPit(int pitId, Map<Integer, Integer> board, int quantity) {
        logger.info("Inside KalahServiceHelper.addStonesToPit "+pitId);
        logger.debug("Inside KalahServiceHelper.addStonesToPit of pit "+pitId+" board details "+board+" with quantity "+quantity);
        board.replace(pitId, board.get(pitId) + quantity);
        logger.trace("Board after adding stones to pit "+board);
        logger.info("Completed KalahServiceHelper.addStonesToPit ");
    }

    /**
     * This method empties all the stones from the respective pit.
     *
     * @param pitId
     * @param board
     */
    public void emptyPit(int pitId, Map<Integer, Integer> board) {
        logger.info("Inside KalahServiceHelper.emptyPit ");
        logger.debug("Inside KalahServiceHelper.emptyPit with pit id "+pitId+" and board details "+board);
        board.replace(pitId, 0);
        logger.trace("Board after emptying pit "+board);
        logger.info("Completed KalahServiceHelper.emptyPit ");
    }
}