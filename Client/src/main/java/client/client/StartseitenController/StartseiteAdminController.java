package client.client.StartseitenController;

import client.client.login.AngemeldeterUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartseiteAdminController implements Initializable {


    // ADMIN hat eine andere Sicht als der Nutzer. Daher brauchen wir 2 Startseiten

    public Button filmAnlegen, filmImport, filmBearbeiten;
    public Button registerButton;
    public Button statistik;

    public Label adminName;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        adminName.setText(AngemeldeterUser.getSpeicher().getVorname()
                + " " + AngemeldeterUser.getSpeicher().getNachname());
    }

    public void insertPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/FilmAnlegen.fxml"));
        Stage stage = (Stage) filmAnlegen.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void importPressed(ActionEvent actionEvent) throws IOException{
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/AutomatischAnlegen.fxml"));
        Stage stage = (Stage) filmImport.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void editPressed(ActionEvent actionEvent) throws  IOException{
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/FilmIDCheck.fxml"));
        Stage stage = (Stage) filmBearbeiten.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }


    public void registerPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Registrierung.fxml"));
        Stage stage = (Stage) filmBearbeiten.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setTitle("Movie-Chat");
        stage.setScene(scene);
    }

    public void fehlermeldungPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/AdminFehlermeldung.fxml"));
        Stage stage = (Stage) filmBearbeiten.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }
    public void getStatistik(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Statistik_Admin/StatistikADMIN.fxml"));
        Stage stage = (Stage) statistik.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }


}
