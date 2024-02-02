package client.client.Vorschlaege;

import Controller.ClientServerHandler;
import client.client.login.AngemeldeterUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class VorschlagController implements Initializable {
    @FXML
    public Button watchlistButton, startseiteButton, updateButton;

    public ComboBox box;

    public ListView<String> filmVorschlaege;

    ObservableList vorschlaege = FXCollections.observableArrayList();
    Alert popUp;

    private ClientServerHandler handler = new ClientServerHandler();

    public VorschlagController() throws MalformedURLException {
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        box.getItems().add("Eigene Filme");
        box.getItems().add("Filme der Freunde");
        box.getSelectionModel().select(0);
        try {
            initListSelbst();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initListSelbst() throws IOException{
        JSONArray alleFilme = handler.sendGETRequest("/controller/film");
        JSONArray alleGesehenen = handler.sendGETRequest("/controller/geseheneFilme");
        JSONArray meineGesehenen = handler.sendGETRequest("/controller/geseheneFilme/email/" + AngemeldeterUser.getSpeicher().getEmail());
        JSONArray zuletztGesehene = new JSONArray();

        JSONObject aktuellerFilm;

        //Holt die 10 letzt gesehenen Film von sich Selst
        int count =10;
        for(int i=meineGesehenen.length()-1; i>=0; i--){
            if(count > 0 && meineGesehenen.getJSONObject(i) != null) {
                aktuellerFilm = meineGesehenen.getJSONObject(i);
                zuletztGesehene.put(aktuellerFilm);
                count--;
            }
            else{
                break;
            }
        }
        //System.out.println(zuletztGesehene.length());
        if(zuletztGesehene.length() != 0) {
            erstelleVorschläge(zuletztGesehene, false);
        }
        else{
            popUp = new Alert(Alert.AlertType.ERROR);
            popUp.setHeaderText("Fehler!");
            popUp.setContentText("Keine Filme gesehen");
            popUp.showAndWait();
        }
    }
    private void initListFreunde() throws IOException{
        JSONArray alleFilme = handler.sendGETRequest("/controller/film");
        JSONArray freundesliste = handler.sendGETRequest("/controller/freundesliste/byEmail/" +AngemeldeterUser.getSpeicher().getEmail());
        JSONArray alleGesehenen = handler.sendGETRequest("/controller/geseheneFilme");
        JSONArray alleGeseheneFreunde = new JSONArray();
        JSONArray zuletztGesehene = new JSONArray();

        JSONObject aktuellerFilm;
        JSONObject aktuellerFreund;

        for(int i =0; i < freundesliste.length(); i++) {
            aktuellerFreund = freundesliste.getJSONObject(i);
            if (aktuellerFreund.get("emailNutzer").toString().equals(AngemeldeterUser.getSpeicher().getEmail())) {
                JSONArray zuletztDesFreundes = handler.sendGETRequest("/controller/geseheneFilme/email/" + aktuellerFreund.get("emailFreund").toString());
                int counter = 10;
                for(int j = zuletztDesFreundes.length()-1; j >= 0;j--){
                    if(counter > 0){
                        aktuellerFilm = zuletztDesFreundes.getJSONObject(j);
                        alleGeseheneFreunde.put(aktuellerFilm);
                        counter--;
                    }
                    else{
                        j =0;
                    }
                }
            }
        }
        if(alleGeseheneFreunde.length() != 0) {
            erstelleVorschläge(alleGeseheneFreunde, false);
        }
        else{
            popUp = new Alert(Alert.AlertType.ERROR);
            popUp.setHeaderText("Fehler!");
            popUp.setContentText("Keine Filme gesehen");
            popUp.showAndWait();
        }
    }
    private JSONArray erstelleVorschläge(JSONArray zuletztGesehene, boolean wirdgetestet) throws IOException {
        JSONArray filmeZumVorschlag = new JSONArray();
        JSONObject aktuellerFilm;
        JSONArray alleFilme = handler.sendGETRequest("/controller/film");
        List<VorschlagNode> vorschlagKategorieList = new LinkedList<>();
        List<String> listKategorie = new LinkedList<>();

        for (int i = 0; i<zuletztGesehene.length(); i++) {  //Zieht Kategorie aus Filmen
            JSONObject film = zuletztGesehene.getJSONObject(i);
            aktuellerFilm = film.getJSONObject("filmId");
            String kategorie = aktuellerFilm.get("kategorie").toString();
            String[] kategorieList = kategorie.split(", ");
            int bewertung = 1;

            JSONArray bewertungen = handler.sendGETRequest("/controller/bewertung/" + aktuellerFilm.get("filmId")); //Holt Bewertung für jeden Film
            if(!bewertungen.isEmpty()) {
                for (int j = 0; j < bewertungen.length(); j++) {
                    JSONObject bewerteterFilm = bewertungen.getJSONObject(j);
                    if (bewerteterFilm.get("nutzerEmail").toString().equals(AngemeldeterUser.getSpeicher().getEmail())) {
                        bewertung = Integer.parseInt(bewerteterFilm.get("sterne").toString());
                    }
                }
            }

            for(int c = 0; c < kategorieList.length; c++) {  //Ordnet Bewertung der Kategorien zu
                boolean done = false;
                for (VorschlagNode node : vorschlagKategorieList) {
                    if (node.getName().matches(kategorieList[c].toString())) {
                        node.addCount(bewertung);
                        done = true;
                    }
                }
                if (done == false) {
                    VorschlagNode node = new VorschlagNode(kategorieList[c].toString());
                    node.addCount(bewertung);
                    vorschlagKategorieList.add(node);
                }
            }

        }
        //System.out.println(vorschlagKategorieList);
        for (VorschlagNode node : vorschlagKategorieList) {
            System.out.println(node.getName() + " - " + node.getCount());
        }

        while(!vorschlagKategorieList.isEmpty()) {  //Ranked die einzelnen Kategorien. Ordnet listKategorie
            VorschlagNode kategorieNode = null;
            for (int i = 0; i < vorschlagKategorieList.size(); i++) {
                if (i == 0) {
                    kategorieNode = vorschlagKategorieList.get(i);
                } else {
                    if (kategorieNode.getCount() < vorschlagKategorieList.get(i).getCount()) {
                        kategorieNode = vorschlagKategorieList.get(i);
                    }
                }
            }
            listKategorie.add(kategorieNode.getName());
            vorschlagKategorieList.remove(kategorieNode);
        }

        System.out.println(listKategorie+": relevante Kategorien");
        //System.out.println(filmeZumVorschlag.length());

        System.out.println(listKategorie.size()+" Kategorie(n)");

        //Gesehene Filme werden herausgefiltert
        for(int i = 0; i<alleFilme.length(); i++) {
            JSONObject film = alleFilme.getJSONObject(i);
            for (int k = 0; k < zuletztGesehene.length(); k++) {
                if (film.getInt("filmId") == (zuletztGesehene.getJSONObject(k).getJSONObject("filmId").getInt("filmId"))) {
                    alleFilme.remove(i);
                    //damit der nächste Film nicht übersprungen wird, da sich die länge verändert:
                    i--;
                }
            }
        }

        //Erstellt Vorschläge Array
        for(int i = 0; i<alleFilme.length(); i++) {
            JSONObject film = alleFilme.getJSONObject(i);
            switch (listKategorie.size()) {
                case 1:
                    if (film.get("kategorie").toString().contains(listKategorie.get(0))) {
                        filmeZumVorschlag.put(film);
                    }
                    break;
                case 2:
                    if (film.get("kategorie").toString().contains(listKategorie.get(0)) ||
                            film.get("kategorie").toString().contains(listKategorie.get(1))) {
                        filmeZumVorschlag.put(film);
                    }
                    break;
                default:
                    if (film.get("kategorie").toString().contains(listKategorie.get(0)) ||
                            film.get("kategorie").toString().contains(listKategorie.get(1)) ||
                            film.get("kategorie").toString().contains(listKategorie.get(2))) {
                        filmeZumVorschlag.put(film);
                    }
                    break;
            }
        }

        JSONArray forTest = new JSONArray();
        //Erstellt ListView
        int counter = 0;
        Random rnd = new Random();

            //length!=0, da ansonsten random einen Fehler wirft und ansonsten bei weniger als 15 vorhandenen Filmen in der Liste
            //eine Dauerschleife wegen counter entstehen kann
            while (counter <15 && filmeZumVorschlag.length()!=0) {
                int random = rnd.nextInt(filmeZumVorschlag.length());

                aktuellerFilm = filmeZumVorschlag.getJSONObject(random);
                forTest.put(aktuellerFilm);
                vorschlaege.add(aktuellerFilm.get("filmId").toString() + " " + aktuellerFilm.get("name").toString() + "   " + aktuellerFilm.get("kategorie").toString() +
                        "   " + aktuellerFilm.get("erscheinungsdatum").toString());
                filmeZumVorschlag.remove(random);
                counter++;
            }

        if(!wirdgetestet) {
            if (vorschlaege.isEmpty()) {
                filmVorschlaege.getItems().add("Keine Vorschläge bitte Filme gucken");
            }
            filmVorschlaege.getItems().addAll(vorschlaege);
        }

        if(wirdgetestet){
            return forTest;
        }
        return null;
    }


    public void updateList(ActionEvent actionEvent) {
        filmVorschlaege.getItems().clear();
        vorschlaege.clear();
        if(box.getSelectionModel().getSelectedItem() == null){
            popUp = new Alert(Alert.AlertType.ERROR);
            popUp.setHeaderText("Fehler!");
            popUp.setContentText("Bitte Basis in Checkbox auswählen");
            popUp.showAndWait();
        }
        else if(box.getSelectionModel().getSelectedItem().equals("Eigene Filme")&& box.getSelectionModel().getSelectedItem() != null) {
            try {
                initListSelbst();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            try {
                initListFreunde();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void startseitePressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startseiteButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void toWatchlist(ActionEvent actionEvent) throws IOException, InterruptedException {
        JSONArray watchlist = new JSONArray();
        watchlist = handler.sendGETRequest("/controller/watchlist");


        if (filmVorschlaege.getSelectionModel().getSelectedItem() == null) {
            popUp = new Alert(Alert.AlertType.ERROR);
            popUp.setHeaderText("Fehler!");
            popUp.setContentText("Bitte Film in Liste auswählen");
            popUp.showAndWait();
            return;
        }

        if(filmVorschlaege.getSelectionModel().getSelectedItem().equals("Keine Vorschläge bitte Filme gucken")){
            popUp = new Alert(Alert.AlertType.ERROR);
            popUp.setHeaderText("Fehler!");
            popUp.setContentText("Bitte Film in Liste auswählen");
            popUp.showAndWait();
            return;
        }

        String IDdesFilms = cropID(filmVorschlaege.getSelectionModel().getSelectedItem().toString());

        JSONObject watchlisteintrag = new JSONObject();
        watchlisteintrag.put("emailNutzer", AngemeldeterUser.getSpeicher().getEmail());
        watchlisteintrag.put("filmId", IDdesFilms);

        String response = handler.sendGETRequestGetString("/controller/geseheneFilme/checkDuplicate/"+IDdesFilms+"/"+AngemeldeterUser.getSpeicher().getEmail());

        if(response.equals("true")){
            popUp = new Alert(Alert.AlertType.ERROR);
            popUp.setHeaderText("Fehler");
            popUp.setContentText("Dieser Film wurde bereits als gesehen markiert");
            popUp.showAndWait();
            return;
        }

        for (int i = 0; i < watchlist.length(); i++) {
            JSONObject tmp = watchlist.getJSONObject(i);
            if (tmp.get("emailNutzer").toString().equals(watchlisteintrag.get("emailNutzer").toString()) && tmp.get("filmId").toString().equals(watchlisteintrag.get("filmId").toString())) {
                popUp = new Alert(Alert.AlertType.ERROR);
                popUp.setHeaderText("Fehler");
                popUp.setContentText("Dieser Film befindet sich bereits auf ihrer Watchlist");
                popUp.showAndWait();
                return;
            }
        }

        handler.sendPostRequest("/controller/watchlist/add", watchlisteintrag);
        popUp = new Alert(Alert.AlertType.ERROR);
        popUp.setHeaderText("Erfolg");
        popUp.setContentText("Film erfolgreich zur Watchlist hinzugefügt");
        popUp.showAndWait();
    }

    private String cropID(String toCrop) {
        String ID = "";
        int counter;

        for (int i = 0; i < toCrop.length(); i++) {
            if (toCrop.charAt(i) == 32) {
                break;
            }
            ID = ID + toCrop.charAt(i);
        }
        return ID;
    }
    public JSONArray testMethod(JSONObject film, JSONArray alleFilme) throws IOException, InterruptedException {
        JSONArray vorschlaege = new JSONArray();
        JSONArray gesehenTest = new JSONArray();

        JSONObject gesehen=new JSONObject();
        gesehen.put("date", LocalDate.now());
        gesehen.put("emailNutzer", "m@m.de");
        gesehen.put("filmId", film);
        handler.sendPostRequest("/controller/geseheneFilme/add", gesehen);
        JSONArray allegesehen=handler.sendGETRequest("/controller/geseheneFilme");
        gesehen.put("geseheneFilmeId", allegesehen.getJSONObject(allegesehen.length()-1).getInt("geseheneFilmeId"));

        gesehenTest.put(gesehen);
        System.out.println("Gesehen: "+gesehenTest);
        vorschlaege = erstelleVorschläge(gesehenTest, true);

        return vorschlaege;
    }
}
