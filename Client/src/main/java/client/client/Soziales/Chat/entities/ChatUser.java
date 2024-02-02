package client.client.Soziales.Chat.entities;

public class ChatUser {

    private String vorname;
    private String nachname;
    private String email;
    private Integer chatID;

    public ChatUser() {
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getChatID() {
        return chatID;
    }

    public void setChatID(Integer chatID) {
        this.chatID = chatID;
    }

    @Override
    public String toString() {
        return vorname +" "+nachname+"; " +email;
    }
}
