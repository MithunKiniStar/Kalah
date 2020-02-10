package com.org.kalah.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.org.kalah.config.KalahGameConfiguration.*;

/**
 * @author Mithun Kini on 07/Feb/2020.
 * Domain Game for Kalah Game persistence.
 */
@Entity
@Table(name = "game")
public class Game implements Serializable {

    @Id
    @GeneratedValue
    private int gameId;

    @ElementCollection
    @MapKeyColumn(name="pitId")
    @Column(name="value")
    private Map<Integer, Integer> kalahBoard;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Enumerated(value = EnumType.STRING)
    private Player player;

    public Game() {
        initializeBoard();
        status = Status.IN_PROGRESS;
        player = Player.FIRST_PLAYER;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Map<Integer, Integer> getKalahBoard() {
        return kalahBoard;
    }

    public void setKalahBoard(Map<Integer, Integer> kalahBoard) {
        this.kalahBoard = kalahBoard;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return gameId == game.gameId &&
                Objects.equals(kalahBoard, game.kalahBoard) &&
                status == game.status &&
                player == game.player;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, kalahBoard, status, player);
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", board=" + kalahBoard +
                ", status=" + status +
                ", player=" + player +
                '}';
    }

        private void initializeBoard() {
        kalahBoard = new HashMap<>();
        for (int i = FIRST_PIT_INDEX; i <= LAST_PIT_INDEX; i++) {
            int firstKhalIndex = Player.FIRST_PLAYER.getKalahId();
            int secondKhalIndex = Player.SECOND_PLAYER.getKalahId();
            int value = (i != firstKhalIndex && i != secondKhalIndex) ? INITIAL_STONES_QUANTITY : 0;
            kalahBoard.put(i, value);
        }
    }

}
