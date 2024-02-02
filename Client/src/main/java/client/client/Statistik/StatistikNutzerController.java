package client.client.Statistik;

import Controller.ClientServerHandler;
import client.client.login.AngemeldeterUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class StatistikNutzerController implements Initializable {

    public DatePicker startDate, endDate;
    public Label zeitraumLabel, watchTime, anzahlDerFilme;
    public TitledPane zeitraumWahl;
    public Button startseiteButton;
    public ListView<String> lieblingsSchauspielerList, lieblingsKategorieList, lieblingsFilmList;
    public ImageView clearDate, reloadButton;
    public CheckBox erweiterteStatistikBox;

    ClientServerHandler handler = new ClientServerHandler();

    public StatistikNutzerController() throws MalformedURLException {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Tooltip.install(reloadButton, new Tooltip("Statistik laden"));
        Tooltip.install(clearDate, new Tooltip("Zeitraumauswahl zurücksetzen"));
    }


    public void startseitePressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startseiteButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }


    public void reloadStatistics(MouseEvent mouseEvent) throws IOException {
        // Überprüfen des eingegebenen Zeitraums

        if (startDate.getValue() == null && endDate.getValue() == null){
            // beide Daten leer: Zeitraum bis heute
            String endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            zeitraumLabel.setText("bis: " + endDate);
            zeitraumWahl.setExpanded(false);
            this.generateStatistics(null, LocalDate.now());
        }

        else if (startDate.getValue() == null && endDate.getValue() != null) {
            // Nur Enddatum vorhanden: Zeitraum bis zum Enddatum
            if (endDate.getValue().isAfter(LocalDate.now())) {
                // Enddatum darf nicht nach dem heutigen Datum liegen
                System.out.println("Das Enddatum kann nicht in der Zukunft liegen");
                this.showAlert("Das Enddatum kann nicht in der Zukunft liegen");
                return;
            }
            String endDate = this.endDate.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            zeitraumLabel.setText("bis: " + endDate);
            zeitraumWahl.setExpanded(false);
            this.generateStatistics(null, this.endDate.getValue());
        }
        else if (startDate.getValue() != null && endDate.getValue() == null) {
            // Nur Startdatum vorhanden: Zeitraum vom Startdatum bis Heute
            if (startDate.getValue().isAfter(LocalDate.now())) {
                // Startdatum darf nicht nach dem heutigen Datum liegen
                System.out.println("Das Startdatum kann nicht in der Zukunft liegen");
                this.showAlert("Das Startdatum kann nicht in der Zukunft liegen");
                return;
            }
            String startDate = this.startDate.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            String endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            zeitraumLabel.setText(startDate + " - " + endDate);
            zeitraumWahl.setExpanded(false);
            this.generateStatistics(this.startDate.getValue(), LocalDate.now());
        }

        else if(startDate.getValue() != null && endDate.getValue() != null){
            // Start- und Enddatum vorhanden: Zeitraum vom Start- bis zum Enddatum
            if (startDate.getValue().isAfter(endDate.getValue())) {
                // Startdatum muss vor dem Enddatum Liegen
                System.out.println("Das Startdatum kann nicht hinter dem Enddatum liegen");
                this.showAlert("Das Startdatum kann nicht hinter dem Enddatum liegen");
                return;
            }
            if (startDate.getValue().isAfter(LocalDate.now()) || endDate.getValue().isAfter(LocalDate.now())) {
                // Start- und Enddatum dürfen nicht nach dem heutigen Datum liegen
                System.out.println("Der Zeitraum kann nicht in der Zukunft liegen");
                this.showAlert("Der Zeitraum kann nicht in der Zukunft liegen");
                return;
            } else{
                String startDate = this.startDate.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                String endDate = this.endDate.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                zeitraumLabel.setText(startDate + " - " + endDate);
                zeitraumWahl.setExpanded(false);
                this.generateStatistics(this.startDate.getValue(), this.endDate.getValue());
            }
        }

    }

    public void clearDatePressed(MouseEvent mouseEvent) {
        startDate.setValue(null);
        endDate.setValue(null);
        zeitraumLabel.setText("");
    }

    public void showAlert (String message){
        Alert report = new Alert(Alert.AlertType.ERROR, (message), ButtonType.OK);
        report.setTitle("Datum Fehler");
        report.setHeaderText("Das Datum ist nicht gültig");
        report.showAndWait();
    }


    public void generateStatistics(LocalDate startDate, LocalDate endDate) throws IOException {
        System.out.println(startDate + " - " + endDate);

        JSONArray filmeImZeitraum = new JSONArray();
        filmeImZeitraum.clear();

        // Holt alle gesehenen Filme des Nutzers
        JSONArray geseheneFilmeList = handler.sendGETRequest("/controller/geseheneFilme/email/"+ AngemeldeterUser.getSpeicher().getEmail());

        // Sortiert die Filme nach dem gewählten Zeitrum
        for (int i = 0; i < geseheneFilmeList.length(); i++) {
            JSONObject gesehenerFilm = geseheneFilmeList.getJSONObject(i);

            if(LocalDate.parse(gesehenerFilm.get("date").toString()).isBefore(endDate) ||
                    LocalDate.parse(gesehenerFilm.get("date").toString()).isEqual(endDate)){
                if(startDate == null){
                    filmeImZeitraum.put(gesehenerFilm.getJSONObject("filmId"));
                }
                else if(LocalDate.parse(gesehenerFilm.get("date").toString()).isAfter(startDate) ||
                        LocalDate.parse(gesehenerFilm.get("date").toString()).isEqual(startDate)){
                    filmeImZeitraum.put(gesehenerFilm.getJSONObject("filmId"));
                }
            }

        }
        System.out.println("Filme im Zeitraum: " + filmeImZeitraum.length());


        // Erstellt die Statistik

        // Listen zum Füllen der Listen zum Anzeigen im View
        ObservableList listSchauspieler = FXCollections.observableArrayList();
        ObservableList listKategorie = FXCollections.observableArrayList();
        ObservableList listFilme = FXCollections.observableArrayList();
        int timeWatched;

        listSchauspieler.removeAll(listSchauspieler);
        listKategorie.removeAll(listKategorie);
        listFilme.removeAll(listFilme);
        timeWatched = 0;

        lieblingsSchauspielerList.getItems().clear();
        lieblingsKategorieList.getItems().clear();
        lieblingsFilmList.getItems().clear();


        // Berechnen der Statistiken

        if(filmeImZeitraum.length()==0) {
            // Keine Filme im Zeitraum vorhanden
            System.out.println("Keine Filme im Zeitraum vorhanden");
            lieblingsKategorieList.getItems().add("keine Kategorie");
            lieblingsSchauspielerList.getItems().add("Kein Cast");
            lieblingsFilmList.getItems().add("Kein Film");
            watchTime.setText("0 min");
        }

        else {
            //Mindestens ein Film im Zeitraum gefunden
            List<StatistikNode> statistikSchauspielerList = new LinkedList<>();
            List<StatistikNode> statistikKategorieList = new LinkedList<>();
            List<StatistikNode> statistikFilmList = new LinkedList<>();

            // Daten der Filme Sammeln

            for (int i = 0; i < filmeImZeitraum.length(); i++) {
                JSONObject film = filmeImZeitraum.getJSONObject(i);

                // Statistik-Daten der Schauspieler:
                String cast = film.get("cast").toString();
                    //System.out.println(i + ". cast: " + cast);
                if(!cast.matches("nicht gefunden")) {
                    cast = cast.replace("[", "");
                    cast = cast.replace("]", "");

                    String[] castList = cast.split(", ");
                    //System.out.println(castList.length);

                    for (int c = 0; c < castList.length; c++) {
                        boolean done = false;
                        for (StatistikNode node : statistikSchauspielerList) {
                            if (node.getName().matches(castList[c].toString())) {
                                node.riseCount();
                                done = true;
                                //System.out.println(castList[c].toString() + " - " + node.getCount());
                            }
                        }
                        if (done == false) {
                            StatistikNode node = new StatistikNode(castList[c].toString());
                            statistikSchauspielerList.add(node);
                            //System.out.println(castList[c].toString());
                        }
                    }
                }


                // Statistik-Daten der Kategorien:
                String kategorie = film.get("kategorie").toString();
                    //System.out.println(i + ". kategorie: " + kategorie);
                if(!kategorie.matches("nicht gefunden")) {
                    String[] kategorieList = kategorie.split(", ");
                    //System.out.println(kategorieList.length);

                    for (int c = 0; c < kategorieList.length; c++) {
                        boolean done = false;
                        for (StatistikNode node : statistikKategorieList) {
                            if (node.getName().matches(kategorieList[c].toString())) {
                                node.riseCount();
                                done = true;
                                //System.out.println(kategorieList[c].toString() + " - " + node.getCount());
                            }
                        }
                        if (done == false) {
                            StatistikNode node = new StatistikNode(kategorieList[c].toString());
                            statistikKategorieList.add(node);
                            //System.out.println(kategorieList[c].toString());
                        }
                    }
                }


                // Statistik-Daten der Filme
                StatistikNode filmNode = new StatistikNode(film.get("name").toString());
                filmNode.setCount(0);
                JSONArray bewertungen = handler.sendGETRequest("/controller/bewertung/" + film.get("filmId"));
                if(!bewertungen.isEmpty()) {
                    for (int j = 0; j < bewertungen.length(); j++) {
                        JSONObject bewerteterFilm = bewertungen.getJSONObject(j);
                        if (bewerteterFilm.get("nutzerEmail").toString().equals(AngemeldeterUser.getSpeicher().getEmail())) {
                            filmNode.setCount(Integer.parseInt(bewerteterFilm.get("sterne").toString()));
                        }
                    }
                }
                statistikFilmList.add(filmNode);
                //System.out.println(filmNode.getName().toString());


                // Statistik der Watchtime
                timeWatched += Integer.parseInt(film.get("filmlaenge").toString());
            }


            // Daten für die Statistik auswerten
            int rank = 1;

            // Statistik für Lieblingsschauspieler auswerten:
            while(!statistikSchauspielerList.isEmpty()) {
                StatistikNode castNode = null;
                for (int i = 0; i < statistikSchauspielerList.size(); i++) {
                    if (i == 0) {
                        castNode = statistikSchauspielerList.get(i);
                    } else {
                        if (castNode.getCount() < statistikSchauspielerList.get(i).getCount()) {
                            castNode = statistikSchauspielerList.get(i);
                        }
                    }
                }
                //System.out.println(castNode.getName().toString() + castNode.getCount());
                if(erweiterteStatistikBox.isSelected()){
                    listSchauspieler.add(rank + " | " + castNode.getName().toString() + " | in " + castNode.getCount() + " Filmen");
                }else{
                    listSchauspieler.add(rank + " | " + castNode.getName().toString());
                }
                statistikSchauspielerList.remove(castNode);
                rank++;
            }
            rank = 1;

            // Statistik für Lieblingskategorien auswerten:
            while(!statistikKategorieList.isEmpty()) {
                StatistikNode kategorieNode = null;
                for (int i = 0; i < statistikKategorieList.size(); i++) {
                    if (i == 0) {
                        kategorieNode = statistikKategorieList.get(i);
                    } else {
                        if (kategorieNode.getCount() < statistikKategorieList.get(i).getCount()) {
                            kategorieNode = statistikKategorieList.get(i);
                        }
                    }
                }
                //System.out.println(kategorieNode.getName().toString() + kategorieNode.getCount());
                if(erweiterteStatistikBox.isSelected()){
                    listKategorie.add(rank + " | " + kategorieNode.getName().toString() + " | in " + kategorieNode.getCount() + " Filmen");
                }else{
                    listKategorie.add(rank + " | " + kategorieNode.getName().toString());
                }
                statistikKategorieList.remove(kategorieNode);
                rank++;
            }
            rank = 1;

            // Statistik für Lieblingsfilme auswerten:
            while(!statistikFilmList.isEmpty()) {
                StatistikNode filmNode = null;
                for (int i = 0; i < statistikFilmList.size(); i++) {
                    if (i == 0) {
                        filmNode = statistikFilmList.get(i);
                    } else {
                        if (filmNode.getCount() < statistikFilmList.get(i).getCount()) {
                            filmNode = statistikFilmList.get(i);
                        }
                    }
                }
                //System.out.println(filmNode.getName().toString() + filmNode.getCount());
                if(erweiterteStatistikBox.isSelected()){
                    listFilme.add(rank + " | " + filmNode.getName().toString() + " | Bewertung: " + filmNode.getCount());
                }else{
                    listFilme.add(rank + " | " + filmNode.getName().toString());
                }
                statistikFilmList.remove(filmNode);
                rank++;
            }


            // Füllen der Listen zum Anzeigen im View

            lieblingsSchauspielerList.getItems().addAll(listSchauspieler);
            lieblingsKategorieList.getItems().addAll(listKategorie);
            lieblingsFilmList.getItems().addAll(listFilme);
            watchTime.setText(String.valueOf(timeWatched) + " min");
            anzahlDerFilme.setText(String.valueOf(filmeImZeitraum.length()));
        }

    }

}
