package com.org.kalah.controller;

import com.org.kalah.constants.APIURIConstant;
import com.org.kalah.dto.GameDTO;
import com.org.kalah.service.KalahService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;

/**
 * @author Mithun Kini on 07/Feb/2020.
 *
 * Main entry for all the manipulations of the Kalah game.
 * startGame,
 * makeMove
 */

@Api(value = "kalahController", description = "This is a Kalah Controller ")
@RestController
@RequestMapping("/games")
public class KalahController {

    private static final Logger logger = LoggerFactory.getLogger(KalahController.class);


    @Autowired
    KalahService kalahService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "This API is used to start game", response = GameDTO.class)
    public GameDTO startGame() {
        logger.info("Inside KalahController.startGame");
        return kalahService.createKalahGame();
    }

    @PutMapping(value = APIURIConstant.MOVE)
    @ApiOperation(value = "This API is used to make a move in the game", response = GameDTO.class)
    public GameDTO makeMove(@PathVariable int gameId,
                                           @PathVariable int pitId) throws Exception {
        logger.info("Inside KalahController.makeMove");
        return kalahService.makeMove(gameId,pitId);
    }
}
