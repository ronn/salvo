package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.repo.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    private final GameRepository gameRepo;

    @Autowired
    public SalvoController(GameRepository gameRepo) {
        this.gameRepo = gameRepo;
    }

    @RequestMapping("/games")
    public List<HashMap<String, Object>> getGames(){

        return gameRepo.findAll()
                .stream()
                .map(game -> new HashMap<String, Object>(){{
                    put("id", game.getId());
                    put("created", game.getCreationDate());
                }})
                .collect(Collectors.toList());
    }
}