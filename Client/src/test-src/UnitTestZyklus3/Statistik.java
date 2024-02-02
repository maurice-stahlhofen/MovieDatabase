package UnitTestZyklus3;

import Controller.ClientServerHandler;
import client.client.FilmVerwalten.FilmVerwaltenController;
import client.client.Statistik.StatistikADMINController;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Statistik {
    ClientServerHandler csh = new ClientServerHandler();
    FilmVerwaltenController bewerter;
    Statistik gesehen;

    int erwarteteAnzahl;
    double erwarteterDurchschnitt;

    public Statistik() throws MalformedURLException {
    }

    @Before
    public void setUP() throws IOException, InterruptedException {
        ClientServerHandler csh = new ClientServerHandler();
        bewerter = new FilmVerwaltenController();
        bewerter.testMethod(5, "", 1, "user1@test.de");
        bewerter.testMethod(4, "", 1, "user2@test.de");
        bewerter.testMethod(3, "", 1, "user3@test.de");
        erwarteterDurchschnitt = 4.0;

        gesehen = new Statistik();
        gesehen.testMethod("2022-06-27", 1, "user1@test.de");
        gesehen.testMethod("2022-06-27", 1, "user2@test.de");
        gesehen.testMethod("2022-06-27", 1, "user3@test.de");
        erwarteteAnzahl = 3;

    }


    @Test
    public void statistikTesten() throws IOException {
        StatistikADMINController statistik = new StatistikADMINController();
        int anzahlStatistik = statistik.getAnzahlGesehenerFilme(1);

        JSONArray bewertungen = csh.sendGETRequest("/controller/bewertung/" + "1");
        double durchschnittStatistik = statistik.getSummeVonSternen(bewertungen) / statistik.getAnzahlBewertungen(1);

        assertEquals(erwarteterDurchschnitt, durchschnittStatistik);
        assertEquals(erwarteteAnzahl, anzahlStatistik);

    }

    public JSONObject testMethod(String date, Integer filmId, String email) throws IOException, InterruptedException {
        JSONArray allFilms = csh.sendGETRequest("/controller/film/id/" + filmId);
        JSONObject film = allFilms.getJSONObject(0);
        JSONObject gesehen = new JSONObject();
        gesehen.put("date", date);
        gesehen.put("filmId", film);
        gesehen.put("emailNutzer", email);


        csh.sendPostRequest("/controller/geseheneFilme/add", gesehen);

        return gesehen;
    }
   }
