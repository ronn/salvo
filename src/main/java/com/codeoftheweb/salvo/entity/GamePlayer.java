package com.codeoftheweb.salvo.entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private List<Ship> ships = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy="gamePlayer")
    private List<Salvo> salvos = new ArrayList<>();

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

    public List<Ship> getShips() {
        return ships;
    }

    public List<Salvo> getSalvos() {
        return salvos;
    }

    public void addShip(Ship ship){
        ship.setGamePlayer(this);
        this.ships.add(ship);
    }

    public void addSalvo(Salvo salvo){
        salvo.setGamePlayer(this);
        this.salvos.add(salvo);
    }

    public Score getScore() {
        return this.player.getScore(this.game);
    }
}