package client.client.login;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.FontFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TwoFAClientController {

    public Button confirmButton;
    public Button cancelButton;
    public TextField codeText;


    // Überprüfung --> Texteingabe = 2FA-Code ?
    public void checkCode(ActionEvent actionEvent) throws IOException {
        if (codeText.getText().toString() == null || !codeText.getText().toString().equals(CodeHandler.getCodeHandler().getCode())) {

            // Code stimmt nicht überein. Zurück zur Login-Seite
            JPanel panelError = new JPanel();
            panelError.add(new java.awt.Label("Eingabe war nicht korrekt!"));
            JOptionPane.showMessageDialog(null, panelError, "ERROR", JOptionPane.ERROR_MESSAGE);

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Stage stg2 = (Stage) confirmButton.getScene().getWindow();
            Scene sc2 = new Scene(loader2.load());
            stg2.setTitle("Movie-Chat");
            stg2.setScene(sc2);

        } else if (CodeHandler.getCodeHandler().getCode().equals(codeText.getText().toString())) {
            // Eingabe stimmt mit 2FA-Code aus Codehandler überein

            JPanel panelSuccess = new JPanel();
            panelSuccess.add(new java.awt.Label(AngemeldeterUser.getSpeicher().getVorname() + ", du wirst als NUTZER eingeloggt!"));
            JOptionPane.showMessageDialog(null, panelSuccess, "ERFOLGREICH", JOptionPane.INFORMATION_MESSAGE);

            // Wechseln zur Startseite
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
            Stage stg2 = (Stage) confirmButton.getScene().getWindow();
            Scene sc2 = new Scene(loader2.load());
            stg2.setTitle("Movie-Chat - NUTZER - "+ AngemeldeterUser.getSpeicher().getVorname() + " " + AngemeldeterUser.getSpeicher().getNachname());
            stg2.setScene(sc2);
        }
    }


    public void backToLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Stage stg2 = (Stage) cancelButton.getScene().getWindow();
        Scene sc2 = new Scene(loader2.load());
        stg2.setTitle("Movie-Chat");
        stg2.setScene(sc2);
    }


}
