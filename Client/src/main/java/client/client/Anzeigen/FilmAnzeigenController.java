package client.client.Anzeigen;

import Controller.ClientServerHandler;
import client.client.login.AngemeldeterUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import client.client.Zwischenspeicher;

import java.io.IOException;
import java.time.LocalDate;

public class FilmAnzeigenController {


    @FXML
    public Button startseiteButton, filternButton, bewertungenButton, filmBewertenButton, filmGesehenButton, watchlistButton;

    public TextField kategorieField, erscheinungsdatumField, castField, nameField;

    public ListView filmliste;

    public Text filternNachText, filmAnzeigenText;

    ClientServerHandler CShandler = new ClientServerHandler();

    ObservableList filme = FXCollections.observableArrayList();

    Alert popUp = new Alert(Alert.AlertType.INFORMATION);
    ClientServerHandler csh = new ClientServerHandler();
    JSONArray alleNutzer = new JSONArray();
    JSONObject angemeldeterUser, filmAuswahl;

    public FilmAnzeigenController() throws IOException {

    }

    @FXML
    private void initialize() throws IOException {

        insertList();
    }

    public void insertList() throws IOException {
        filmliste.getItems().clear();
        filme.clear();
        filme.removeAll(filmliste);
        JSONArray filmearray = CShandler.sendGETRequest("/controller/film");
        for (int i = 0; i < filmearray.length(); i++) {
            JSONObject tmp = filmearray.getJSONObject(i);
            filme.add(tmp.get("filmId").toString() + " " + tmp.get("name").toString() + ", {" + tmp.get("kategorie").toString() + "}, " + tmp.get("filmlaenge").toString() + " Minuten, " + tmp.get("erscheinungsdatum").toString() + ", " + tmp.get("regisseur").toString() + ", " + tmp.get("cast").toString());
        }

        filmliste.getItems().addAll(filme);

    }


