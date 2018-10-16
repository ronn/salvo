package com.codeoftheweb.salvo.repo;

import com.codeoftheweb.salvo.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByUserName();
}