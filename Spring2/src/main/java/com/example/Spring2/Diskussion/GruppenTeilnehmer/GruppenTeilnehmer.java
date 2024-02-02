package com.example.Spring2.Diskussion.GruppenTeilnehmer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Objects;

//https://www.baeldung.com/jpa-composite-primary-keys

@Entity
@IdClass(GruppenTeilnehmerID.class)
public class GruppenTeilnehmer {

    @Id
    private Integer groupID;
    @Id
    private String participant;

    public GruppenTeilnehmer() {
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
        GruppenTeilnehmer that = (GruppenTeilnehmer) o;
        return Objects.equals(groupID, that.groupID) && Objects.equals(participant, that.participant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupID, participant);
    }

    @Override
    public String toString() {
        return "GruppenTeilnehmer{" +
                "groupID=" + groupID +
                ", participant='" + participant + '\'' +
                '}';
    }
}
