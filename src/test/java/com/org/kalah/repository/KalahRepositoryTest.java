package com.org.kalah.repository;


import com.org.kalah.domain.Game;
import com.org.kalah.domain.Player;
import com.org.kalah.domain.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class KalahRepositoryTest {

    @Autowired
    private KalaRepository repository;

    @Test
    public void saveGameTest() {
        Game game = new Game();
        Game newGame = repository.save(game);
        assertNotNull(newGame);
        assertEquals(1, newGame.getGameId());
        assertEquals(Status.IN_PROGRESS, newGame.getStatus());
        assertEquals(Player.FIRST_PLAYER, newGame.getPlayer());
    }
}
