package client.client.Soziales.Discussion;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Disc_MenueController {
    public Button discussionSearch_button;
    public Button myDiscussions_button;
    public Button startviewbutton;
    public Button back_button;

    public void discussionSerach_view(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Discussion_SearchView.fxml"));
        Stage stage = (Stage) discussionSearch_button.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void myDiscussions_View(ActionEvent actionEvent)throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Discussion_OpenView.fxml"));
        Stage stage = (Stage) myDiscussions_button.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void startview(ActionEvent actionEvent)throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startviewbutton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void invitationPressed(ActionEvent actionEvent) throws IOException{
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Discussion_InviteView.fxml"));
        Stage stage = (Stage) startviewbutton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }
}
