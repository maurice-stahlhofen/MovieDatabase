package client.client.Anlegen;

import Controller.ClientServerHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class FilmAnlegenController {

    @FXML
    public TextField kategorieField,
            nameField,
            castField,
            drehbuchautorField,
            regisseurField,
            filmlaengeField,
            erscheinungsdatumField;

    public ImageView filmbanner;

    private Image banner;

    private byte[] bannerByte;

    Alert popUp = new Alert(Alert.AlertType.INFORMATION);

    public Button filmanlegenButton, startseiteButton;

    ClientServerHandler handler = new ClientServerHandler();

    public FilmAnlegenController() throws IOException {

    }


    public void startseitePressed(ActionEvent event) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteAdminView.fxml"));
        Stage stage = (Stage) startseiteButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }


    public void anlegenPressed(ActionEvent event) throws IOException, InterruptedException {
        if (nameField.getText().isEmpty() || castField.getText().isEmpty() || kategorieField.getText().isEmpty() ||
                drehbuchautorField.getText().isEmpty() || filmlaengeField.getText().isEmpty()
                || erscheinungsdatumField.getText().isEmpty() || regisseurField.getText().isEmpty()) {
            popUp.setAlertType(Alert.AlertType.ERROR);
            popUp.setHeaderText("Feld Fehler");
            popUp.setContentText("Bitte füllen Sie alle Felder aus!");
            popUp.showAndWait();
            
        } else if (checkDigits(filmlaengeField.getText()) == true) {
            popUp.setAlertType(Alert.AlertType.ERROR);
            popUp.setHeaderText("Feld Fehler");
            popUp.setContentText("Bitte füllen Sie alle Felder korrekt aus!\nFilmlänge enthalten nur Zahlen!");
            popUp.showAndWait();

        }
        else if (checkDigits(castField.getText()) == false) {
            popUp.setAlertType(Alert.AlertType.ERROR);
            popUp.setHeaderText("Feld Fehler");
            popUp.setContentText("Bitte füllen Sie alle Felder korrekt aus!\nCast sollte keine Zahlen enthalten!");
            popUp.showAndWait();

        }else {
            if(checkDate(erscheinungsdatumField.getText()) || checkDate4(erscheinungsdatumField.getText())) {
                JSONObject film = new JSONObject();
                film.put("name", nameField.getText());
                film.put("cast", castField.getText());
                film.put("drehbuchautor", drehbuchautorField.getText());
                film.put("kategorie", kategorieField.getText());
                film.put("filmlaenge", Integer.parseInt(filmlaengeField.getText()));
                film.put("erscheinungsdatum", erscheinungsdatumField.getText());
                film.put("regisseur", regisseurField.getText());
                film.put("filmbanner", bannerByte);

                handler.sendPostRequest("/controller/film/add", film);

                popUp.setAlertType(Alert.AlertType.CONFIRMATION);
                popUp.setHeaderText("Erfolgreich!");
                popUp.setContentText(nameField.getText() + " wurde angelegt!");
                popUp.showAndWait();

                FXMLLoader laden = new FXMLLoader(getClass().getResource("/FilmAnlegen.fxml"));
                Stage stage = (Stage) startseiteButton.getScene().getWindow();
                Scene scene = new Scene(laden.load());
                stage.setScene(scene);
            }else{
                popUp.setAlertType(Alert.AlertType.ERROR);
                popUp.setHeaderText("Feld Fehler");
                popUp.setContentText("Bitte füllen Sie alle Felder korrekt aus!\nDatum hat das Format dd.mm.YYYY und yyyy und nimmt keine Buchstaben an!");
                popUp.showAndWait();

            }

        }
    }


    public void bannerCK(MouseEvent mouseEvent) throws IOException {
        FileChooser exp = new FileChooser();
        exp.setTitle("Filmbanner einfuegen");
        File file = exp.showOpenDialog(null);
        if (file != null) {
            Image bild = new Image(file.toURI().toString());
            filmbanner.setImage(bild);
            banner = filmbanner.getImage();

            bannerByte = bildZuByte(file);
        }
    }

    private byte[] bildZuByte(File file) throws IOException {
        BufferedImage banner = ImageIO.read(file);
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        ImageIO.write(banner, "jpg", array);
        byte[] bannerByte = array.toByteArray();
        return bannerByte;
    }

    // Der String wird an jeder Position durchlaufen und das einzelne Element wird dann auf Buchstaben überprüft.
// Ist nur ein Buchstabe dazwischen wird true ausgegeben und somit in anlegenPressed ein Fehler angezeigt
    private boolean checkDigits(String string) {
        for (int i = 0; i <= string.length() - 1; i++) {
            String tmp = "" + string.charAt(i);
            if (tmp.matches("[A-Za-z]")) {
                return true;
            }
        }
        return false;
    }
    private boolean checkKategorie(String kategorie) {
        String[] genre={"Action", "Adventure", "Animation", "Biography", "Comedy", "Crime", "Documentary",
                "Drama", "Family", "Fantasy", "Film Noir", "Game-Show", "History", "Horror", "Music", "Musical", "Mystery",
                "News", "Reality-TV", "Romance", "Sci-Fi", "Sport", "Talk Show", "Thriller", "War", "Western"};
        for(String check:genre){
            if(kategorie.equals(check)){
                return true;
            }
        }
        return false;
    }
    private boolean checkDate(String string) {
        boolean b=true;
        if(string.length()!=10){
            return false;
        }
        int punkt=0;
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
