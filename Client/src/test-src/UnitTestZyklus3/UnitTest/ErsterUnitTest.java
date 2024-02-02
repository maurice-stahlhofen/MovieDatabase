package UnitTestZyklus3.UnitTest;

import Controller.ClientServerHandler;
import client.client.FilmVerwalten.FilmVerwaltenController;
import client.client.Zwischenspeicher;
import client.client.login.AngemeldeterUser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErsterUnitTest {
    FilmVerwaltenController bewerter;
    JSONObject bewertung;
    JSONObject bewertungTest;

    public ErsterUnitTest() throws MalformedURLException {
    }
    @Before
    public void setUp() throws MalformedURLException {
        ClientServerHandler csh=new ClientServerHandler();
        AngemeldeterUser.getSpeicher().setEmail("m@m.de");
        Zwischenspeicher.getSpeicher().setFilmId(1);
    }
    @Test
    public void bewertenTesten() throws IOException, InterruptedException {
        Integer filmId=1;
        String nutzerEmail="m@m.de";
        //schickt Test-Entität raus
        bewerter = new FilmVerwaltenController();
        bewerter.testMethod(2,"war cool", filmId, nutzerEmail);
        //überprüft, ob in Datenbank angekommen
        ClientServerHandler handler = new ClientServerHandler();
        JSONArray bewertungen = handler.sendGETRequest("/controller/bewertung");

        for(int i=0; i<bewertungen.length(); i++){
            JSONObject found=bewertungen.getJSONObject(i);
            JSONObject foundfilm=found.getJSONObject("filmId");
            Integer filmid=foundfilm.getInt("filmId");
            String email=found.get("nutzerEmail").toString();

            if(filmid.equals(filmId) && nutzerEmail.equals(email)){
                bewertungTest=found;  break;
            }}
        //hier remove, da die Datenbank jeder Bewertung eine automatische ID verleiht,
        // die unser erstelltes Object noch nicht besitzt
        bewertungTest.remove("bewertungsId");
    }
    @After
    public void teardown(){
        //hier zu String, da das assertEquals einen "Fail" wirft, obwohl die Objekte den gleichen Inhalt haben
        String bewertung1=bewertung.toString();
        String bewertung2=bewertungTest.toString();
        assertEquals(bewertung1, bewertung2);
    }
}

