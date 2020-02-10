package com.org.kalah.service;

import com.org.kalah.dto.GameDTO;

/**
 * @author Mithun Kini on 07/Feb/2020.
 *
 */
public interface KalahService {
    public GameDTO createKalahGame();

    public GameDTO makeMove(int gameId, int pitId) throws Exception;
}
