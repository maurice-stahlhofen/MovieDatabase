package client.client.Statistik;

import Controller.ClientServerHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PopupController {


    public ImageView confirm;
    public ImageView cancel;
    public CheckBox Bewertung;
    public CheckBox GeseheneFilme;
    public Text reportTxt;

    private List<Integer> selectedFilmIDs = StatistikZwischenspeicher.getSpeicher().getList();

    ClientServerHandler csh = new ClientServerHandler();

    public PopupController() throws MalformedURLException {
    }

    public void confirmPopup(MouseEvent mouseEvent) throws IOException {

        if (selectedFilmIDs == null) {

            confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> exit((Node) mouseEvent.getSource()));
        } else {

            // Bewertungen und gesehene Filme der Auswahl werden gelöscht
            if (Bewertung.isSelected() && GeseheneFilme.isSelected()) {

                for (int i = 0; i < selectedFilmIDs.size(); i++) {
                    String response = csh.sendGETRequestGetString("/controller/bewertung/delete/" + selectedFilmIDs.get(i));
                    System.out.println("Bewertung(en) " + response);
                }


                for (int i = 0; i < selectedFilmIDs.size(); i++) {
                    String response = csh.sendGETRequestGetString("/controller/geseheneFilme/delete/" + selectedFilmIDs.get(i));
                    System.out.println("Gesehene Filme " + response);
                }

                // Gesehene Filme der Auswahl werden gelöscht
            } else if (GeseheneFilme.isSelected()) {
                for (int i = 0; i < selectedFilmIDs.size(); i++) {
                    String response = csh.sendGETRequestGetString("/controller/geseheneFilme/delete/" + selectedFilmIDs.get(i));
                    System.out.println("Gesehene Filme " + response);
                }

                // Bewertungen der Auswahl werden gelöscht
            } else if (Bewertung.isSelected()) {
                for (int i = 0; i < selectedFilmIDs.size(); i++) {
                    String response = csh.sendGETRequestGetString("/controller/bewertung/delete/" + selectedFilmIDs.get(i));
                    System.out.println("Bewertung(en) " + response);
                }
            }

            confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> exit((Node) mouseEvent.getSource()));

        }
    }

    public void cancelPopup(MouseEvent mouseEvent) {

        if (selectedFilmIDs == null) {
            cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> exit((Node) mouseEvent.getSource()));
        } else {
            cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> exit((Node) mouseEvent.getSource()));
        }

    }

    private void exit(Node node) {
        Stage stage = ((Stage) (node).getScene().getWindow());
        stage.close();
    }


}
