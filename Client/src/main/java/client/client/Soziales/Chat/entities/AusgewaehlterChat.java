package client.client.Soziales.Chat.entities;

public class AusgewaehlterChat {

    private static AusgewaehlterChat ausgewaehlteChats;
    private int chatID;

    private ChatUser participant;
    private String vornameAnderer;
    private String nachnameAnderer;

    private AusgewaehlterChat(){
    }

    public static AusgewaehlterChat getAusgewaehlterChat(){
      if(ausgewaehlteChats==null){
          ausgewaehlteChats=new AusgewaehlterChat();
      }
      return ausgewaehlteChats;
    }

    public ChatUser getParticipant() {
        return participant;
    }

    public void setParticipant(ChatUser participant) {
        this.participant = participant;
    }
    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }

    public String getVornameAnderer() {
        return vornameAnderer;
    }

    public void setVornameAnderer(String vornameAnderer) {
        this.vornameAnderer = vornameAnderer;
    }

    public String getNachnameAnderer() {
        return nachnameAnderer;
    }

    public void setNachnameAnderer(String nachnameAnderer) {
        this.nachnameAnderer = nachnameAnderer;
    }
}
