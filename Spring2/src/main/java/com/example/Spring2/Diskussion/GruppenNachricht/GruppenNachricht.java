package com.example.Spring2.Diskussion.GruppenNachricht;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GruppenNachricht {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;
    private String participant;
    //Firstname und Lastname hier dazu, damit die Afrage später im Controller auf der
    //ClientSeite schneller/ performanter(?) ist
    private String participantFirstName;
    private String participantLastname;
    private Integer groupID;
    private String content;

    public GruppenNachricht() {
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParticipantFirstName() {
        return participantFirstName;
    }

    public void setParticipantFirstName(String participantFirstName) {
        this.participantFirstName = participantFirstName;
    }

    public String getParticipantLastname() {
        return participantLastname;
    }

    public void setParticipantLastname(String participantLastname) {
        this.participantLastname = participantLastname;
    }
}
