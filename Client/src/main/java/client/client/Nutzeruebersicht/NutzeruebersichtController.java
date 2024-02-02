package client.client.Nutzeruebersicht;

import Controller.ClientServerHandler;
import client.client.login.AngemeldeterUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class NutzeruebersichtController implements Initializable {


    public Text txtVorname;
    public Text txtNachname;
    public Text txtEmail;
    public Button backWindow;
    public ListView<String> freundesliste;
    public Button settingButton;
    public ListView<String> watchlist;
    public ListView<String> geseheneFilme;
    public Text txtPasswort;

    ClientServerHandler handler = new ClientServerHandler();

    ObservableList list = FXCollections.observableArrayList();

    public NutzeruebersichtController() throws MalformedURLException {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        txtVorname.setText(AngemeldeterUser.getSpeicher().getVorname());
        txtNachname.setText(AngemeldeterUser.getSpeicher().getNachname());
        txtEmail.setText(AngemeldeterUser.getSpeicher().getEmail());
        txtPasswort.setText(AngemeldeterUser.getSpeicher().getPasswort());
        try {
            loadData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void backToMainpage(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stg = (Stage) backWindow.getScene().getWindow();
        Scene sc = new Scene(loader.load());
        stg.setScene(sc);
    }

    private void loadData() throws IOException {

        //Freundesliste

        list.removeAll(list);
        JSONArray freundeslisteArray = handler.sendGETRequest("/controller/freundesliste");
        JSONArray nutzerArray = handler.sendGETRequest("/controller/nutzer");

        for (int i = 0; i < freundeslisteArray.length(); i++) {
            JSONObject tmp = freundeslisteArray.getJSONObject(i);


            if (tmp.get("emailNutzer").toString().equals(AngemeldeterUser.getSpeicher().getEmail())) {
                for(int k = 0; k < nutzerArray.length(); k++){
                    JSONObject tmpNutzer = nutzerArray.getJSONObject(k);

                    if(tmpNutzer.get("email").toString().equals(tmp.get("emailFreund").toString())){
                        list.add(tmpNutzer.get("vorname").toString()+" "+tmpNutzer.get("nachname").toString());
                    }
                }


            }

        }
        if(list.isEmpty()){
            freundesliste.getItems().add("Deine Freundesliste ist leer");
        }
        freundesliste.getItems().addAll(list);


        //Watchlist

        list.removeAll(list);
        JSONArray watchlistArray = handler.sendGETRequest("/controller/watchlist");
        JSONArray filmArray = handler.sendGETRequest("/controller/film");

        for (int i = 0; i < watchlistArray.length(); i++) {
            JSONObject tmp = watchlistArray.getJSONObject(i);

            if (tmp.get("emailNutzer").toString().equals(AngemeldeterUser.getSpeicher().getEmail())) {
                for (int k = 0; k < filmArray.length(); k++) {
                    JSONObject tmpFilm = filmArray.getJSONObject(k);
                    if (tmpFilm.get("filmId").toString().equals(tmp.get("filmId").toString())) {
                        list.add("Name: " + tmpFilm.get("name").toString() + ", Datum: " + tmpFilm.get("erscheinungsdatum").toString() + ", Kategorie: " + tmpFilm.get("kategorie").toString());
                    }
                }

            }
        }
        if(list.isEmpty()){
            watchlist.getItems().add("Deine Watchlist ist leer");
        }
        watchlist.getItems().addAll(list);



        //Gesehene Filme
        list.removeAll(list);
        // Gibt alle Filme unter der Email aus:
        JSONArray userGeseheneFilmeListe = handler.sendGETRequest("/controller/geseheneFilme/email/"+AngemeldeterUser.getSpeicher().getEmail());


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
                geseheneFilme.getItems().add("Du hast keine Filme als gesehen markiert");
            }else {
                geseheneFilme.getItems().addAll(list);
            }


    }


    public void listResult(MouseEvent mouseEvent) {
    }

    public void setVisibleSettings(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Sichtbarkeitseinstellung.fxml"));
        Stage stg = (Stage) settingButton.getScene().getWindow();
        Scene sc = new Scene(loader.load());
        stg.setScene(sc);
    }
}
