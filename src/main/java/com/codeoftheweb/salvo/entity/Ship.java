package com.codeoftheweb.salvo.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
public class Ship {

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    @Column(name = "location")
    private List<String> locations;

    private Type type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    public Ship() {
    }

    public Ship(Type type) {
        this.type = type;
        this.locations = Arrays.asList(
                "A1",
                "A2",
                "A4",
                "A3",
                "5",
                "F6",
                "C7",
                "C9",
                "C8",
                "H6",
                "H4",
                "H3",
                "H2",
                "H1"
        );
    }

    public Long getId() {
        return id;
    }

    public List<String> getLocations() {
        return locations;
    }

    public Type getType() {
        return type;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    void setGamePlayer(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
    }

    public enum Type{
        Carrier,
        Battleship,
        Submarine,
        Destroyer,
        Patrol_Boat
    }
}