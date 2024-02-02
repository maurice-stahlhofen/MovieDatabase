package client.client.Statistik;

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
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.jar.Attributes;

public class SelectFilmStatistikController implements Initializable {
    public TextField filmIDTxt;

    public ImageView filmIdAddButton;

    public Text wrongFilmID;

    public TextField resultText;
    public Button backButton;

    public List<Integer> numbers = new LinkedList<>();
    public Button saveButton;
    public ListView filmView;
    public Button allFilmButton;
    public TextField filmIDTxtRemove;
    public ImageView filmIdRemoveButton;
    public Text wrongFilmIDRemove;
    public ImageView allButton;

    private List<Integer> allFilmIDs = new LinkedList<>();

    public SelectFilmStatistikController() throws MalformedURLException {

    }

    ClientServerHandler handler = new ClientServerHandler();

    ObservableList list = FXCollections.observableArrayList();


    public void setResultData(ActionEvent actionEvent) {
    }


    // Ohne Speichern der Auswahl zurück zur Statistik
    public void backToStatistik(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Statistik_Admin/StatistikADMIN.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            loadData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Tooltip.install(allButton, new Tooltip("Alle Filme hinzufügen"));
        Tooltip.install(filmIdAddButton, new Tooltip("Film hinzufügen"));
        Tooltip.install(filmIdRemoveButton, new Tooltip("Film entfernen"));


    }

    // Man kann nicht mehrere gleiche Filme zur Auswahl hinzufügen
    public boolean checkDuplicate(int number) {
        if(numbers==null){
            return true;
        }else {
            if (numbers.contains(number)) {
                return false;
            } else {
                return true;
            }
        }
    }

    public void saveAndSwitchToTable(ActionEvent actionEvent) throws IOException {
        executeSaveAndSwitch();

    }

    // Filmauswahl wird gespeichert und man kommt zur Statistiktabelle
    public void executeSaveAndSwitch() throws IOException {
        StatistikZwischenspeicher.getSpeicher().setList(numbers);

        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Statistik_Admin/StatistikADMIN.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    // Alle Filme aus der Datenbank in View laden
    public void loadData() throws IOException {
        JSONArray filmArray = handler.sendGETRequest("/controller/film");


        for (int i = 0; i < filmArray.length(); i++) {
            JSONObject tmp = filmArray.getJSONObject(i);
            allFilmIDs.add(Integer.parseInt(tmp.get("filmId").toString()));
            list.add("FilmID: " + tmp.get("filmId").toString() + " || Name: " + tmp.get("name").toString() + " || Datum: " + tmp.get("erscheinungsdatum").toString() + " || Kategorie: " + tmp.get("kategorie"));
        }
        if (list.isEmpty()) {
            filmView.getItems().add("Noch keine Filme angelegt");
        }else {
            filmView.getItems().addAll(list);
            numbers = StatistikZwischenspeicher.getSpeicher().getList();
            if(numbers!=null) {
                printResultText();
            }else{

            }
        }
    }

    private boolean checkDigits(String string) {
        for (int i = 0; i <= string.length() - 1; i++) {
            String tmp = "" + string.charAt(i);
            if (!tmp.matches("[0-9]")) {
                return false;
            }
        }
        return true;
    }

    // Anzeige der Filme, die man bis dahin ausgewählt hat
    public void printResultText(){
        resultText.setText("");
        for(int i = 0; i < numbers.size(); i++){
            if (resultText.getText().toString().length() == 0) {
                resultText.setText(String.valueOf(numbers.get(i)));
                filmIDTxt.setText("");
            } else {
                resultText.setText(resultText.getText() + ", " + numbers.get(i));
                filmIDTxt.setText("");
            }
        }
    }

// FilmID zur Auswahl hinzufügen
    public void addFilmID(MouseEvent mouseEvent) {

        if (filmIDTxt.getText().toString().length() == 0 || checkDigits(filmIDTxt.getText().toString()) == false) {
            wrongFilmIDRemove.setText("");
            wrongFilmID.setText("Keine Zahl eingegeben");
        } else if (checkDuplicate(Integer.parseInt(filmIDTxt.getText().toString())) == false) {
            wrongFilmIDRemove.setText("");
            wrongFilmID.setText("Die FilmID wurde bereits hinzugefügt!");

        } else {
            if(list.isEmpty()){
                wrongFilmIDRemove.setText("");
                wrongFilmID.setText("Die Filmliste ist noch leer");
            }else if(!allFilmIDs.contains(Integer.parseInt(filmIDTxt.getText().toString()))) {
                wrongFilmIDRemove.setText("");
                wrongFilmID.setText("Die FilmID existiert nicht!");
            }else{
                if(numbers==null){
                    numbers= new LinkedList<>();
                }
                this.numbers.add(Integer.valueOf(filmIDTxt.getText()));
                wrongFilmID.setText("");
                printResultText();
            }

        }

    }

    // FilmID aus Auswahl entfernen
    public void removeFilmID(MouseEvent mouseEvent) {
        if(filmIDTxtRemove.getText().toString().length()==0 || checkDigits(filmIDTxtRemove.getText().toString())==false){
            wrongFilmIDRemove.setText("Keine Zahl eingegeben");
        }else {
            int eingabeZahl = Integer.parseInt(filmIDTxtRemove.getText().toString());
            if (numbers.contains(eingabeZahl)) {
               int index= numbers.indexOf(Integer.parseInt(filmIDTxtRemove.getText().toString()));
               numbers.remove(index);
                filmIDTxtRemove.setText("");
                printResultText();
            } else {
                wrongFilmID.setText("");
                wrongFilmIDRemove.setText("FilmID nicht in Auswahl!");
            }
        }
    }

    // Alle Filme für Statistik auswählen
    public void selectALLFilms(MouseEvent event) {
        resultText.setText("");
        if(numbers==null){
            numbers= new LinkedList<>();
        }else{
            numbers.clear();
        }
        for(int i = 0; i < allFilmIDs.size(); i++){
            numbers.add(allFilmIDs.get(i));
        }
        printResultText();
    }

    // Durch Doppelklicken in Listview wird der Film zur Auswahl hinzugefügt
    public void getFilm(MouseEvent click) {
        if (click.getClickCount() == 2) {

            int nummer = allFilmIDs.get(filmView.getSelectionModel().getSelectedIndex());

            if(numbers==null){
                numbers= new LinkedList<>();
            }
            this.numbers.add(nummer);
            wrongFilmID.setText("");
            printResultText();

        }
    }
}
