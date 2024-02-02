import Controller.ClientServerHandler;
import client.client.FilmVerwalten.FilmVerwaltenController;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;
public class ErsterUnittest {
    FilmVerwaltenController bewerter;

    @BeforeAll
    public void setUp() throws MalformedURLException {
        ClientServerHandler handler = new ClientServerHandler();

    }

    @Test
    @DisplayName("Test")
    public void bewertenTesten() throws IOException, InterruptedException {
        ClientServerHandler handler = new ClientServerHandler();
        JSONObject bewertung = new JSONObject();
        bewertung.put("filmId", 1);
        bewertung.put("sterne", 2);
        bewertung.put("nutzerEmail", "m@m.de");
        handler.sendPostRequest("/controller/bewertung/add", bewertung);
        bewerter = new FilmVerwaltenController();

        JSONArray bewertungen = new JSONArray();
        bewertungen = handler.sendGETRequest("/controller/bewertung");

        JSONObject bewertungTest = bewertungen.getJSONObject(bewertungen.length()-1);
        Assertions.assertTrue(bewertung.equals(bewertungTest));
    }
}
