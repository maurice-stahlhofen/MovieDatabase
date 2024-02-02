package com.example.Spring2.DiskussionsEinladungPackage;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Diskussionseinladung {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private int diskussionsID;
    private String recipient;
    private String sender;

    public Diskussionseinladung(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiskussionsID() {
        return diskussionsID;
    }

    public void setDiskussionsID(int diskussionsID) {
        this.diskussionsID = diskussionsID;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
