package com.org.kalah.dto;

import java.util.Map;
import java.util.Objects;

/**
 * @author Mithun Kini on 07/Feb/2020.
 *
 * Url - Which will be used by both players to play game
 * Board Status - The position of all stones in Kalah board
 * Player Turn - Indicates which player should play next.
 */
public class GameDTO {
    private int gameId;

    private String url;

    private Map<Integer, Integer> boardStatus;

    private String playerTurn;

    public GameDTO(int gameId, String url, Map<Integer, Integer> status, String playerTurn) {
        this.gameId = gameId;
        this.url = url;
        this.boardStatus = status;
        this.playerTurn = playerTurn;
    }

    public GameDTO() {
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<Integer, Integer> getBoardStatus() {
        return boardStatus;
    }

    public void setBoardStatus(Map<Integer, Integer> boardStatus) {
        this.boardStatus = boardStatus;
    }

    public String getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(String playerTurn) {
        this.playerTurn = playerTurn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameDTO gameDTO = (GameDTO) o;
        return gameId == gameDTO.gameId &&
                Objects.equals(url, gameDTO.url) &&
                Objects.equals(boardStatus, gameDTO.boardStatus) &&
                Objects.equals(playerTurn, gameDTO.playerTurn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, url, boardStatus, playerTurn);
    }

    @Override
    public String toString() {
        return "GameDTO{" +
                "gameId=" + gameId +
                ", url='" + url + '\'' +
                ", status=" + boardStatus +
                ", playerTurn='" + playerTurn + '\'' +
                '}';
    }
}