package client.client.Filmeinladung;

import Controller.ClientServerHandler;
import client.client.login.AngemeldeterUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilmeinladungverschickenController {

    public Button startseiteButton;

    public Button zurueckButton;

    public TextField filmtitelField;

    public Alert popUp = new Alert(Alert.AlertType.WARNING);

    private DateTimeFormatter dateFormatter;

    public DatePicker datePicker;

    public TextField uhrzeitField;

    public TextField textField;

    public ListView nutzerListview;


    public ObservableList nutzer= FXCollections.observableArrayList();

    public void initialize() throws IOException{
        aktualisierenPressed();
    }


    public void startseitePressed() throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startseiteButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void zurueckPressed() throws IOException{
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/filmeinladung3.1.1.fxml"));
        Stage stage = (Stage) zurueckButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void verschickenPressed() throws IOException,InterruptedException, JSONException {

        if(filmtitelField.getText()==""){
            popUp.setAlertType(Alert.AlertType.ERROR);
            popUp.setHeaderText("Fehler!");
            popUp.setContentText("Bitte einen Filmtitel eintragen");
            popUp.showAndWait();


        }else{
            if(datePicker.getValue() == null){

                popUp.setAlertType(Alert.AlertType.ERROR);
                popUp.setHeaderText("Fehler!");
                popUp.setContentText("Bitte ein gültiges Datum auswählen");
                popUp.showAndWait();


            }else {
                if(isTime(uhrzeitField.getText())==false){
                    popUp.setAlertType(Alert.AlertType.ERROR);
                    popUp.setHeaderText("Fehler!");
                    popUp.setContentText("Bitte eine gültige Uhrzeit eingeben");
                    popUp.showAndWait();
                }else {
                    if(nutzerListview.getSelectionModel().getSelectedItem()==null){
                        popUp.setAlertType(Alert.AlertType.ERROR);
                        popUp.setHeaderText("Fehler!");
                        popUp.setContentText("Bitte einen Nutzer in der Liste auswählen");
                        popUp.showAndWait();
                    }else {

                        if(!getEmailAddressesInString(nutzerListview.getSelectionModel().getSelectedItem().toString()).get(0).equals(AngemeldeterUser.getSpeicher().getEmail())){
                            //hier EInladung erstellen

                            JSONObject einladung= new JSONObject();
                            ClientServerHandler csh = new ClientServerHandler();

                            einladung.put("vonNutzerEmail", AngemeldeterUser.getSpeicher().getEmail());
                            einladung.put("anNutzerEmail", getEmailAddressesInString(nutzerListview.getSelectionModel().getSelectedItem().toString()).get(0));
                            einladung.put("datum", datePicker.getValue());
                            einladung.put("uhrzeit", uhrzeitField.getText());
                            einladung.put("nachricht", textField.getText());
                            einladung.put("angenommen", false);
                            einladung.put("film", filmtitelField.getText());

                            csh.sendPostRequest("/controller/filmeinladung/add", einladung);
                            csh.sendPostRequest("/api/sendFilmeinladung", einladung);

                            filmtitelField.clear();
                            uhrzeitField.clear();
                            textField.clear();
                            datePicker.getEditor().clear();
                            nutzerListview.getSelectionModel().clearSelection();

                            popUp.setAlertType(Alert.AlertType.INFORMATION);
                            popUp.setHeaderText("Erfolg!");
                            popUp.setContentText("Einladung verschickt");
                            popUp.showAndWait();

                        }else{
                            popUp.setAlertType(Alert.AlertType.ERROR);
                            popUp.setHeaderText("Fehler!");
                            popUp.setContentText("Bitte nicht sich selbst in der Liste auswählen");
                            popUp.showAndWait();
                        }




                    }



                }

            }
        }

    }

    public void aktualisierenPressed() throws IOException {
        ClientServerHandler csh = new ClientServerHandler();
        JSONArray alleNutzer=csh.sendGETRequest("/controller/nutzer");

        for(int i=0; i<alleNutzer.length();i++){
            JSONObject tmp= alleNutzer.getJSONObject(i);
            nutzer.add(tmp.get("vorname").toString()+" "+ tmp.get("nachname").toString()+" "+tmp.get("email").toString());
        }

        nutzerListview.getItems().addAll(nutzer);
    }

    public boolean isTime(String time){
        try{
            LocalTime.parse(time);
            return true;
        } catch (DateTimeParseException e){
            return false;
        }
    }

    //von https://handyopinion.com/utility-method-to-get-all-email-addresses-from-a-string-in-java/
    public static ArrayList<String> getEmailAddressesInString(String text) {
        ArrayList<String> emails = new ArrayList<>();

        Matcher matcher = Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}").matcher(text);
        while (matcher.find()) {
            emails.add(matcher.group());
        }
        return emails;
    }


}
