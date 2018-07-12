package com.codeoftheweb.salvo.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Ship {

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    @Column(name = "location")
    private List<String> locations;

    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    public Ship() {
    }

    public Ship(List<String> locations, String type) {
        this.locations = locations;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public List<String> getLocations() {
        return locations;
    }

    public String getType() {
        return type;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
    }
}