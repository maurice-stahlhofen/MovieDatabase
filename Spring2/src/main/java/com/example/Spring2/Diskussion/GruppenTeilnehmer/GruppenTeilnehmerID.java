package com.example.Spring2.Diskussion.GruppenTeilnehmer;

//https://www.baeldung.com/jpa-composite-primary-keys
//https://attacomsian.com/blog/spring-data-jpa-composite-primary-key
//https://www.jpa-buddy.com/blog/the-ultimate-guide-on-composite-ids-in-jpa-entities/

import java.io.Serializable;
import java.util.Objects;

public class GruppenTeilnehmerID implements Serializable {

    private Integer groupID;
    private String participant;

    public GruppenTeilnehmerID(Integer groupID, String participant) {
        this.groupID = groupID;
        this.participant = participant;
    }

    public GruppenTeilnehmerID() {
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GruppenTeilnehmerID that = (GruppenTeilnehmerID) o;
        return Objects.equals(groupID, that.groupID) && Objects.equals(participant, that.participant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupID, participant);
    }
}
