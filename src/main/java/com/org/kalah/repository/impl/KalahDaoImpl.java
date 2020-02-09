package com.org.kalah.repository.impl;

import com.org.kalah.domain.Game;
import com.org.kalah.domain.Status;

import com.org.kalah.exceptions.KalahException;
import com.org.kalah.mapper.KalahMapper;
import com.org.kalah.repository.KalaRepository;
import com.org.kalah.repository.KalahDao;
import org.apache.tomcat.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
public class KalahDaoImpl implements KalahDao {

    private static final Logger logger = LoggerFactory.getLogger(KalahDaoImpl.class);


    @Autowired
    KalaRepository repository;



    @Override
    public Game saveGame(Game game) {
        logger.info("Inside KalahDaoImpl.saveGame");
        return repository.save(game);
    }

    @Override
    public Game makeMove(int gameId) {
        logger.info("Inside KalahDaoImpl.makeMove");
        try {
            return repository.findById(gameId).get();
        } catch (NoSuchElementException e) {
            logger.error("Invalid game id " + gameId +" , "+ e.getStackTrace());
            throw new KalahException("Invalid game id " + gameId + " . Please enter a valid game id or create a new game before you make a move.");
        } catch (Exception ex) {
            logger.error("Problem occured while making a move in a game of game id " + gameId + ex.getStackTrace());
            throw new KalahException("Problem occured while making a move in a game of game id " + gameId + " . Please try after sometime or contact system administratior.");
        }

    }


}
