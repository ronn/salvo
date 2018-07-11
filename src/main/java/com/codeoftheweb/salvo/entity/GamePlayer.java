package com.codeoftheweb.salvo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class GamePlayer {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private Date creationDate;

    public GamePlayer() {
    }

    public GamePlayer(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}