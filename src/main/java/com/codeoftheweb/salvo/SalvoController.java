package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.Game;
import com.codeoftheweb.salvo.entity.GamePlayer;
import com.codeoftheweb.salvo.entity.Ship;
import com.codeoftheweb.salvo.repo.GamePlayerRepository;
import com.codeoftheweb.salvo.repo.GameRepository;
import com.codeoftheweb.salvo.repo.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {

    private final GameRepository gameRepo;
    private final GamePlayerRepository gamePlayerRepo;
    private final PlayerRepository playerRepo;

    @Autowired
    public SalvoController(GameRepository gameRepo, GamePlayerRepository gamePlayerRepo, PlayerRepository playerRepo) {
        this.gameRepo = gameRepo;
        this.gamePlayerRepo = gamePlayerRepo;
        this.playerRepo = playerRepo;
    }

    @RequestMapping("/game_view/{nn}")
    public Map<String, Object> getGameView(@PathVariable Long nn){
        return gamePlayerRepo.findById(nn)
                .map(gamePlayer -> new LinkedHashMap<String, Object>(){{
                    put("id", gamePlayer.getGame().getId());
                    put("created", gamePlayer.getGame().getCreated());
                    put("gamePlayers", getGamePlayersFrom(gamePlayer.getGame().getGamePlayers()));
                    put("ships", build(gamePlayer.getShips()));
                }})
                .orElse(null);
    }

    private List<Map<String, Object>> build(Set<Ship> ships){
        return ships.stream()
                .map(ship -> new LinkedHashMap<String, Object>(){{
                    put("type", ship.getType());
                    put("locations", ship.getLocations());
                }})
                .collect(toList());
    }

    private List<Map<String, Object>> getGamePlayersFrom(Set<GamePlayer> gps){
        return gps
                .stream()
                .map(gp -> new HashMap<String, Object>() {{
                    put("id", gp.getId());
                    put("player", getPlayer(gp));
                }})
                .collect(toList());
    }

    private Map<String, Object> getPlayer(GamePlayer gp){
        return new HashMap<String, Object>(){{
            put("id", gp.getPlayer().getId());
            put("email", gp.getPlayer().getEmail());
        }};
    }

    @RequestMapping("/games")
    public List<Map<String, Object>> getGames(){
         return gameRepo.findAll()
                .stream()
                .map(this::getMapFrom)
                .collect(toList());
    }

    private Map<String, Object> getMapFrom(Game game){
        return new HashMap<String, Object>(){{
            put("id", game.getId());
            put("created", game.getCreated());
            put("gamePlayers", getListOfMapsFrom(game.getGamePlayers()));
        }};
    }

    private List<Map<String, Object>> getListOfMapsFrom(Set<GamePlayer> gps){
        return gps
                .stream()
                .map(this::getMapFrom)
                .collect(toList());
    }

    private Map<String, Object> getMapFrom(GamePlayer gp){
        return new HashMap<String, Object>(){{
            put("id", gp.getId());
            put("player", gp.getPlayer());
        }};
    }
}