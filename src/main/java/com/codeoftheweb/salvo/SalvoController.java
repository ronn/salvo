package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.*;
import com.codeoftheweb.salvo.repo.GamePlayerRepository;
import com.codeoftheweb.salvo.repo.GameRepository;
import com.codeoftheweb.salvo.repo.PlayerRepository;
import com.codeoftheweb.salvo.repo.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {

    private final GameRepository gameRepo;
    private final GamePlayerRepository gamePlayerRepo;
    private final PlayerRepository playerRepo;
    private final ScoreRepository scoreRepo;

    @Autowired
    public SalvoController(GameRepository gameRepo, GamePlayerRepository gamePlayerRepo, PlayerRepository playerRepo, ScoreRepository scoreRepo) {
        this.gameRepo = gameRepo;
        this.gamePlayerRepo = gamePlayerRepo;
        this.playerRepo = playerRepo;
        this.scoreRepo = scoreRepo;
    }

    @RequestMapping("/leaderboard")
    public List<Map<String, Object>> getLeaderBoard(){
        return playerRepo.findAll().stream()
                .map(this::getPlayerInfo)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    @RequestMapping(value = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> registerPlayer(@RequestBody Player player){
        return Optional.ofNullable(playerRepo.findByEmail(player.getEmail()))
                .map(p -> new ResponseEntity<Map<String, String>>(new HashMap<String, String>() {{
                            put("error", "name in use");
                        }}, HttpStatus.FORBIDDEN)
                ).orElseGet(() -> new ResponseEntity<Map<String, String>>(new HashMap<String, String>() {{
                    put("username", playerRepo.save(player).getEmail());
                }}, HttpStatus.CREATED));
    }


    private ResponseEntity<Map<String, String>> aa(Player player){


        return new ResponseEntity<Map<String, String>>(new HashMap<String, String>(){{

        }}
        return Optional.ofNullable(playerRepo.findByEmail(player.getEmail()))
                .map(p -> new ResponseEntity<Map<String, String>>(new HashMap<String, String>() {{
                            put("error", "name in use");
                        }}, HttpStatus.FORBIDDEN)
                ).orElseGet(() -> new ResponseEntity<Map<String, String>>(new HashMap<String, String>() {{
                    put("username", playerRepo.save(player).getEmail());
                }}, HttpStatus.CREATED));
    }

    private Map<String, Object> getPlayerInfo(Player p){
        List<Score> scores = scoreRepo.findByPlayer(p);
        if (scores.size() == 0) return null;

        Double WON_SCORE = 1.0;
        Double TIE_SCORE = 0.5;
        Double LOST_SCORE = 0.0;

        return new LinkedHashMap<String, Object>(){{
            put("name", p.getEmail());
            put("total", getTotalScore(scores));
            put("won", getCount(scores, WON_SCORE));
            put("lost", getCount(scores, LOST_SCORE));
            put("tied", getCount(scores, TIE_SCORE));
        }};
    }

    private Long getCount(List<Score> scores, Double scoreType){
        return scores
                .stream()
                .filter(score -> scoreType.equals(score.getScore()))
                .count();
    }

    private Double getTotalScore(List<Score> scores){
        return scores
                .stream()
                .mapToDouble(Score::getScore)
                .sum();
    }

    @RequestMapping("/game_view/{nn}")
    public Map<String, Object> getGameView(@PathVariable Long nn){
        return gamePlayerRepo.findById(nn)
                .map(gamePlayer -> new LinkedHashMap<String, Object>(){{
                    put("id", gamePlayer.getGame().getId());
                    put("created", gamePlayer.getGame().getCreated());
                    put("gamePlayers", getGamePlayersFrom(gamePlayer.getGame().getGamePlayers()));
                    put("ships", buildShips(gamePlayer.getShips()));
                    put("salvoes", buildSalvoes(gamePlayer.getGame().getGamePlayers()));
                }})
                .orElse(null);
    }

    private List<Map<String, Object>> buildShips(Set<Ship> ships){
        return ships.stream()
                .map(ship -> new LinkedHashMap<String, Object>(){{
                    put("type", ship.getType());
                    put("locations", ship.getLocations());
                }})
                .collect(toList());
    }

    private List<Map<String, Object>> buildSalvoes(Set<GamePlayer> gps){
        return gps.stream()
                .flatMap(gp -> gp.getSalvoes().stream()
                        .map(salvo -> new LinkedHashMap<String, Object>() {{
                                put("turn", salvo.getTurn());
                                put("player", salvo.getGamePlayer().getPlayer().getId());
                                put("locations", salvo.getLocations());
                            }})
                        ).collect(toList());
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

    private Map<String, Object> getPlayer(Player player){
        return new HashMap<String, Object>(){{
            put("id", player.getId());
            put("email", player.getEmail());
        }};
    }

    @RequestMapping("/games")
    public Map<String, Object> getGames(Authentication auth){
        List<Map<String, Object>> games = gameRepo.findAll()
                .stream()
                .map(this::getMapFrom)
                .collect(toList());

        return new LinkedHashMap<String, Object>(){{
            put("player", getPlayerFromAuth(auth));
            put("games", games);
        }};
    }

    private Map<String, Object> getMapFrom(Game game){
        return new LinkedHashMap<String, Object>(){{
            put("id", game.getId());
            put("created", game.getCreated());
            put("gamePlayers", getListOfMapsFrom(game.getGamePlayers()));
        }};
    }

    private boolean isGuest(Authentication authentication) {
        return null == authentication || authentication instanceof AnonymousAuthenticationToken;
    }

    private Map<String, Object> getPlayerFromAuth(Authentication auth){
        return getPlayerOpt(auth)
                .map(this::getPlayer)
                .orElse(null);
    }

    private Optional<Player> getPlayerOpt(Authentication auth) {
        return Optional.ofNullable(isGuest(auth) ? null : playerRepo.findByEmail(auth.getName()));
    }

    private List<Map<String, Object>> getListOfMapsFrom(Set<GamePlayer> gps){
        return gps
                .stream()
                .map(this::getMapFrom)
                .collect(toList());
    }

    private Map<String, Object> getMapFrom(GamePlayer gp){
        return new LinkedHashMap<String, Object>(){{
            put("id", gp.getId());
            put("player", gp.getPlayer());
            put("score", gp.getScore());
        }};
    }
}