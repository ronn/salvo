package com.codeoftheweb.salvo.repo;

import com.codeoftheweb.salvo.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game, Long> {
}