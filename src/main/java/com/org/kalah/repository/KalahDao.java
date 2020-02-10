package com.org.kalah.repository;

import com.org.kalah.domain.Game;
import com.org.kalah.dto.GameDTO;

/**
 * @author Mithun Kini on 07/Feb/2020.
 *
 */
public interface KalahDao {
    public Game saveGame(Game game);

    public Game makeMove(int gameId) throws Exception;
}
