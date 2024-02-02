package client.client.Soziales;

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
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class AndersProfilController implements Initializable {
    @FXML
    public Text titel, txtVorname, txtNachname, txtEmail;

    public ImageView profil_View;
    public Button filmSucheButton;
    private Image profil_bild;
    private BufferedImage profil_buff;
    private byte[] pbbyte;

    public ListView<String> geseheneFilme, watchlist, freundesliste;

    @FXML
    Button startseiteButton, backButton, addenButton;
    ClientServerHandler handler = new ClientServerHandler();

    JSONArray alleFilme = new JSONArray();
    JSONArray watchliste = new JSONArray();
    JSONArray alleUser = new JSONArray();
    JSONArray Freunde = new JSONArray();
    JSONArray geseheneFilmeListArray = new JSONArray();

    String sichtbarkeit = FremderUser.getSpeicher().getSichtbarkeit();

    ObservableList list = FXCollections.observableArrayList();

    public AndersProfilController() throws MalformedURLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titel.setText(FremderUser.getSpeicher().getVorname());
        txtVorname.setText(FremderUser.getSpeicher().getVorname());
        txtNachname.setText(FremderUser.getSpeicher().getNachname());
        txtEmail.setText(FremderUser.getSpeicher().getEmail());

        //System.out.println(sichtbarkeit);

        switch (sichtbarkeit) {
            case "ALLE":
                try {
                    initALLE();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "FREUNDE":
                try {
                    initFREUNDE();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "ICH":
                try {
                    initICH();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
        }

    }

    private void initALLE() throws IOException {
        //Freundesliste
        list.removeAll(list);
        Freunde = handler.sendGETRequest("/controller/freundesliste");//Arrays füllen
        alleUser = handler.sendGETRequest("/controller/nutzer");

        for (int i = 0; i < Freunde.length(); i++) {
            JSONObject tmp = Freunde.getJSONObject(i);

            if (tmp.get("emailNutzer").toString().equals(FremderUser.getSpeicher().getEmail())) {
                for (int k = 0; k < alleUser.length(); k++) {
                    JSONObject tmpNutzer = alleUser.getJSONObject(k);
                    //checken ob die Freunde sind
                    if (tmpNutzer.get("email").toString().equals(tmp.get("emailFreund").toString())) {
                        list.add(tmpNutzer.get("vorname").toString() + " " + tmpNutzer.get("nachname").toString());
                    }
                }
            }
        }
        if (list.isEmpty()) {
            freundesliste.getItems().add(FremderUser.getSpeicher().getVorname() + "'s Freundesliste ist leer");
        }
        freundesliste.getItems().addAll(list);

        //Watchlist
        list.removeAll(list);
        watchliste = handler.sendGETRequest("/controller/watchlist");
        alleFilme = handler.sendGETRequest("/controller/film");

        for (int i = 0; i < watchliste.length(); i++) {
            JSONObject tmp = watchliste.getJSONObject(i);

            if (tmp.get("emailNutzer").toString().equals(FremderUser.getSpeicher().getEmail())) {
                for (int k = 0; k < alleFilme.length(); k++) {
                    JSONObject tmpFilm = alleFilme.getJSONObject(k);
                    if (tmpFilm.get("filmId").toString().equals(tmp.get("filmId").toString())) {
                        list.add("Name: " + tmpFilm.get("name").toString() + ", Datum: " + tmpFilm.get("erscheinungsdatum").toString() + ", Kategorie: " + tmpFilm.get("kategorie").toString());
                    }
                }
            }
        }
        if (list.isEmpty()) {
            watchlist.getItems().add(FremderUser.getSpeicher().getVorname() + "'s Watchlist ist leer");
        }
        watchlist.getItems().addAll(list);

        //Gesehene Filme
        list.removeAll(list);
        // Gibt alle Filme unter der Email aus:
        JSONArray userGeseheneFilmeListe = handler.sendGETRequest("/controller/geseheneFilme/email/"+FremderUser.getSpeicher().getEmail());


        if(userGeseheneFilmeListe.length()==0){

        }else {
            //Im Array ist mind. ein gesehener Film
            for (int j = 0; j < userGeseheneFilmeListe.length(); j++) {
                JSONObject temp = userGeseheneFilmeListe.getJSONObject(j);


                JSONObject film = temp.getJSONObject("filmId");

                list.add("Name: " + film.get("name").toString() + " , Datum: " + film.get("erscheinungsdatum").toString() + " , Kategorie: " + film.get("kategorie").toString());


            }
        }
        if(list.isEmpty()){
            geseheneFilme.getItems().add(FremderUser.getSpeicher().getVorname() +" hat keine Filme als gesehen markiert");
        }else {
            geseheneFilme.getItems().addAll(list);
        }
    }

    private void initFREUNDE() throws IOException {
        if(sindFreunde() == true){
            //Freundesliste
            list.removeAll(list);
            Freunde = handler.sendGETRequest("/controller/freundesliste");//Arrays füllen
            alleUser = handler.sendGETRequest("/controller/nutzer");

            for (int i = 0; i < Freunde.length(); i++) {
                JSONObject tmp = Freunde.getJSONObject(i);

                if (tmp.get("emailNutzer").toString().equals(FremderUser.getSpeicher().getEmail())) {
                    for (int k = 0; k < alleUser.length(); k++) {
                        JSONObject tmpNutzer = alleUser.getJSONObject(k);
                        //checken ob die Freunde sind
                        if (tmpNutzer.get("email").toString().equals(tmp.get("emailFreund").toString())) {
                            list.add(tmpNutzer.get("vorname").toString() + " " + tmpNutzer.get("nachname").toString());
                        }
                    }
                }
            }
            if (list.isEmpty()) {
                freundesliste.getItems().add(FremderUser.getSpeicher().getVorname() + "'s Freundesliste ist leer");
            }
            freundesliste.getItems().addAll(list);

            //Watchlist
            list.removeAll(list);
            watchliste = handler.sendGETRequest("/controller/watchlist");
            alleFilme = handler.sendGETRequest("/controller/film");

            for (int i = 0; i < watchliste.length(); i++) {
                JSONObject tmp = watchliste.getJSONObject(i);

                if (tmp.get("emailNutzer").toString().equals(FremderUser.getSpeicher().getEmail())) {
                    for (int k = 0; k < alleFilme.length(); k++) {
                        JSONObject tmpFilm = alleFilme.getJSONObject(k);
                        if (tmpFilm.get("filmId").toString().equals(tmp.get("filmId").toString())) {
                            list.add("Name: " + tmpFilm.get("name").toString() + ", Datum: " + tmpFilm.get("erscheinungsdatum").toString() + ", Kategorie: " + tmpFilm.get("kategorie").toString());
                        }
                    }
                }
            }
            if (list.isEmpty()) {
                watchlist.getItems().add(FremderUser.getSpeicher().getVorname() + "'s Watchlist ist leer");
            }
            watchlist.getItems().addAll(list);

            //Gesehene Filme
            list.removeAll(list);
            // Gibt alle Filme unter der Email aus:
            JSONArray userGeseheneFilmeListe = handler.sendGETRequest("/controller/geseheneFilme/email/"+FremderUser.getSpeicher().getEmail());


            if(userGeseheneFilmeListe.length()==0){

            }else {
                //Im Array ist mind. ein gesehener Film
                for (int j = 0; j < userGeseheneFilmeListe.length(); j++) {
                    JSONObject temp = userGeseheneFilmeListe.getJSONObject(j);


                    JSONObject film = temp.getJSONObject("filmId");

                    list.add("Name: " + film.get("name").toString() + " , Datum: " + film.get("erscheinungsdatum").toString() + " , Kategorie: " + film.get("kategorie").toString());


                }
            }
            if(list.isEmpty()){
                geseheneFilme.getItems().add(FremderUser.getSpeicher().getVorname() +" hat keine Filme als gesehen markiert");
            }else {
                geseheneFilme.getItems().addAll(list);
            }
        }
        else{
            geseheneFilme.getItems().add("Nicht einsehbar");
            watchlist.getItems().add("Nicht einsehbar");
            freundesliste.getItems().add("Nicht einsehbar");
        }
    }

    private void initICH() throws IOException {
        geseheneFilme.getItems().add("Nicht einsehbar");
        watchlist.getItems().add("Nicht einsehbar");
        freundesliste.getItems().add("Nicht einsehbar");
    }

    private boolean sindFreunde() throws IOException {
        Freunde = handler.sendGETRequest("/controller/freundesliste");
        for(int i = 0; i < Freunde.length();i++){
            JSONObject tmp = Freunde.getJSONObject(i);

            if(tmp.get("emailNutzer").equals(AngemeldeterUser.getSpeicher().getEmail()) &&
            tmp.get("emailFreund").equals(FremderUser.getSpeicher().getEmail()) ||
                    tmp.get("emailNutzer").equals(FremderUser.getSpeicher().getEmail()) &&
                            tmp.get("emailFreund").equals(AngemeldeterUser.getSpeicher().getEmail()) ){
                return true;
            }
        }
        return false;
    }
    public void backPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Nutzersuche.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void startseitePressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startseiteButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void addenPressed(ActionEvent actionEvent) throws IOException, InterruptedException {
//        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Freunde_EinladungenMenueView.fxml"));
//        Stage stage = (Stage) addenButton.getScene().getWindow();
//        Scene scene = new Scene(laden.load());
//        stage.setScene(scene);

        String wanteduseremail = FremderUser.getSpeicher().getEmail();
        String clientemail= AngemeldeterUser.getSpeicher().getEmail();

        //überprüft bereits existente Freundschaften
        JSONArray freundschaften=handler.sendGETRequest("/controller/freundesliste");

        if(wanteduseremail.equals(clientemail)){
            showAlert(Alert.AlertType.WARNING, "Fehler!", "Du kannst dir selbst keine Freundeseinladung schicken." );
            return;
        }

        if(freundschaften.length()>0){
            for(int i=0; i<freundschaften.length(); i++){
                JSONObject freunde=freundschaften.getJSONObject(i);
                String nutzerEmail=freunde.get("emailNutzer").toString();
                String freundEmail=freunde.get("emailFreund").toString();

                //einseitig die emails abchecken, da eine Freundschaft gegenseitig (2mal) besteht
                //Überprüft, ob eine Freundschaft bereits existiert
                if((clientemail.equals(nutzerEmail) && wanteduseremail.equals(freundEmail))){
                    showAlert(Alert.AlertType.WARNING, "Fehler!","Ihr seid bereits Freunde!" );
                    return;
                }
            }
        }

        //lädt alle existenten Einladungen
        JSONArray einladungen=handler.sendGETRequest("/controller/freundeseinladungen");
        if(einladungen.length()>0){
            boolean einladungexists=false;
            for(int i=0; i<einladungen.length(); i++){
                JSONObject einladung=einladungen.getJSONObject(i);
                String emailClient=einladung.get("emailNutzer").toString();
                String emailFreund=einladung.get("emailFreund").toString();

                //Überprüft, ob es eine Einladung schon gibt
                //1.Fall: Client schickt erneut eine Einladung
                if (emailClient.equals(clientemail) && emailFreund.equals(wanteduseremail)) {

                    einladungexists=true;
                    showAlert(Alert.AlertType.WARNING,"Ups!", "Du hast diesem Nutzer bereits eine Freundeseinladung geschickt.");
                    //2.Fall: Client hat bereits eine Einladung von dem ausgewählten Nutzer erhalten
                }else if((emailClient.equals(wanteduseremail) && emailFreund.equals(clientemail))){

                    einladungexists=true;
                    showAlert(Alert.AlertType.WARNING, "Ups!", "Dieser Nutzer hat dir bereits eine Einladung geschickt!");
                }
            }
            //Es existiert keine Einladung
            if(!einladungexists){
                JSONObject neweinladung = new JSONObject();
                neweinladung.put("emailNutzer", clientemail);
                neweinladung.put("emailFreund", wanteduseremail);
                handler.sendPostRequest("/controller/freundeseinladungen/add", neweinladung);

                showAlert(Alert.AlertType.INFORMATION, "Erfolg!","Die Einladung wurde verschickt!");
            }
            //es existieren noch überhaupt keine Einladungen
        }else{
            JSONObject neweinladung = new JSONObject();
            neweinladung.put("emailNutzer", clientemail);
            neweinladung.put("emailFreund", wanteduseremail);
            handler.sendPostRequest("/controller/freundeseinladungen/add", neweinladung);

            showAlert(Alert.AlertType.INFORMATION,"Erfolg!","Die Einladung wurde verschickt!" );
        }

    }

    public void listResult(MouseEvent mouseEvent) {
    }

    //um die Methode addenPressed() zu entlasten
    public void showAlert(Alert.AlertType alertType, String header, String context){
        Alert alert = new Alert(alertType);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }

    public void toFilmSuche(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/anzeigenlistview.fxml"));
        Stage stage = (Stage) filmSucheButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }
}

