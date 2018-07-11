package com.codeoftheweb.salvo.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class GamePlayer {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private Date creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    public GamePlayer() {
    }

    public GamePlayer(Date creationDate, Player player, Game game) {
        this.creationDate = creationDate;
        this.player = player;
        this.game = game;
    }

    public Long getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public void setPleayer(Player player) {
        this.player = player;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}