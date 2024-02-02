package client.client.registrierung;

import Controller.ClientServerHandler;
import Controller.JsonController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrierungsController {


    public BorderPane systemAdmin;
    public Button login;
    public Button exit;
    public Tab sysAdmin;
    public TextField email;
    public TextField vName;
    public TextField nName;
    public PasswordField passwort;

    public Tab benutzer;
    public Text invalid;
    public Button registrationToLogin;
    public TextField vNameNutzer;
    public TextField nNameNutzer;
    public PasswordField passwortNutzer;
    public Button nutzerRegistrieren;
    public TextField emailNutzer;
    public DatePicker gebDatum;
    public Text invalidNutzerText;
    public Button adminRegistrieren;
    public ImageView profilBild;

    private Image profil;

    private byte[] profilByte;

    ClientServerHandler csh = new ClientServerHandler();

    public RegistrierungsController() throws MalformedURLException {
    }

    public void userLogin(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Stage stg = (Stage) login.getScene().getWindow();
        Scene sc = new Scene(loader.load());
        stg.setScene(sc);


    }

    public void closeProgramm(ActionEvent event) throws IOException {
        System.exit(0);

    }


    public void adminSignUp(ActionEvent actionEvent) throws IOException, InterruptedException {

        checkSignUpADMIN();

    }

    private void checkSignUpADMIN() throws IOException, InterruptedException {
        if (email.getText().toString().isEmpty() || passwort.getText().toString().isEmpty() || vName.getText().toString().isEmpty() || nName.getText().toString().isEmpty()) {
            invalid.setText("Bitte ALLE Felder ausfüllen!");

            //Namen auf Korrektheit prüfen
        } else if (checkLetters(vName.getText().toString()) == false || checkLetters(nName.getText().toString()) == false) {
            invalid.setText("Die Eingabe ist kein Name");

        } else {

            // email-check
            // Folgende 3 Zeilen sind von:
            // https://stackoverflow.com/questions/8204680/java-regex-email
            Pattern VALID_EMAIL_ADDRESS_REGEX =
                    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email.getText().toString());


            if (matcher.matches() == false) {
                invalid.setText("Das ist keine E-Mail: " + email.getText().toString());
            }else

             {
                JsonController jc = new JsonController();
                JSONObject emailChecker = jc.ueberpruefeLogin(email.getText().toString());

                //Prüfe ob Email in DB existiert
                if (emailChecker.length() != 0) {
                    invalid.setText("E-Mail existiert bereits!");
                } else {

                    vName.setText(vName.getText().toString().substring(0, 1).toUpperCase() + vName.getText().toString().substring(1, vName.getText().toString().length()));
                    nName.setText(nName.getText().toString().substring(0, 1).toUpperCase() + nName.getText().toString().substring(1, nName.getText().toString().length()));

                    JSONObject admin = new JSONObject();
                    admin.put("vorname", vName.getText());
                    admin.put("nachname", nName.getText());
                    admin.put("email", email.getText());
                    admin.put("passwort", passwort.getText());

                    csh.sendPostRequest("/controller/admin/add", admin);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/registrierungSuccess.fxml"));
                    Stage stg = (Stage) adminRegistrieren.getScene().getWindow();
                    Scene sc = new Scene(loader.load());
                    stg.setScene(sc);

                }

            }

        }
    }


    public void registrationSwitchToLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Stage stg = (Stage) registrationToLogin.getScene().getWindow();
        Scene sc = new Scene(loader.load());
        stg.setScene(sc);
    }

    private boolean checkLetters(String field) {

        for (int i = 0; i < field.length() - 1; i++) {
            String temp = "" + field.charAt(i);
            if (i == 0 && (!temp.matches("[A-Za-züäö]"))) {
                return false;
            }

            if (!temp.matches("[üäöA-Za-z-]")) {
                return false;
            }
        }
        return true;
    }

    public void nutzerSignUp(ActionEvent actionEvent) throws IOException, InterruptedException {
        checkSignUpNUTZER();
    }

    private void checkSignUpNUTZER() throws IOException, InterruptedException {
        if (emailNutzer.getText().toString().isEmpty() || passwortNutzer.getText().toString().isEmpty() || vNameNutzer.getText().toString().isEmpty() || nNameNutzer.getText().toString().isEmpty() || gebDatum.getValue() == null) {
            invalidNutzerText.setText("Bitte ALLE Felder ausfüllen!");

            //Namen auf Korrektheit prüfen
        } else if (checkLetters(vNameNutzer.getText().toString()) == false || checkLetters(nNameNutzer.getText().toString()) == false) {
            invalidNutzerText.setText("Die Eingabe ist kein Name");

        } else {

            // Email-Check:
            // Folgende 3 Zeilen sind von:
            // https://stackoverflow.com/questions/8204680/java-regex-email
            Pattern VALID_EMAIL_ADDRESS_REGEX =
                    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailNutzer.getText().toString());


            if (matcher.matches() == false) {
                invalidNutzerText.setText("Das ist keine E-Mail: " + emailNutzer.getText().toString());
            } else {
                JsonController jc = new JsonController();
                JSONObject emailChecker = jc.ueberpruefeLogin(emailNutzer.getText().toString());

                //Prüfe ob Email in DB existiert
                if (emailChecker.length() != 0) {
                    invalidNutzerText.setText("E-Mail existiert bereits!");
                } else {

                    vNameNutzer.setText(vNameNutzer.getText().toString().substring(0, 1).toUpperCase() + vNameNutzer.getText().toString().substring(1, vNameNutzer.getText().toString().length()));
                    nNameNutzer.setText(nNameNutzer.getText().toString().substring(0, 1).toUpperCase() + nNameNutzer.getText().toString().substring(1, nNameNutzer.getText().toString().length()));

                    JSONObject nutzer = new JSONObject();
                    nutzer.put("vorname", vNameNutzer.getText());
                    nutzer.put("nachname", nNameNutzer.getText());
                    nutzer.put("email", emailNutzer.getText());
                    nutzer.put("gebDatum", gebDatum.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    nutzer.put("passwort", passwortNutzer.getText());
                    nutzer.put("zProfilbild", profilByte);

                    csh.sendPostRequest("/controller/nutzer/add", nutzer);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/registrierungSuccess.fxml"));
                    Stage stg = (Stage) nutzerRegistrieren.getScene().getWindow();
                    Scene sc = new Scene(loader.load());
                    stg.setScene(sc);

                }

            }

        }
    }

    //Quelle Maurice - Filmbanner anlegen
    private byte[] bildToByte(File file) throws IOException {
        BufferedImage bild = ImageIO.read(file);
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        ImageIO.write(bild, "jpg", array);
        byte[] profilByte = array.toByteArray();
        return profilByte;
    }

    //Quelle Maurice - Filmbanner anlegen
    public void bannerCK(MouseEvent mouseEvent) throws IOException {
        FileChooser exp = new FileChooser();
        exp.setTitle("Profilbild hinzufügen");
        File file = exp.showOpenDialog(null);
        if (file != null) {
            Image bild = new Image(file.toURI().toString());
            profilBild.setImage(bild);
            profil = profilBild.getImage();

            profilByte = bildToByte(file);
        }
    }
}