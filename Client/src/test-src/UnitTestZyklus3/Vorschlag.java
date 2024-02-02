package UnitTestZyklus3;

import Controller.ClientServerHandler;
import client.client.Vorschlaege.VorschlagController;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Vorschlag {
    VorschlagController vcr;
    ClientServerHandler handler = new ClientServerHandler();
    JSONObject film1 = new JSONObject();
    JSONObject film2 = new JSONObject();
    JSONObject film3 = new JSONObject();

    public Vorschlag() throws MalformedURLException {
    }

    @Before
    public void setUP() throws IOException, InterruptedException {

        film1 = testMethod("film1", "Sad");
        film2 = testMethod("film2", "Sad");
        film3 = testMethod("film3", "Sad");

        handler.sendPostRequest("/controller/film/add", film1);
        handler.sendPostRequest("/controller/film/add", film2);
        handler.sendPostRequest("/controller/film/add", film3);
    }

    @Test
    public void vorschlagTest() throws IOException, InterruptedException {
        vcr = new VorschlagController();
        JSONArray alleFilme = handler.sendGETRequest("/controller/film");
        film1.put("filmId", alleFilme.getJSONObject(alleFilme.length()-3).getInt("filmId"));
        film2.put("filmId", alleFilme.getJSONObject(alleFilme.length()-2).getInt("filmId"));
        film3.put("filmId", alleFilme.getJSONObject(alleFilme.length()-1).getInt("filmId"));
        JSONArray vorschlaege = vcr.testMethod(film1, alleFilme);
        boolean kategorieOk = true;

        for(int i = 0; i < vorschlaege.length(); i++){
            JSONObject aktuellerFilm = vorschlaege.getJSONObject(i);
            if(!aktuellerFilm.get("kategorie").toString().equals("Sad")){
                kategorieOk = false;
            }
        }
        assertTrue(kategorieOk);

        for(int i=0; i<vorschlaege.length(); i++){
            JSONObject found=vorschlaege.getJSONObject(i);
            System.out.println("Vorschlag: "+found.getString("name")+", "+found.getString("kategorie"));
        }

    }

    private JSONObject testMethod(String name, String kategorie){
        JSONObject film = new JSONObject();
        film.put("name", name);
        film.put("kategorie", kategorie);
        return film;
    }
    @After
    public void teardown() throws IOException, InterruptedException {
        System.out.println("\nTeardown beginnt: \n");
        JSONArray geseheneFilme=handler.sendGETRequest("/controller/geseheneFilme");
        handler.sendDeleteRequest("/controller/geseheneFilme/deleteTEST/"+geseheneFilme.getJSONObject(geseheneFilme.length()-1).getInt("geseheneFilmeId"));

        handler.sendDeleteRequest("/controller/film/delete/"+film1.getInt("filmId"));
        handler.sendDeleteRequest("/controller/film/delete/"+film2.getInt("filmId"));
        handler.sendDeleteRequest("/controller/film/delete/"+film3.getInt("filmId"));
    }

}