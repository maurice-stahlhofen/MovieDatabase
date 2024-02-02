package client.client.Fehlermeldung;

import Controller.ClientServerHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;

public class NutzerFehlermeldung {

    public TextField filmname;
    public TextArea fehlernachricht;

    public Label missingFilmname, zulangeNachricht;
    public Button startseiteButton;

    ClientServerHandler handler = new ClientServerHandler();

    public NutzerFehlermeldung() throws IOException { }
    public void startseitePressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startseiteButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void fehlermeldungAbsendenPressed(ActionEvent actionEvent) throws IOException, InterruptedException {
        // Überprüfen ob die Eingaben gültig sind
        missingFilmname.setText("");
        zulangeNachricht.setText("");
        if(filmname.getText().isEmpty()){
            missingFilmname.setText("Bitte einen Filmnamen eingeben");
            return;
        }
        if(fehlernachricht.getText().length() > 252) {
            zulangeNachricht.setText("Die eingegebene Nachricht ist zu lang");
            return;
        }

        // Speichern der Fehlermeldung und Absenden der Adminbenachrichtigungen
        else{
            JSONObject fehlermeldung = new JSONObject();
            fehlermeldung.put("filmName", filmname.getText());
            fehlermeldung.put("nachricht", fehlernachricht.getText());
            fehlermeldung.put("date", LocalDate.now());

            handler.sendPostRequest("/controller/fehlermeldung/add", fehlermeldung);
            handler.sendPostRequest("/api/sendAdminBenachrichtigug", fehlermeldung);

            Alert report = new Alert(Alert.AlertType.NONE, ("Fehlermeldung erfolgreich abgesendet"), ButtonType.OK);
            report.setTitle("Erfolgreich!");
            report.showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
            Stage stg = (Stage) startseiteButton.getScene().getWindow();
            Scene sc = new Scene(loader.load());
            stg.setScene(sc);
        }
    }

}
