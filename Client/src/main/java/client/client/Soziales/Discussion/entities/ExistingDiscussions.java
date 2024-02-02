package client.client.Soziales.Discussion.entities;

import Controller.ClientServerHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

public class ExistingDiscussions {
    private Integer ID;
    private String name;
    private boolean isPrivate;
    private String creatorMail;
    private String creatorName;
    private Integer teilnehmerZahl=0;

    private ClientServerHandler csh=new ClientServerHandler();

    public ExistingDiscussions() throws MalformedURLException {
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getCreatorMail() {
        return creatorMail;
    }

    public void setCreatorMail(String creatorMail) {
        this.creatorMail = creatorMail;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Integer getTeilnehmerZahl() {
        return teilnehmerZahl;
    }

    public void setTeilnehmerZahl(Integer teilnehmerZahl) {
        this.teilnehmerZahl = teilnehmerZahl;
    }

    public void initializeTeilnehmerAnzahl() throws IOException {
        JSONArray alleTeilnehmer=csh.sendGETRequest("/controller/diskussion/teilnehmer");
        for(int i=0; i<alleTeilnehmer.length(); i++){
            JSONObject teilnehmer=alleTeilnehmer.getJSONObject(i);
            Integer groupID=Integer.parseInt(teilnehmer.get("groupID").toString());
            if(groupID.equals(this.getID())){
                this.teilnehmerZahl++;
            }
        }
    }

    @Override
    public String toString() {
        String privat;
        String creator;
        if(isPrivate){
            privat="Privat;         ";
            creator= "; Von "+creatorName;
        }else{
            privat="Öffentlich; ";
            creator="";
        }

        return privat+" "+
                teilnehmerZahl+ " Teilnehmer"+ "; \""+
                name+"\""+creator;
    }
}
