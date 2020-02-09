package com.org.kalah.service;

import com.org.kalah.dto.GameDTO;

public interface KalahService {
    public GameDTO createKalahGame();

    public GameDTO makeMove(int gameId, int pitId) throws Exception;
}
