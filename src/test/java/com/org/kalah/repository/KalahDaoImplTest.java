package com.org.kalah.repository;

import com.org.kalah.domain.Game;
import com.org.kalah.domain.Player;
import com.org.kalah.domain.Status;
import com.org.kalah.repository.impl.KalahDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class KalahDaoImplTest {

    @Mock
    KalaRepository repository;

    @InjectMocks
    KalahDaoImpl kalahDao;

    Game game = null;

    @Before
    public void init(){
        game= new Game();
        game.setGameId(1);
    }

    @Test
    public void saveGameTest(){
        when(repository.save(any(Game.class))).thenReturn(game);
        Game newGame = kalahDao.saveGame(game);
        assertNotNull(game);
        assertEquals(1, game.getGameId());
        assertEquals(Status.IN_PROGRESS, game.getStatus());
        assertEquals(Player.FIRST_PLAYER, game.getPlayer());
    }

    @Test
    public void makeMoveTest(){
        when(repository.findById(game.getGameId())).thenReturn(java.util.Optional.ofNullable(game));
        Game newGame = kalahDao.makeMove(game.getGameId());
        assertNotNull(game);
        assertEquals(1, game.getGameId());
        assertEquals(Status.IN_PROGRESS, game.getStatus());
        assertEquals(Player.FIRST_PLAYER, game.getPlayer());
    }

}
