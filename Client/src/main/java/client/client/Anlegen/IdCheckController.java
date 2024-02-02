package client.client.Anlegen;

import Controller.JsonController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import org.json.JSONObject;
import client.client.Zwischenspeicher;

import java.io.IOException;
import java.net.MalformedURLException;

public class IdCheckController {

    @FXML
    public TextField movieIDField;

    @FXML
    public Button startseiteButton, searchButton;
    public Label invalidInput;

    JsonController jsh = new JsonController();
    JSONObject film;

    Alert popUp = new Alert(Alert.AlertType.INFORMATION);

    public IdCheckController() throws MalformedURLException {
    }

    //Überprüft ob die eingegeben Film-ID in der Datenbank vorhanden ist
    public void idCheckPressed() throws IOException {

        if(movieIDField.getText().isEmpty()){
            popUp.setAlertType(Alert.AlertType.WARNING);
            popUp.setHeaderText("Fehler");
            popUp.setContentText("Bitte eine Zahl eingeben!");
            popUp.showAndWait();
        }else
        // setzt film auf das zurückgegeben object aus ueberpruefeFilmId
        film = jsh.ueberpruefeFilmId(Integer.parseInt(movieIDField.getText()));

        // Wenn keine FilmID gefunden wurde, wird die Fehlermeldung angezeigt. Es wurde ein leeres Json-Object zurückgegeben
        if (film.length() == 0) {
            popUp.setAlertType(Alert.AlertType.WARNING);
            popUp.setHeaderText("Fehler");
            popUp.setContentText("Kein Film mit dieser ID gefunden!");
            popUp.showAndWait();
        }


        // Es wird in die nächste Seite gewechselt, da die FilmId vorliegt
        else {
            Zwischenspeicher.getSpeicher().setFilmId(Integer.parseInt(movieIDField.getText()));
            FXMLLoader laden = new FXMLLoader(getClass().getResource("/ChangeMovieData.fxml"));
            Stage stage = (Stage) startseiteButton.getScene().getWindow();
            Scene scene = new Scene(laden.load());
            stage.setScene(scene);

        }


    }

    //Wechselt zurück zur Startseite
    public void startseitePressed(ActionEvent event) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteAdminView.fxml"));
        Stage stage = (Stage) startseiteButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
        
    }


}