    public void filmeFiltern() throws IOException {
        filmliste.getItems().clear();
        filme.clear();
        filme.removeAll(filmliste);
        JSONArray filmearray = CShandler.sendGETRequest("/controller/film");

        if (kategorieField.getText() == "" && erscheinungsdatumField.getText() == "" && castField.getText() == "" && nameField.getText() == "") {
            for (int i = 0; i < filmearray.length(); i++) {
                JSONObject tmp = filmearray.getJSONObject(i);
                filme.add(tmp.get("filmId").toString() + " " + tmp.get("name").toString() + ", {" + tmp.get("kategorie").toString() + "}, " + tmp.get("filmlaenge").toString() + " Minuten, " + tmp.get("erscheinungsdatum").toString() + ", " + tmp.get("regisseur").toString() + ", " + tmp.get("cast").toString());
            }
        }

        //Kategorie Filter
        if (kategorieField.getText() != "" && erscheinungsdatumField.getText() == "" && castField.getText() == "" && nameField.getText() == "") {
            for (int i = 0; i < filmearray.length(); i++) {
                JSONObject tmp = filmearray.getJSONObject(i);

                if (tmp.get("kategorie").toString().contains(kategorieField.getText())) {
                    filme.add(tmp.get("filmId").toString() + " " + tmp.get("name").toString() + ", {" + tmp.get("kategorie").toString() + "}, " + tmp.get("filmlaenge").toString() + " Minuten, " + tmp.get("erscheinungsdatum").toString() + ", " + tmp.get("regisseur").toString() + ", " + tmp.get("cast").toString());
                }
            }
        }

        //erscheinungsdatum Filter
        if (kategorieField.getText() == "" && erscheinungsdatumField.getText() != "" && castField.getText() == "" && nameField.getText() == "") {
            for (int i = 0; i < filmearray.length(); i++) {
                JSONObject tmp = filmearray.getJSONObject(i);

                if (tmp.get("erscheinungsdatum").toString().contains(erscheinungsdatumField.getText())) {
                    filme.add(tmp.get("filmId").toString() + " " + tmp.get("name").toString() + ", {" + tmp.get("kategorie").toString() + "}, " + tmp.get("filmlaenge").toString() + " Minuten, " + tmp.get("erscheinungsdatum").toString() + ", " + tmp.get("regisseur").toString() + ", " + tmp.get("cast").toString());
                }
            }
        }

        //cast Filter
        if (kategorieField.getText() == "" && erscheinungsdatumField.getText() == "" && castField.getText() != "" && nameField.getText() == "") {
            for (int i = 0; i < filmearray.length(); i++) {
                JSONObject tmp = filmearray.getJSONObject(i);

                if (tmp.get("cast").toString().contains(castField.getText())) {
                    filme.add(tmp.get("filmId").toString() + " " + tmp.get("name").toString() + ", {" + tmp.get("kategorie").toString() + "}, " + tmp.get("filmlaenge").toString() + " Minuten, " + tmp.get("erscheinungsdatum").toString() + ", " + tmp.get("regisseur").toString() + ", " + tmp.get("cast").toString());
                }
            }
        }

        //name Filter
        if (kategorieField.getText() == "" && erscheinungsdatumField.getText() == "" && castField.getText() == "" && nameField.getText() != "") {
            for (int i = 0; i < filmearray.length(); i++) {
                JSONObject tmp = filmearray.getJSONObject(i);

                if (tmp.get("name").toString().contains(nameField.getText())) {
                    filme.add(tmp.get("filmId").toString() + " " + tmp.get("name").toString() + ", {" + tmp.get("kategorie").toString() + "}, " + tmp.get("filmlaenge").toString() + " Minuten, " + tmp.get("erscheinungsdatum").toString() + ", " + tmp.get("regisseur").toString() + ", " + tmp.get("cast").toString());
                }
            }
        }

        //kategorie und datum filter
        if (kategorieField.getText() != "" && erscheinungsdatumField.getText() != "" && castField.getText() == "" && nameField.getText() == "") {
            for (int i = 0; i < filmearray.length(); i++) {
                JSONObject tmp = filmearray.getJSONObject(i);

                if (tmp.get("kategorie").toString().contains(kategorieField.getText()) && tmp.get("erscheinungsdatum").toString().contains(erscheinungsdatumField.getText())) {
                    filme.add(tmp.get("filmId").toString() + " " + tmp.get("name").toString() + ", {" + tmp.get("kategorie").toString() + "}, " + tmp.get("filmlaenge").toString() + " Minuten, " + tmp.get("erscheinungsdatum").toString() + ", " + tmp.get("regisseur").toString() + ", " + tmp.get("cast").toString());
                }
            }
        }

        //kategorie und cast filter
        if (kategorieField.getText() != "" && erscheinungsdatumField.getText() == "" && castField.getText() != "" && nameField.getText() == "") {
            for (int i = 0; i < filmearray.length(); i++) {
                JSONObject tmp = filmearray.getJSONObject(i);

                if (tmp.get("kategorie").toString().contains(kategorieField.getText()) && tmp.get("cast").toString().contains(castField.getText())) {
                    filme.add(tmp.get("filmId").toString() + " " + tmp.get("name").toString() + ", {" + tmp.get("kategorie").toString() + "}, " + tmp.get("filmlaenge").toString() + " Minuten, " + tmp.get("erscheinungsdatum").toString() + ", " + tmp.get("regisseur").toString() + ", " + tmp.get("cast").toString());
                }
            }
        }

        //kategorie und name filter
        if (kategorieField.getText() != "" && erscheinungsdatumField.getText() == "" && castField.getText() == "" && nameField.getText() != "") {
            for (int i = 0; i < filmearray.length(); i++) {
                JSONObject tmp = filmearray.getJSONObject(i);

                if (tmp.get("kategorie").toString().contains(kategorieField.getText()) && tmp.get("name").toString().contains(nameField.getText())) {
                    filme.add(tmp.get("filmId").toString() + " " + tmp.get("name").toString() + ", {" + tmp.get("kategorie").toString() + "}, " + tmp.get("filmlaenge").toString() + " Minuten, " + tmp.get("erscheinungsdatum").toString() + ", " + tmp.get("regisseur").toString() + ", " + tmp.get("cast").toString());
                }
            }
        }

        //datum und cast
        if (kategorieField.getText() == "" && erscheinungsdatumField.getText() != "" && castField.getText() != "" && nameField.getText() == "") {
            for (int i = 0; i < filmearray.length(); i++) {
                JSONObject tmp = filmearray.getJSONObject(i);

                if (tmp.get("erscheinungsdatum").toString().contains(erscheinungsdatumField.getText()) && tmp.get("cast").toString().contains(castField.getText())) {
                    filme.add(tmp.get("filmId").toString() + " " + tmp.get("name").toString() + ", {" + tmp.get("kategorie").toString() + "}, " + tmp.get("filmlaenge").toString() + " Minuten, " + tmp.get("erscheinungsdatum").toString() + ", " + tmp.get("regisseur").toString() + ", " + tmp.get("cast").toString());
                }
            }
        }

        //datum und name
        if (kategorieField.getText() == "" && erscheinungsdatumField.getText() != "" && castField.getText() == "" && nameField.getText() != "") {
            for (int i = 0; i < filmearray.length(); i++) {
                JSONObject tmp = filmearray.getJSONObject(i);

                if (tmp.get("erscheinungsdatum").toString().contains(erscheinungsdatumField.getText()) && tmp.get("name").toString().contains(nameField.getText())) {
                    filme.add(tmp.get("filmId").toString() + " " + tmp.get("name").toString() + ", {" + tmp.get("kategorie").toString() + "}, " + tmp.get("filmlaenge").toString() + " Minuten, " + tmp.get("erscheinungsdatum").toString() + ", " + tmp.get("regisseur").toString() + ", " + tmp.get("cast").toString());
                }
            }
        }

        //cast und name
        if (kategorieField.getText() == "" && erscheinungsdatumField.getText() == "" && castField.getText() != "" && nameField.getText() != "") {
            for (int i = 0; i < filmearray.length(); i++) {
                JSONObject tmp = filmearray.getJSONObject(i);

                if (tmp.get("cast").toString().contains(castField.getText()) && tmp.get("name").toString().contains(nameField.getText())) {
                    filme.add(tmp.get("filmId").toString() + " " + tmp.get("name").toString() + ", {" + tmp.get("kategorie").toString() + "}, " + tmp.get("filmlaenge").toString() + " Minuten, " + tmp.get("erscheinungsdatum").toString() + ", " + tmp.get("regisseur").toString() + ", " + tmp.get("cast").toString());
                }
            }
        }

        //kategorie datum cast filter
        if (kategorieField.getText() != "" && erscheinungsdatumField.getText() != "" && castField.getText() != "" && nameField.getText() == "") {
            for (int i = 0; i < filmearray.length(); i++) {
                JSONObject tmp = filmearray.getJSONObject(i);

                if (tmp.get("kategorie").toString().contains(kategorieField.getText()) && tmp.get("erscheinungsdatum").toString().contains(erscheinungsdatumField.getText()) && tmp.get("cast").toString().contains(castField.getText())) {
                    filme.add(tmp.get("filmId").toString() + " " + tmp.get("name").toString() + ", {" + tmp.get("kategorie").toString() + "}, " + tmp.get("filmlaenge").toString() + " Minuten, " + tmp.get("erscheinungsdatum").toString() + ", " + tmp.get("regisseur").toString() + ", " + tmp.get("cast").toString());
                }
            }
        }

        //datum cast name filter
        if (kategorieField.getText() == "" && erscheinungsdatumField.getText() != "" && castField.getText() != "" && nameField.getText() != "") {
            for (int i = 0; i < filmearray.length(); i++) {
                JSONObject tmp = filmearray.getJSONObject(i);

                if (tmp.get("erscheinungsdatum").toString().contains(erscheinungsdatumField.getText()) && tmp.get("cast").toString().contains(castField.getText()) && tmp.get("name").toString().contains(nameField.getText())) {
                    filme.add(tmp.get("filmId").toString() + " " + tmp.get("name").toString() + ", {" + tmp.get("kategorie").toString() + "}, " + tmp.get("filmlaenge").toString() + " Minuten, " + tmp.get("erscheinungsdatum").toString() + ", " + tmp.get("regisseur").toString() + ", " + tmp.get("cast").toString());
                }
            }
        }

        //kategorie cast name filter
        if (kategorieField.getText() != "" && erscheinungsdatumField.getText() == "" && castField.getText() != "" && nameField.getText() != "") {
            for (int i = 0; i < filmearray.length(); i++) {
                JSONObject tmp = filmearray.getJSONObject(i);

                if (tmp.get("kategorie").toString().contains(kategorieField.getText()) && tmp.get("cast").toString().contains(castField.getText()) && tmp.get("name").toString().contains(nameField.getText())) {
                    filme.add(tmp.get("filmId").toString() + " " + tmp.get("name").toString() + ", {" + tmp.get("kategorie").toString() + "}, " + tmp.get("filmlaenge").toString() + " Minuten, " + tmp.get("erscheinungsdatum").toString() + ", " + tmp.get("regisseur").toString() + ", " + tmp.get("cast").toString());
                }
            }
        }

        //kategorie datum name filter
        if (kategorieField.getText() != "" && erscheinungsdatumField.getText() != "" && castField.getText() == "" && nameField.getText() != "") {
            for (int i = 0; i < filmearray.length(); i++) {
                JSONObject tmp = filmearray.getJSONObject(i);

                if (tmp.get("kategorie").toString().contains(kategorieField.getText()) && tmp.get("erscheinungsdatum").toString().contains(erscheinungsdatumField.getText()) && tmp.get("name").toString().contains(nameField.getText())) {
                    filme.add(tmp.get("filmId").toString() + " " + tmp.get("name").toString() + ", {" + tmp.get("kategorie").toString() + "}, " + tmp.get("filmlaenge").toString() + " Minuten, " + tmp.get("erscheinungsdatum").toString() + ", " + tmp.get("regisseur").toString() + ", " + tmp.get("cast").toString());
                }
            }
        }

        //kategorie datum cast name filter
        if (kategorieField.getText() != "" && erscheinungsdatumField.getText() != "" && castField.getText() != "" && nameField.getText() != "") {
            for (int i = 0; i < filmearray.length(); i++) {
                JSONObject tmp = filmearray.getJSONObject(i);

                if (tmp.get("kategorie").toString().contains(kategorieField.getText()) && tmp.get("erscheinungsdatum").toString().contains(erscheinungsdatumField.getText()) && tmp.get("cast").toString().contains(castField.getText())) {
                    filme.add(tmp.get("filmId").toString() + " " + tmp.get("name").toString() + ", {" + tmp.get("kategorie").toString() + "}, " + tmp.get("filmlaenge").toString() + " Minuten, " + tmp.get("erscheinungsdatum").toString() + ", " + tmp.get("regisseur").toString() + ", " + tmp.get("cast").toString());
                }
            }
        }

        filmliste.getItems().addAll(filme);


    }


