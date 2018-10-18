package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.Game;
import com.codeoftheweb.salvo.repo.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {

    private final
    GameRepository gameRepo;

    @Autowired
    public SalvoController(GameRepository gameRepo) {
        this.gameRepo = gameRepo;
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
        }};
    }
}