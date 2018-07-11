package com.codeoftheweb.salvo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Player {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String userName;

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
}