    //Von https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
    public boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public void filmGesehenPressed() throws IOException, InterruptedException {

        JSONArray alleFilme = csh.sendGETRequest("/controller/film");
        if (filmliste.getSelectionModel().getSelectedItem() == null) {
            popUp.setAlertType(Alert.AlertType.ERROR);
            popUp.setHeaderText("Fehler!");
            popUp.setContentText("Bitte Film in Liste auswählen");
            popUp.showAndWait();
        } else {
            if (!filmliste.getSelectionModel().getSelectedItem().equals("Keine User gefunden")) {
                String IDdesFilms = cropID(filmliste.getSelectionModel().getSelectedItem().toString());
                System.out.println(IDdesFilms + "erster checkpoint film gesehen");
                if (IDdesFilms.equals("Es")) {
                    popUp.setAlertType(Alert.AlertType.ERROR);
                    popUp.setHeaderText("Fehler!");
                    popUp.setContentText("Zu dem Film gibs keine Bewertungen");
                    popUp.showAndWait();
                } else {
                    for (int i = 0; i < alleFilme.length(); i++) {
                        JSONObject tmp = alleFilme.getJSONObject(i);
                        if (tmp.get("filmId").toString().equals(IDdesFilms)) {
                            filmAuswahl = tmp;
                            //System.out.println(user.toString());
                        }
                    }

                    if (isNumeric(filmAuswahl.get("filmId").toString()) == false) {
                        popUp.setAlertType(Alert.AlertType.WARNING);
                        popUp.setHeaderText("Fehler");
                        popUp.setContentText("Film ID muss eine Zahl sein!");
                        popUp.showAndWait();
                        return;
                    }

                    JSONArray eachMovie = csh.sendGETRequest("/controller/film");

                    if (Integer.parseInt(filmAuswahl.get("filmId").toString()) > eachMovie.length()) {
                        popUp.setAlertType(Alert.AlertType.WARNING);
                        popUp.setHeaderText("Fehler");
                        popUp.setContentText("FilmID ist zu groß");
                        popUp.showAndWait();
                        return;
                    }

                    if (Integer.parseInt(filmAuswahl.get("filmId").toString()) < 0) {
                        popUp.setAlertType(Alert.AlertType.WARNING);
                        popUp.setHeaderText("Fehler");
                        popUp.setContentText("FilmID ist zu klein. Mindestens 0");
                        popUp.showAndWait();
                        return;
                    }
                    //füllt den JSON Array mit allen Nutzern
                    alleNutzer = csh.sendGETRequest("/controller/nutzer");

                    //Überprüft den angemeldeten Nutzer
                    for (int i = 0; i < alleNutzer.length(); i++) {
                        JSONObject temp = alleNutzer.getJSONObject(i);
                        if (temp.get("email").toString().equals(AngemeldeterUser.getSpeicher().getEmail())) {
                            this.angemeldeterUser = temp;
                        }
                    }

                    //sucht die Entität hinter dem filmIDTextField
                    JSONArray allefilme = csh.sendGETRequest("/controller/film");
                    JSONObject gesucht = new JSONObject();
                    JSONObject filmPointer = new JSONObject();

                    for (int i = 0; i < allefilme.length(); i++) {
                        filmPointer = allefilme.getJSONObject(i);
                        if (filmPointer.get("filmId").toString().equals(filmAuswahl.get("filmId").toString())) {
                            gesucht = filmPointer;
                        }
                    }

                    JSONObject film = new JSONObject();
                    film.put("emailNutzer", angemeldeterUser.get("email"));
                    film.put("filmId", gesucht.get("filmId"));
                    film.put("date", LocalDate.now());

                    //abfrage ob film bereits gesehen wurde
                    JSONArray alleGesehenen = csh.sendGETRequest("/controller/geseheneFilme");
                    for (int i = 0; i < alleGesehenen.length(); i++) {
                        JSONObject speicher = alleGesehenen.getJSONObject(i);
                        if (speicher.get("emailNutzer").toString().equals(film.get("emailNutzer").toString()) && ((JSONObject) speicher.get("filmId")).get("filmId").toString().equals(film.get("filmId").toString())) {
                            popUp.setAlertType(Alert.AlertType.INFORMATION);
                            popUp.setHeaderText("Information");
                            popUp.setContentText("Film wurde bereits als gesehen Markiert");
                            popUp.showAndWait();
                            return;
                        }
                    }

                    // Fügt den aktuell ausgewählten Film zu "controller/geseheneFilme" hinzu
                    film.put("filmId", gesucht);
                    csh.sendPostRequest("/controller/geseheneFilme/add", film);
                    popUp.setAlertType(Alert.AlertType.INFORMATION);
                    popUp.setHeaderText("Bestätigung");
                    popUp.setContentText("Der Film wurde als gesehen markiert!");
                    popUp.showAndWait();

                    //remove from watchlist  profile/watchlists fehlt
                    film.put("filmId", gesucht.get("filmId"));
                    JSONArray kompletteWatchlist = csh.sendGETRequest("/controller/watchlist");
                    for (int i = 0; i < kompletteWatchlist.length(); i++) {
                        JSONObject speicher2 = kompletteWatchlist.getJSONObject(i);

                        //if abfrage mit "emailNutzer" und "filmId" um die watchlistID herauszufinden
                        if (speicher2.get("emailNutzer").toString().equals(film.get("emailNutzer").toString()) && speicher2.get("filmId").toString().equals(film.get("filmId").toString())) {
                            film.put("watchlistId", speicher2.get("watchlistId"));
                            csh.sendPostRequest("/controller/watchlist/delete", film);
                        }
                    }
                }
            }
        }
    }

