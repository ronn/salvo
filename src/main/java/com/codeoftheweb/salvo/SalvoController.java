package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.*;
import com.codeoftheweb.salvo.repo.*;
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
    private final ShipRepository shipRepo;
    private final SalvoRepository salvoRepo;

    @Autowired
    public SalvoController(GameRepository gameRepo, GamePlayerRepository gamePlayerRepo, PlayerRepository playerRepo, ScoreRepository scoreRepo, ShipRepository shipRepo, SalvoRepository salvoRepo) {
        this.gameRepo = gameRepo;
        this.gamePlayerRepo = gamePlayerRepo;
        this.playerRepo = playerRepo;
        this.scoreRepo = scoreRepo;
        this.shipRepo = shipRepo;
        this.salvoRepo = salvoRepo;
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
        return getResponseEntity(
                playerRepo.findByEmail(player.getEmail()),
                player
        );
    }

    private ResponseEntity<Map<String, String>> getResponseEntity(Optional<Player> optionalPlayer, Player p){
        return new ResponseEntity<>(getResponseSignUp(optionalPlayer, p), getHTTPStatus(optionalPlayer)
        );
    }

    private HashMap<String, String> getResponseSignUp(Optional<Player> optionalPlayer, Player p) {
        return new HashMap<String, String>() {{
            optionalPlayer
                    .map(player -> put("Error", "Name in use"))
                    .orElseGet(() -> put("userName", playerRepo.save(p).getEmail()));
        }};
    }

    private HttpStatus getHTTPStatus(Optional<Player> optionalPlayer) {
        return optionalPlayer
                .map(player -> HttpStatus.FORBIDDEN)
                .orElse(HttpStatus.CREATED);
    }

    private Map<String, Object> getPlayerInfo(Player p){
        List<Score> scores = scoreRepo.findByPlayer(p);
        if (scores.size() == 0) return null;

        final Double WON_SCORE = 1.0;
        final Double TIE_SCORE = 0.5;
        final Double LOST_SCORE = 0.0;

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
    public ResponseEntity<Map<String, Object>> getGameView(@PathVariable Long nn, Authentication auth){
          return gamePlayerRepo.findById(nn)
                .flatMap(gamePlayer -> getPlayerOpt(auth)
                        .map(player -> getMapResponseEntity(gamePlayer, player))
                ).orElse(new ResponseEntity<>(HttpStatus.FORBIDDEN));
    }

    private ResponseEntity<Map<String, Object>> getMapResponseEntity(GamePlayer gamePlayer, Player player) {
        return gamePlayer.getPlayer().equals(player) ?
                new ResponseEntity<>(
                        new LinkedHashMap<String, Object>() {{
                            put("id", gamePlayer.getGame().getId());
                            put("created", gamePlayer.getGame().getCreated());
                            put("gamePlayers", getGamePlayersFrom(gamePlayer.getGame().getGamePlayers()));
                            put("ships", buildShips(gamePlayer.getShips()));
                            put("salvoes", buildSalvoes(gamePlayer.getGame().getGamePlayers()));
                        }},
                        HttpStatus.OK
                )
                : new ResponseEntity<>(HttpStatus.FORBIDDEN);
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
        return new LinkedHashMap<String, Object>(){{
            put("player", getPlayerFromAuth(auth));
            put("games", getCollectedGames());
        }};
    }

    private List<Map<String, Object>> getCollectedGames() {
        return gameRepo.findAll()
                .stream()
                .map(this::getMapFrom)
                .collect(toList());
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
        return isGuest(auth) ? Optional.empty() : playerRepo.findByEmail(auth.getName());
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

    @RequestMapping(value = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication auth){
        return getPlayerOpt(auth)
                .map(player -> getGpid(player, new Game()))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    private ResponseEntity<Map<String, Object>> getGpid(Player player, Game game) {
        return new ResponseEntity<>(
                new HashMap<String, Object>() {{
                    put("gpid", saveGame(player, game));
                }},
                HttpStatus.CREATED
        );
    }

    private Long saveGame(Player player, Game game) {
        GamePlayer gp = new GamePlayer(new Date());

        player.addGamePlayer(gp);
        game.addGamePlayer(gp);

        gameRepo.save(game);
        playerRepo.save(player);

        return gamePlayerRepo.save(gp).getId();
    }

    @RequestMapping(value = "/game/{nn}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> joinGame(@PathVariable Long nn, Authentication auth){
         return getPlayerOpt(auth)
                .flatMap(player -> gameRepo.findById(nn)
                        .map(game -> game.getGamePlayers().size() > 1
                                ? getForbiddenResponse("Game is full")
                                : getGpid(player, game)
                        )
                ).orElseGet(() -> getForbiddenResponse("No such Game!"));
    }

    private ResponseEntity<Map<String, Object>> getForbiddenResponse(String msj) {
        return getCustomResponse(msj, HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<Map<String, Object>> getUnauthorizedResponse(String msj) {
        return getCustomResponse(msj, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<Map<String, Object>> getCreatedResponse(String msj) {
        return getCustomResponse(msj, HttpStatus.CREATED);
    }

    private ResponseEntity<Map<String, Object>> getCustomResponse(String msj, HttpStatus status) {
        return new ResponseEntity<>(
                new HashMap<String, Object>() {{
                    put("msj", msj);
                    put("status", status);
                }},
                status
        );
    }

    @RequestMapping(value = "/games/players/{gpId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> placeShips(@PathVariable Long gpId, @RequestBody List<Ship> ships, Authentication auth){
        return getPlayerOpt(auth)
                .map(player -> gamePlayerRepo.findById(gpId)
                        .map(gp -> gp.getPlayer().equals(player)
                                ? saveShips(ships, gp)
                                : getUnauthorizedResponse("The current user is not the game player the ID references")
                        ).orElseGet(() -> getUnauthorizedResponse("There is no game player with the given ID"))
                 ).orElseGet(() -> getUnauthorizedResponse("There is no current user logged in"));
    }

    private ResponseEntity<Map<String, Object>> saveShips(List<Ship> ships, GamePlayer gp){
        if (!gp.getShips().isEmpty()) return getForbiddenResponse("User already has ships placed");

        ships.forEach(ship -> {
            ship.setGamePlayer(gp);
            shipRepo.save(ship);
        });

        return getCreatedResponse("Ships placed!");
    }

    @RequestMapping(value = "/games/players/{gpId}/salvos", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> fireSalvo(@PathVariable Long gpId, @RequestBody Salvo salvo, Authentication auth){
        return getPlayerOpt(auth)
                .map(player -> gamePlayerRepo.findById(gpId)
                        .map(gp -> gp.getPlayer().equals(player)
                                ? saveSalvo(salvo, gp)
                                : getUnauthorizedResponse("The current user is not the game player the ID references")
                        ).orElseGet(() -> getUnauthorizedResponse("There is no game player with the given ID"))
                ).orElseGet(() -> getUnauthorizedResponse("There is no current user logged in"));
    }

    private ResponseEntity<Map<String, Object>> saveSalvo(Salvo salvo, GamePlayer gp){
        System.out.println("Trying to fire salvo: " + salvo);
        boolean hasSalvoInTurn = !gp.getSalvoes().stream()
                .filter(s -> s.getTurn().equals(salvo.getTurn()))
                .collect(toList())
                .isEmpty();

        System.out.println("User already has salvos for the turn: " + hasSalvoInTurn);
        if (hasSalvoInTurn) return getForbiddenResponse("User has already fired a salvo in this turn");

        salvo.setGamePlayer(gp);
        salvoRepo.save(salvo);

        return getCreatedResponse("Salvo fired!");
    }
}