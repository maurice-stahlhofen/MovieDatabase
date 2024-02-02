package com.example.Spring2.ChatPackage;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String participant1Email;
    private String participant2Email;

    public Chat() {
    }


    public String getParticipant1Email() {
        return participant1Email;
    }

    public void setParticipant1Email(String participant1Email) {
        this.participant1Email = participant1Email;
    }

    public String getParticipant2Email() {
        return participant2Email;
    }

    public void setParticipant2Email(String participant2Email) {
        this.participant2Email = participant2Email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
