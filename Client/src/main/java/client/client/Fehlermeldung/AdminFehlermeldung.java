package client.client.Fehlermeldung;

import Controller.ClientServerHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class AdminFehlermeldung implements Initializable {

    public TableColumn filmnameColumn, nachrichtColumn, dateColumn, idColumn;
    public TableView listeFehlermeldungen;
    public Button startseiteButton, bearbeitetButton;

    public Label keineAuswahl;

    ClientServerHandler handler = new ClientServerHandler();
    ObservableList<Fehlermeldung> list = FXCollections.observableArrayList();

    public AdminFehlermeldung() throws IOException { }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadData() throws IOException {
        // Lädt die Daten der Fehlermeldungen in die Tabelle zum anzeigen im View

        list.removeAll(list);
        JSONArray fehlermeldungen = handler.sendGETRequest("/controller/fehlermeldung");

        for (int i = 0; i < fehlermeldungen.length(); i++) {
            JSONObject tmp = fehlermeldungen.getJSONObject(i);

            Fehlermeldung fehlermeldung = new Fehlermeldung(tmp.get("filmName").toString(),
                              tmp.get("nachricht").toString(), tmp.get("date").toString(),
                              tmp.get("fehlermeldungsId").toString());

               list.add(fehlermeldung);
        }

        if(list.isEmpty()){
            System.out.println("Die Liste der Fehlermeldungen ist leer");
        }

        filmnameColumn.setCellValueFactory(new PropertyValueFactory<>("filmName"));
        nachrichtColumn.setCellValueFactory(new PropertyValueFactory<>("nachricht"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        listeFehlermeldungen.setItems(list);

        // funktionsweise zum einfügen von Daten in eine JavaFX TableView von:
        // https://medium.com/@keeptoo/adding-data-to-javafx-tableview-stepwise-df582acbae4f

    }



    public void startseitePressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteAdminView.fxml"));
        Stage stage = (Stage) startseiteButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void bearbeitetPressed(ActionEvent actionEvent) throws IOException, InterruptedException {
        // Löscht die markierte Fehlermeldung aus der Datenbank

        Fehlermeldung fehlermeldung = (Fehlermeldung) listeFehlermeldungen.getSelectionModel().getSelectedItem();
        keineAuswahl.setText("");
        if(fehlermeldung == null){
            keineAuswahl.setText("Keine Fehlermeldung ausgewählt");
            System.out.println("Keine Nachricht ausgewählt"); return;
        }

        JSONObject jsonFehlermeldung = new JSONObject();
        jsonFehlermeldung.put("filmName", fehlermeldung.getFilmName().toString());
        jsonFehlermeldung.put("nachricht", fehlermeldung.getNachricht().toString());
        jsonFehlermeldung.put("date", fehlermeldung.getDate().toString());
        jsonFehlermeldung.put("fehlermeldungsId", fehlermeldung.getId().toString());

        handler.sendPostRequest("/controller/fehlermeldung/delet", jsonFehlermeldung);

        Alert report = new Alert(Alert.AlertType.NONE, ("Fehlermeldung bearbeitet und gelöscht"), ButtonType.OK);
        report.setTitle("Erfolgreich");
        report.showAndWait();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminFehlermeldung.fxml"));
        Stage stg = (Stage) bearbeitetButton.getScene().getWindow();
        Scene sc = new Scene(loader.load());
        stg.setScene(sc);
    }

    public void anzeigenPressed(ActionEvent actionEvent) {
        // Zeigt die Nachricht der Fehlermeldung in einem Pop-Up-Fenster an

        Fehlermeldung fehlermeldung = (Fehlermeldung) listeFehlermeldungen.getSelectionModel().getSelectedItem();
        keineAuswahl.setText("");
        if(fehlermeldung == null){
            keineAuswahl.setText("Keine Fehlermeldung ausgewählt");
            System.out.println("Keine Nachricht ausgewählt"); return;
        }

        Alert report = new Alert(Alert.AlertType.INFORMATION, (fehlermeldung.getNachricht().toString()), ButtonType.OK);
        report.setTitle(fehlermeldung.getFilmName().toString());
        report.showAndWait();
    }

}
