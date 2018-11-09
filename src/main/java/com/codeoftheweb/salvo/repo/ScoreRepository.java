package com.codeoftheweb.salvo.repo;

import com.codeoftheweb.salvo.entity.Player;
import com.codeoftheweb.salvo.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findByPlayer(Player player);
}