package com.codeoftheweb.salvo.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Game {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private Date creationDate;

    @OneToMany(mappedBy="game", fetch=FetchType.LAZY)
    private List<GamePlayer> gamePlayers = new ArrayList<>();

    @OneToMany(mappedBy="game", fetch=FetchType.LAZY)
    private List<Score> scores = new ArrayList<>();

    public Game() {
    }

    public Game(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void addGamePlayer(GamePlayer gp){
        gp.setGame(this);
        this.gamePlayers.add(gp);
    }

    public List<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public List<Player> getPlayers(){
        return this.gamePlayers
                .stream()
                .map(GamePlayer::getPlayer)
                .collect(Collectors.toList());
    }

    public List<Score> getScores() {
        return scores;
    }

    public void addScore(Score score){
        score.setGame(this);
        scores.add(score);
    }
}