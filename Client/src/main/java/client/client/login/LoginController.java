package client.client.login;

import Controller.ClientServerHandler;
import Controller.JsonController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;

public class LoginController {

    public Label wrongLogin;
    public Button back;
    @FXML
    public Button button;

    @FXML
    public TextField email;

    @FXML
    public PasswordField passwort;
    public ImageView loginBild;
    public Label informationText;


    JsonController jsh = new JsonController();
    JSONObject  loginObject;

    public LoginController() throws MalformedURLException {
    }

    public void userLogIn(ActionEvent button) throws IOException, InterruptedException {
        checkLogin();
    }

    public void checkLogin() throws IOException, InterruptedException {

        loginObject = jsh.ueberpruefeLogin(email.getText().toString());

        // Ein Feld nicht ausgefüllt
        if (email.getText().toString().length() == 0 & passwort.getText().toString().length() == 0) {
            wrongLogin.setText("Bitte alle Felder ausfüllen!");

            //Keinen Nutzer gefunden, da Objekt leer
        } else if ( loginObject.length() == 0) {
            wrongLogin.setText("Keinen Nutzer gefunden");


        } else if (passwort.getText().equals( loginObject.get("passwort").toString())) {
                // Passwort stimmt mit Eingabe überein

            switch ( loginObject.get("userType").toString()) {

                // ID 1 ist Adminlogin
                case "1":

                    // Name des angemeldeten Admins zwischenspeichern
                    AngemeldeterUser.getSpeicher().setVorname( loginObject.get("vorname").toString());
                    AngemeldeterUser.getSpeicher().setNachname( loginObject.get("nachname").toString());

                    Alert report = new Alert(Alert.AlertType.NONE, ( loginObject.get("vorname") + ", du wirst als ADMIN eingeloggt!"), ButtonType.OK);
                    report.setTitle("Erfolgreich!");
                    report.showAndWait();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/StartseiteAdminView.fxml"));
                    Stage stg = (Stage) button.getScene().getWindow();
                    Scene sc = new Scene(loader.load());
                    stg.setTitle("Movie-Chat - ADMIN - "+ loginObject.get("vorname")+ " " +  loginObject.get("nachname"));
                    stg.setScene(sc);
                    break;


                    // ID 2 ist der Nutzerlogin
                case "2":
                    informationText.setText("");

                    // Daten zum Angemeldeten User zwischenspeichern für andere Features
                    AngemeldeterUser.getSpeicher().setVorname( loginObject.get("vorname").toString());
                    AngemeldeterUser.getSpeicher().setNachname( loginObject.get("nachname").toString());
                    AngemeldeterUser.getSpeicher().setEmail( loginObject.get("email").toString());
                    AngemeldeterUser.getSpeicher().setPasswort(( loginObject.get("passwort").toString()));

                   System.out.println("Bitte warten, die Mail wird versendet!");
                    ClientServerHandler csh = new ClientServerHandler();

                    String code = "";
                    // Übergabe der Email + Namen an Server
                    // Server schickt Email
                    // tempString enthält den Code
                    try {
                        code = csh.sendGETRequestGetString("/api/send2FAViaEmail/" + loginObject.get("email").toString() + "/" + loginObject.get("vorname").toString());


                    // Codehandler wird der 2FA-Code übergeben
                    CodeHandler.getCodeHandler().setCode(code);
                    System.out.println("Der Code lautet: " + code);
                    System.out.println("Mail ist raus!");

                    } catch(Exception e){
                        System.out.println();
                        System.out.println("------ WORKAROUND -----");
                        System.out.println("Passwort war korrekt");
                        System.out.println();
                        System.out.println("2FA-Mail konnte nicht versendet werden. Kein Internet oder Provider-Connection gesperrt");
                        System.out.println("Gib den Code ein: 1111");
                        code = "1111";
                        CodeHandler.getCodeHandler().setCode(code);
                    }

                    // Wechseln zur 2FA-Seite
                    FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/ZweiFA.fxml"));
                    Stage stg3 = (Stage) button.getScene().getWindow();
                    Scene sc3 = new Scene(loader3.load());
                    stg3.setTitle("2-Faktor Authentifizierung");
                    stg3.setScene(sc3);

                        break;


                default:
                    wrongLogin.setText("Bitte nochmal einloggen!");

            }


        } else {
            wrongLogin.setText("Falsches Passwort!");
        }
    }


    public void windowBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Registrierung.fxml"));
        Stage stg = (Stage) back.getScene().getWindow();
        Scene sc = new Scene(loader.load());
        stg.setScene(sc);
    }
}

