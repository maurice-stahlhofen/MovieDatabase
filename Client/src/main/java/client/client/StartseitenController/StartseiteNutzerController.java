package client.client.StartseitenController;

import client.client.login.AngemeldeterUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class StartseiteNutzerController implements Initializable {


    public Button registerButton;
    public Button userViewButton;

    public Button userSearchButton;
    public Button freundeButton;
    public Button chatButton;
    public Button gruppenButton;

    public Button filmeSuchenButton;
    public Button filmBewertenButton;
    public Button filmEinladenButton;
    public Button statistikButton;
    public Button reportButton;

    public Button vorschlaegeButton;

    public Label nutzerName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nutzerName.setText(AngemeldeterUser.getSpeicher().getVorname()
                + " " + AngemeldeterUser.getSpeicher().getNachname());

    }

    public ImageView nutzerProfilbild;

    public void registerPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Registrierung.fxml"));
        Stage stage = (Stage) registerButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setTitle("Movie-Chat");
        stage.setScene(scene);
    }

    public void userViewPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Nutzeruebersicht.fxml"));
        Stage stage = (Stage) userViewButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void userSearchPressed(ActionEvent event) throws IOException{
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Nutzersuche.fxml"));
        Stage stage = (Stage) userSearchButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void freundePressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Freunde_EinladungenMenueView.fxml"));
        Stage stage = (Stage) freundeButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void chatButtonPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Chat_MenueView.fxml"));
        Stage stage = (Stage) chatButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void gruppenButtonPressed(ActionEvent actionEvent) throws IOException {
            FXMLLoader laden = new FXMLLoader(getClass().getResource("/Discussion_MenueView.fxml"));
            Stage stage = (Stage) gruppenButton.getScene().getWindow();
            Scene scene = new Scene(laden.load());
            stage.setScene(scene);
    }

    public void filmeSuchenPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/anzeigenlistview.fxml"));
        Stage stage = (Stage) filmeSuchenButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void statistikenPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StatistikNutzer.fxml"));
        Stage stage = (Stage) filmeSuchenButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void filmEinladenPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/filmeinladung3.1.1.fxml"));
        Stage stage = (Stage) filmeSuchenButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void reportPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/NutzerFehlermeldung.fxml"));
        Stage stage = (Stage) reportButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void vorschlaegePressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/VorschlaegeView.fxml"));
        Stage stage = (Stage) vorschlaegeButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }
}
