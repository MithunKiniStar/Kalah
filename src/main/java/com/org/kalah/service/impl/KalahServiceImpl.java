package com.org.kalah.service.impl;

import com.org.kalah.controller.KalahController;
import com.org.kalah.domain.Game;
import com.org.kalah.dto.GameDTO;
import com.org.kalah.mapper.KalahMapper;
import com.org.kalah.repository.KalahDao;
import com.org.kalah.service.KalahService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KalahServiceImpl implements KalahService {

    private static final Logger logger = LoggerFactory.getLogger(KalahServiceImpl.class);

    @Autowired
    KalahDao kalahDao;

    @Autowired
    KalahServiceHelper kalahServiceHelper;

    @Autowired
    KalahMapper kalahMapper;

    @Override
    public GameDTO createKalahGame() {
        logger.info("Inside KalahServiceImpl.createKalahGame");
        Game game = kalahDao.saveGame(new Game());
        logger.debug("Created new Game "+game);
        return kalahMapper.toDTO(game);
    }

    @Override
    public GameDTO makeMove(int gameId, int pitId) throws Exception {
        logger.info("Inside KalahServiceImpl.makeMove");
        Game game = kalahDao.makeMove(gameId);
        logger.trace("Game domain before making move "+game);
        kalahServiceHelper.performMove(game, pitId);
        logger.trace("Game domain after making move "+game);
        return kalahMapper.toDTOForMakeMove(kalahDao.saveGame(game));
    }
}
