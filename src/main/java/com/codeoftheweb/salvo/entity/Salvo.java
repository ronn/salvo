package com.codeoftheweb.salvo.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
public class Salvo {

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    @Column(name = "location")
    private List<String> locations;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    private Integer turn;


    public Salvo() {
    }

    public Salvo(Integer turn) {
        this.turn = turn;
        this.locations = Arrays.asList(
                "A1",
                "A2",
                "A4",
                "A3",
                "G5",
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

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public Integer getTurn() {
        return turn;
    }

    void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
}