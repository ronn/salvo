package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.entity.Game;
import com.codeoftheweb.salvo.entity.GamePlayer;
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
                .map(game -> getGameMap(game, getGamePlayers(game)))
                .collect(Collectors.toList());
    }

    private List<HashMap<String, Object>> getGamePlayers(Game game) {
        return game.getGamePlayers().stream()
                .map(gamePlayer -> getGamePlayerMap(gamePlayer, getPlayerMap(gamePlayer)))
                .collect(Collectors.toList());
    }

    private HashMap<String, Object> getGameMap(Game game, List<HashMap<String, Object>> gamePlayersMap) {
        return new HashMap<String, Object>(){{
                    put("id", game.getId());
                    put("created", game.getCreationDate());
                    put("gamePlayers", gamePlayersMap);
                }};
    }

    private HashMap<String, Object> getGamePlayerMap(GamePlayer gamePlayer, HashMap<String, Object> player) {
        return new HashMap<String, Object>() {{
            put("id", gamePlayer.getId());
            put("player", player);
        }};
    }

    private HashMap<String, Object> getPlayerMap(GamePlayer gamePlayer) {
        return new HashMap<String, Object>() {{
            put("id", gamePlayer.getPlayer().getId());
            put("email", gamePlayer.getPlayer().getUserName());
        }};
    }
}