package com.org.kalah.service;

import com.org.kalah.domain.Game;
import com.org.kalah.dto.GameDTO;
import com.org.kalah.mapper.KalahMapper;
import com.org.kalah.repository.KalahDao;
import com.org.kalah.repository.impl.KalahDaoImpl;
import com.org.kalah.service.impl.KalahServiceHelper;
import com.org.kalah.service.impl.KalahServiceImpl;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KalahServiceTest {

    @Mock
    KalahDaoImpl kalahDao;

    @Mock
    KalahServiceHelper kalahServiceHelper;

    @Mock
    KalahMapper kalahMapper;

    @InjectMocks
    private KalahServiceImpl kalahService;


    @Value("${kalah.game.url}")
    private String kalahURL;

    Game game = null;

    @Before
    public void init(){
        game= new Game();
    }

    @Test
    public void CreateGameTest () {
        GameDTO newGameDTO = new GameDTO();
        newGameDTO.setGameId(1);
        newGameDTO.setUrl(kalahURL);
        when(kalahDao.saveGame(any(Game.class))).thenReturn(game);
        when(kalahMapper.toDTO(any(Game.class))).thenReturn(newGameDTO);
        GameDTO kalahGameDTO = kalahService.createKalahGame();
        assertEquals(1, kalahGameDTO.getGameId());
        assertEquals(kalahURL, kalahGameDTO.getUrl());
    }

    @Test
    public void testMakeMove() throws Exception {

        Map<Integer, Integer> boardStatus = Stream.of(new Integer[][] {
                { 1, 0}, { 2, 7 },{ 3, 7 },{ 4, 7 },{ 5, 7 },{ 6, 7 },{ 7, 1 },{ 8, 6 },{ 9, 6 },{ 10, 6 },{ 11, 6 },{ 12, 6 },{ 13, 6 },{ 14, 0 }
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        when(kalahDao.makeMove(1)).thenReturn(game);
        when(kalahMapper.toDTOForMakeMove(kalahDao.saveGame(any(Game.class)))).thenReturn(new GameDTO(1,kalahURL,boardStatus,"FIRST_PLAYER"));
        GameDTO gameDTO = kalahService.makeMove(1, 1);
        assertEquals(1, gameDTO.getGameId());
        assertEquals(kalahURL, gameDTO.getUrl());
        assertEquals(boardStatus,gameDTO.getBoardStatus());
        assertEquals("FIRST_PLAYER",gameDTO.getPlayerTurn());
    }



}
