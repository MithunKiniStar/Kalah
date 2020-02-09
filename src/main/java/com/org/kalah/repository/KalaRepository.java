package com.org.kalah.repository;

import com.org.kalah.domain.Game;
import com.org.kalah.domain.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public interface KalaRepository extends CrudRepository<Game, Integer>, JpaSpecificationExecutor<Game> {
    //Game saveGame();
}
