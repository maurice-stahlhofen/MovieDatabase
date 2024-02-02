package client.client.Soziales.Discussion.entities;

public class Einladung {
    private Integer id;
    private String recipient;
    private String sender;;
    private Integer groupID;
    //GroupName für toString()-Methode
    private String groupName;

    public Einladung() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String toString() {
        return "Privat: "+groupName+"; Von "+getSender();
    }
}
