package client.client.Filmeinladung;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class FilmeinladungMenueController {

    public Button verschickenButton;

    public Button verwaltenButton;

    public void verwaltenPressed() throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/filmeinladung3.1.1.3.fxml"));
        Stage stage = (Stage) verwaltenButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);

    }

    public void verschickenPressed() throws IOException{
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/filmeinladung.fxml"));
        Stage stage = (Stage) verwaltenButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void startseitePressed() throws IOException{
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) verwaltenButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }
}
