package client.client.Soziales.Einladungen;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class EinladungMenueViewController {
    public Button invitationmanagement_button;
    public Button invitation_button;
    public Button startview_button;

    public void invitationmanagement(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Freunde_EinladungenVerwaltung.fxml"));
        Stage stage = (Stage) invitationmanagement_button.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void invitationview(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Freunde_EinladungenVerschicken.fxml"));
        Stage stage = (Stage) invitation_button.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void startview(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startview_button.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }
}
