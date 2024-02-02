package client.client.Anlegen;

import Controller.ClientServerHandler;
import Controller.JsonController;
import client.client.Zwischenspeicher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChangeMovieDataController implements Initializable {

    @FXML
    public Button backButton;

    @FXML
    public TextField nameField, categoryField, lengthField, releaseField, directorField, scriptField, castField;
    public ImageView filmbanner;

    private Image bildBanner;

    private byte[] bannerByte;



    Alert popUp = new Alert(Alert.AlertType.INFORMATION);

    ClientServerHandler csh = new ClientServerHandler();

    int speicher = Zwischenspeicher.getSpeicher().getFilmId();

    public ChangeMovieDataController() throws MalformedURLException {

    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        JSONObject filmObject = new JSONObject();
        JsonController jsc = null;
        try {
            jsc = new JsonController();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        try {
            filmObject = jsc.ueberpruefeFilmId(speicher);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        nameField.setText(filmObject.get("name").toString());
        categoryField.setText((filmObject.get("kategorie").toString()));
        lengthField.setText(filmObject.get("filmlaenge").toString());
        releaseField.setText(filmObject.get("erscheinungsdatum").toString());
        directorField.setText(filmObject.get("regisseur").toString());
        scriptField.setText(filmObject.get("drehbuchautor").toString());
        castField.setText(filmObject.get("cast").toString());



    }

    public void dataPressed() throws IOException, InterruptedException {

        int speicher = Zwischenspeicher.getSpeicher().getFilmId();

        //überprüft ob alle pflicht felder eingegeben sind
        if (nameField.getText().isEmpty() || categoryField.getText().isEmpty() || lengthField.getText().isEmpty() || releaseField.getText().isEmpty() ||
                directorField.getText().isEmpty() || scriptField.getText().isEmpty() || castField.getText().isEmpty() ) {

            //fehlermeldung wenn nicht alle pflichtfelder ausgefüllt sind
            popUp.setAlertType(Alert.AlertType.WARNING);
            popUp.setHeaderText("Fehler");
            popUp.setContentText("Fehlende Daten, bitte erneut eingeben.");
            popUp.showAndWait();
        } else {
            //erstellt und füllt das Film JsonObject mit den in den feldern eingegeben daten
            if(checkDate(releaseField.getText()) || checkDate4(releaseField.getText())) {
                if(!checkDigits(lengthField.getText())) {

                    JSONObject film = new JSONObject();

                    film.put("name", nameField.getText());
                    film.put("kategorie", categoryField.getText());
                    film.put("filmlaenge", lengthField.getText());
                    film.put("erscheinungsdatum", releaseField.getText());
                    film.put("regisseur", directorField.getText());
                    film.put("drehbuchautor", scriptField.getText());
                    film.put("cast", castField.getText());
                    film.put("filmbanner", bannerByte);

                    // sendet eine PutRequest an folgenden pfad mit dem film jsonobject
                    csh.sendPutRequest("/films/" + speicher, film);

                    Alert report = new Alert(Alert.AlertType.INFORMATION);
                    report.setAlertType(Alert.AlertType.CONFIRMATION);
                    report.setHeaderText("Erfolgreich!");
                    report.setContentText("Die Daten wurden erfolgreich geändert!");
                    report.showAndWait();

                    //navigiert zur FilmIDCheck.fxml
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FilmIDCheck.fxml"));
                    Stage stg = (Stage) backButton.getScene().getWindow();
                    Scene scene = new Scene(loader.load());
                    stg.setScene(scene);
                }else{
                    popUp.setAlertType(Alert.AlertType.ERROR);
                    popUp.setHeaderText("Feld Fehler");
                    popUp.setContentText("Bitte füllen Sie alle Felder korrekt aus!\nFilmlänge besteht nur aus Zahlen in Minutenangabe!");
                    popUp.showAndWait();
                }
            }
            else{
                popUp.setAlertType(Alert.AlertType.ERROR);
                popUp.setHeaderText("Feld Fehler");
                popUp.setContentText("Bitte füllen Sie alle Felder korrekt aus!\nDatum hat das Format dd.mm.YYYY oder YYYY und nimmt keine Buchstaben an!");
                popUp.showAndWait();
            }
        }

    }

    //navigiert zur FilmIDCheck.fxml
    public void backPressed() throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/FilmIDCheck.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }


    // Analoge Methode zu FilmAnlegenController zum Einfügen des Bildes
    public void bannerCheck(MouseEvent mouseEvent)throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Filmbanner einfuegen");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Image bild = new Image(file.toURI().toString());
            filmbanner.setImage(bild);
            bildBanner = filmbanner.getImage();
            bannerByte = bildZuByte(file);
        }
    }

    // Analoge Methode zu FilmAnlegenController zum Einfügen des Bildes
    private byte[] bildZuByte(File file) throws IOException{
        BufferedImage banner = ImageIO.read(file);
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        ImageIO.write(banner, "jpg", array);
        byte [] bannerByte = array.toByteArray();
        return bannerByte;

    }
    private boolean checkDigits(String string) {
        for (int i = 0; i <= string.length() - 1; i++) {
            String tmp = "" + string.charAt(i);
            if (tmp.matches("[A-Za-z]")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDate(String string) {
        boolean b=true;
        int punkt=0;
        if(string.length()!=10){
            return false;
        }
        for (int i = 0; i <= string.length() - 1; i++) {
            String tmp = "" + string.charAt(i);
                if(i==2 || i==5){
                    if(!tmp.matches(".")){
                        b=false;
                    }
                }else{
                    if(!tmp.matches("[0-9]")){
                        b=false;
                    }
            }
        }
        return b;
    }
    public boolean checkDate4(String datum){
        if(datum.length()!=4){
            return false;
        }
        for(int i=0; i<=datum.length()-1;i++){
            String tmp= ""+datum.charAt(i);
            if(!tmp.matches("[0-9]")){
                return false;
            }
        }
        return true;
    }
}
