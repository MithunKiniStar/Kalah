package com.org.kalah.mapper;

import com.org.kalah.domain.Game;
import com.org.kalah.dto.GameDTO;
import com.org.kalah.service.impl.KalahServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 * @author Mithun Kini on 07/Feb/2020.
 *
 * KalahMapper - Maps Game domain object to GameDTO
 *
 */
@Component
public class KalahMapper {

    private static final Logger logger = LoggerFactory.getLogger(KalahMapper.class);

    @Value("${kalah.game.url}")
    private String kalahURL;


    /**
     * This method converts Game domain object to GameDTO
     * URL is picked from the environment specific URL coonfigured in application.yml
     *
     * @param game
     * @return
     */
    public GameDTO toDTO(Game game) {
        logger.info("Inside toDTO ");
        logger.debug("Inside toDTO with game details "+game);
        GameDTO gameDTO = new GameDTO();
        gameDTO.setGameId(game.getGameId());
        gameDTO.setUrl(kalahURL.concat(String.valueOf(game.getGameId())));
        logger.debug("Response of toDTO : GameDTO "+gameDTO);
        logger.info("Completed  toDTO ");
        return gameDTO;
    }


    /**
     * This method converts Game domain object to GameDTO
     * URL is picked from the environment specific URL coonfigured in application.yml
     * playerTurn - suggests which player needs to make move next
     *
     * @param game
     * @return
     */
    public GameDTO toDTOForMakeMove(Game game) {
        logger.info("Inside toDTOForMakeMove ");
        logger.debug("Inside toDTOForMakeMove with game details "+game);
        int id = game.getGameId();
        Map<Integer, Integer> status = game.getKalahBoard();
        GameDTO gameDTO = new GameDTO(id, kalahURL.concat(String.valueOf(id)), status, game.getPlayer().name());
        logger.debug("Completed toDTOForMakeMove with GameDTO "+gameDTO);
        return gameDTO;
    }
}