    public void startseitePressed() throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startseiteButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void bewertungenPressed() throws IOException {

        if (filmliste.getSelectionModel().getSelectedItem() == null) {
            popUp.setAlertType(Alert.AlertType.ERROR);
            popUp.setHeaderText("Fehler!");
            popUp.setContentText("Bitte Film in Liste auswählen");
            popUp.showAndWait();
        }else{

            Zwischenspeicher.getSpeicher().setFilmId(Integer.parseInt(cropID(filmliste.getSelectionModel().getSelectedItem().toString())));

            FXMLLoader laden = new FXMLLoader(getClass().getResource("/bewertungenSeite2.fxml"));
            Stage stage = (Stage) bewertungenButton.getScene().getWindow();
            Scene scene = new Scene(laden.load());
            stage.setScene(scene);
        }

    }

    public void filmBewertenPressed(ActionEvent actionEvent) throws IOException {

        JSONArray alleFilme = csh.sendGETRequest("/controller/film");
        if (filmliste.getSelectionModel().getSelectedItem() == null) {
            popUp.setAlertType(Alert.AlertType.ERROR);
            popUp.setHeaderText("Fehler!");
            popUp.setContentText("Bitte Film in Liste auswählen");
            popUp.showAndWait();
        } else {

            if (!filmliste.getSelectionModel().getSelectedItem().equals("Keine User gefunden")) {

                String IDdesFilms = cropID(filmliste.getSelectionModel().getSelectedItem().toString());
                if (IDdesFilms.equals("Es")) {
                    popUp.setAlertType(Alert.AlertType.ERROR);
                    popUp.setHeaderText("Fehler!");
                    popUp.setContentText("Zu dem Film gibs keine Bewertungen");
                    popUp.showAndWait();

                } else {
                    for (int i = 0; i < alleFilme.length(); i++) {
                        JSONObject tmp = alleFilme.getJSONObject(i);
                        if (tmp.get("filmId").toString().equals(IDdesFilms)) {
                            filmAuswahl = tmp;
                            //System.out.println(user.toString());
                        }
                    }

                }
            }

            Zwischenspeicher.getSpeicher().setFilmId(Integer.parseInt(filmAuswahl.get("filmId").toString()));
            int speicherID = Zwischenspeicher.getSpeicher().getFilmId();

            String result = csh.sendGETRequestGetString("/controller/geseheneFilme/checkDuplicate/" + speicherID + "/" + AngemeldeterUser.getSpeicher().getEmail());

            if (result.equals("false")) {
                popUp.setAlertType(Alert.AlertType.WARNING);
                popUp.setHeaderText("Fehler");
                popUp.setContentText("Der Film wurde noch nicht geschaut!");
                popUp.showAndWait();
                return;
            }

            FXMLLoader laden = new FXMLLoader(getClass().getResource("/filmNutzerVerwaltung.fxml"));
            Stage stage = (Stage) filmBewertenButton.getScene().getWindow();
            Scene scene = new Scene(laden.load());
            stage.setScene(scene);
        }
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

    public void addToWatchlist(ActionEvent actionEvent) throws IOException, InterruptedException {
        JSONArray allebewertungen = new JSONArray();
        allebewertungen = CShandler.sendGETRequest("/controller/watchlist");


        if (filmliste.getSelectionModel().getSelectedItem() == null) {
            popUp.setAlertType(Alert.AlertType.ERROR);
            popUp.setHeaderText("Fehler!");
            popUp.setContentText("Bitte Film in Liste auswählen");
            popUp.showAndWait();
            return;
        }

        String IDdesFilms = cropID(filmliste.getSelectionModel().getSelectedItem().toString());

        JSONObject watchlisteintrag = new JSONObject();
        watchlisteintrag.put("emailNutzer", AngemeldeterUser.getSpeicher().getEmail());
        watchlisteintrag.put("filmId", IDdesFilms);

        String response = CShandler.sendGETRequestGetString("/controller/geseheneFilme/checkDuplicate/"+IDdesFilms+"/"+AngemeldeterUser.getSpeicher().getEmail());

        if(response.equals("true")){
            popUp.setAlertType(Alert.AlertType.WARNING);
            popUp.setHeaderText("Fehler");
            popUp.setContentText("Dieser Film wurde bereits als gesehen markiert");
            popUp.showAndWait();
            return;
        }

        for (int i = 0; i < allebewertungen.length(); i++) {
            JSONObject tmp = allebewertungen.getJSONObject(i);
            if (tmp.get("emailNutzer").toString().equals(watchlisteintrag.get("emailNutzer").toString()) && tmp.get("filmId").toString().equals(watchlisteintrag.get("filmId").toString())) {
                popUp.setAlertType(Alert.AlertType.WARNING);
                popUp.setHeaderText("Fehler");
                popUp.setContentText("Dieser Film befindet sich bereits auf ihrer Watchlist");
                popUp.showAndWait();
                return;
            }
        }

        CShandler.sendPostRequest("/controller/watchlist/add", watchlisteintrag);
        popUp.setAlertType(Alert.AlertType.WARNING);
        popUp.setHeaderText("Erfolg");
        popUp.setContentText("Film erfolgreich zur Watchlist hinzugefügt");
        popUp.showAndWait();
    }

}