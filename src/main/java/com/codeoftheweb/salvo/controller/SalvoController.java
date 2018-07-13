package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.entity.Game;
import com.codeoftheweb.salvo.entity.GamePlayer;
import com.codeoftheweb.salvo.repo.GamePlayerRepository;
import com.codeoftheweb.salvo.repo.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    private final GameRepository gameRepo;
    private final GamePlayerRepository gamePlayerRepo;

    @Autowired
    public SalvoController(GameRepository gameRepo, GamePlayerRepository gamePlayerRepo) {
        this.gameRepo = gameRepo;
        this.gamePlayerRepo = gamePlayerRepo;
    }

    @RequestMapping("/games")
    public List<HashMap<String, Object>> getGames(){
        return gameRepo.findAll()
                .stream()
                .map(this::getGameMap)
                .collect(Collectors.toList());
    }

    @RequestMapping("/games/game_view/{id}")
    public HashMap<String, Object> getGame(@PathVariable("id") Long id){
        return gamePlayerRepo.findById(id)
                .map(this::getGameMapById)
                .orElse(null);
    }

    private HashMap<String, Object> getGameMapById(GamePlayer gamePlayer) {
        final List<HashMap<String, Object>> ships = getShipsMap(gamePlayer);

        final List<HashMap<String, Object>> salvoes = getSalvoesMap(gamePlayer);

        final Game game = gamePlayer.getGame();

        HashMap<String, Object> gameMap = getGameMap(game);

        gameMap.put("ships", ships);
        gameMap.put("salvoes", salvoes);

        return gameMap;
    }

    private List<HashMap<String, Object>> getSalvoesMap(GamePlayer gamePlayer) {
        return gamePlayer.getSalvos()
                .stream()
                .map(salvo -> new HashMap<String, Object>() {{
                    put("turn", salvo.getTurn());
                    put("player", salvo.getGamePlayer().getPlayer().getId());
                    put("location", salvo.getLocations());
                }})
                .collect(Collectors.toList());
    }

    private List<HashMap<String, Object>> getShipsMap(GamePlayer gamePlayer) {
        return gamePlayer.getShips().stream()
                .map(ship -> new HashMap<String, Object>() {{
                    put("type", ship.getType());
                    put("locations", ship.getLocations());
                }}).collect(Collectors.toList());
    }

    private HashMap<String, Object> getGameMap(Game game) {
        return new HashMap<String, Object>(){{
                    put("id", game.getId());
                    put("created", game.getCreationDate());
                    put("gamePlayers", getGamePlayers(game));
                }};
    }

    private List<HashMap<String, Object>> getGamePlayers(Game game) {
        return game.getGamePlayers().stream()
                .map(gamePlayer -> getGamePlayerMap(gamePlayer, getPlayerMap(gamePlayer)))
                .collect(Collectors.toList());
    }

    private HashMap<String, Object> getGamePlayerMap(GamePlayer gamePlayer, HashMap<String, Object> player) {
        return new HashMap<String, Object>() {{
            put("id", gamePlayer.getId());
            put("player", player);
            put("score", gamePlayer.getScore());
        }};
    }

    private HashMap<String, Object> getPlayerMap(GamePlayer gamePlayer) {
        return new HashMap<String, Object>() {{
            put("id", gamePlayer.getPlayer().getId());
            put("email", gamePlayer.getPlayer().getUserName());
        }};
    }
}