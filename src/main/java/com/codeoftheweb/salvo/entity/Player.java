package com.codeoftheweb.salvo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Player {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String userName;

    @OneToMany(mappedBy="player", fetch=FetchType.LAZY)
    private List<GamePlayer> gamePlayers = new ArrayList<>();

    public Player() {
    }

    public Player(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void addGamePlayer(GamePlayer gp){
        gp.setPleayer(this);
        this.gamePlayers.add(gp);
    }

    @JsonIgnore
    public List<Game> getGames(){
        return this.gamePlayers
                .stream()
                .map(GamePlayer::getGame)
                .collect(Collectors.toList());
    }
}