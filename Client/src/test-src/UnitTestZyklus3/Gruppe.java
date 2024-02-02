package UnitTestZyklus3;

import Controller.ClientServerHandler;
import client.client.Soziales.Discussion.Disc_SearchController;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Gruppe {
    ClientServerHandler csh;
    Disc_SearchController discsearch;

    String discussionsBEFORE;
    String participantsBEFORE;
    String searchBEFORE;

    String discussionsAFTER;
    String participantsAFTER;
    String searchAFTER;

    String testperson1="testUser1@test.de";
    String testperson2="testUser2@test.de";

    JSONObject discussion1;
    JSONObject discussion2;


    public Gruppe() throws MalformedURLException {
    }

    @Before
    public void setUP() throws IOException, InterruptedException {
        csh=new ClientServerHandler();
        discsearch=new Disc_SearchController();

        //Parameter des Controllers anpassen
        discsearch.wirdgetestet=true;
        discsearch.email=testperson2;

        //erstellen von Gruppen
        discussion1=new JSONObject();
        discussion1.put("name", "MODULTEST1");
        discussion1.put("isprivate", false);
        discussion1.put("creatorMail", testperson1);
        discussion1.put("creatorName","testtest");

        discussion2=new JSONObject();
        discussion2.put("name", "MODULTEST2");
        discussion2.put("isprivate", false);
        discussion2.put("creatorMail", testperson1);
        discussion2.put("creatorName","testtest");
        csh.sendPostRequest("/controller/diskussion/gruppe/add",discussion1);
        csh.sendPostRequest("/controller/diskussion/gruppe/add",discussion2);
    }

    @Test
    public void gruppeTesten() throws IOException, InterruptedException {
        //welche Gruppen vorher angezeigt werden dürfen und welche Teilnehmer es vorher insgesamt gibt
        discsearch.initialize();
        discussionsBEFORE=discsearch.allowedDiscussions.toString();
        participantsBEFORE=csh.sendGETRequest("/controller/diskussion/teilnehmer").toString();
        discsearch.searchDisc("TEST1");
        searchBEFORE=discsearch.searchTest.toString();

        //um an die ID zum Beitreten zu kommen (wird Serverseitig automatisch generiert!)
        JSONObject discussiontojoin=csh.sendGETRequest("/controller/diskussion/gruppe/bySpecificGroup/"+ false+"/"+discussion1.getString("name")+"/"+discussion1.getString("creatorMail")).getJSONObject(0);

        //"testUser2@test.de" tretet Gruppe "MODULTEST1" bei
        discsearch.joinDisc(discussiontojoin.getInt("id"));
        discussionsAFTER=discsearch.allowedDiscussions.toString();
        participantsAFTER=csh.sendGETRequest("/controller/diskussion/teilnehmer").toString();
        discsearch.searchDisc("TEST1");
        searchAFTER=discsearch.searchTest.toString();

        //die Arrays sollten NICHT identisch sein, da einerseits die testperson2 ein neuer Teinehmer ist
        //und anderrseits die angezeigten Gruppen nun anders sein sollten.
        assertNotEquals(discussionsBEFORE, discussionsAFTER);
        assertNotEquals(participantsBEFORE, participantsAFTER);
        assertNotEquals(searchBEFORE, searchAFTER);
    }

    @After
    public void teardown() throws IOException, InterruptedException {
        //zur Veranschaulichung:
        System.out.println("\nAb hier beginnt der TearDown:");
        System.out.println("\nHier die Diskussionsgruppen:");
        System.out.println(discussionsBEFORE);
        System.out.println(discussionsAFTER);
        System.out.println("\nHier die Teilnehmer:");
        System.out.println(participantsBEFORE);
        System.out.println(participantsAFTER+"\n");
        System.out.println("\nHier die Suchergebnisse:");
        System.out.println(searchBEFORE);
        if(searchAFTER.isEmpty()){
            System.out.println("----Suche ist leer-----\n");
        }else{
            System.out.println(searchAFTER+"\n");
        }
        
        //löscht alle neu getätigten Datenbank-Einpflegungen, für eine saubere Datenbank
        String grouptodelete1=csh.sendGETRequest("/controller/diskussion/gruppe/bySpecificGroup/"+false+"/"+"MODULTEST1"+"/"+testperson1).getJSONObject(0).get("id").toString();
        String grouptodelete2=csh.sendGETRequest("/controller/diskussion/gruppe/bySpecificGroup/"+false+"/"+"MODULTEST2"+"/"+testperson1).getJSONObject(0).get("id").toString();

        csh.sendDeleteRequest("/controller/diskussion/gruppe/deleteByID/"+grouptodelete1);
        csh.sendDeleteRequest("/controller/diskussion/gruppe/deleteByID/"+grouptodelete2);
        csh.sendDeleteRequest("/controller/diskussion/teilnehmer/delete/"+grouptodelete1+"/"+testperson2);
    }
}
