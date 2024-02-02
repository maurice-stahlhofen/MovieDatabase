package client.client.Soziales;

import Controller.ClientServerHandler;
import Controller.JsonController;
import client.client.Zwischenspeicher;
import client.client.login.AngemeldeterUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

public class NutzerFindenController {

    public Button startseiteButton , searchButton, profilButton;

    public TextField vornameField, nachnameField, emailField;

    private ClientServerHandler handler = new ClientServerHandler();

    private JsonController json = new JsonController();

    public ListView<String> nutzerListe;

    private JSONObject user = new JSONObject();

    Alert popUp = new Alert(Alert.AlertType.INFORMATION);

    Zwischenspeicher speicher = Zwischenspeicher.getSpeicher();

    JSONArray alleUser = new JSONArray();

    ObservableList list = FXCollections.observableArrayList();

    public NutzerFindenController() throws MalformedURLException {

    }

    public void searchPressed(ActionEvent event){

        list.removeAll(list);
        nutzerListe.getItems().clear();
        try {
            alleUser = handler.sendGETRequest("/controller/nutzer");
        }
        catch(Exception e){
            e.printStackTrace();
        }

        for(int i = 0; i < alleUser.length(); i++){
            JSONObject helper = alleUser.getJSONObject(i);

            if (helper.get("vorname").toString().equals(vornameField.getText())||
                    helper.get("nachname").toString().equals(nachnameField.getText())||
                    helper.get("email").toString().equals(emailField.getText())){

                list.add(helper.get("email").toString()+" "+ helper.get("vorname").toString() +" "+
                helper.get("nachname").toString());
            }
        }
        if(list.isEmpty()){
            nutzerListe.getItems().add("Keine User gefunden");
        }
        nutzerListe.getItems().addAll(list);
    }

    public void profilPressed(ActionEvent event) throws IOException {

        if(nutzerListe.getSelectionModel().getSelectedItem() == null){
            popUp.setAlertType(Alert.AlertType.ERROR);
            popUp.setHeaderText("Fehler!");
            popUp.setContentText("Bitte Nutzer in Liste auswählen");
            popUp.showAndWait();
        }
        else {
            if (!nutzerListe.getSelectionModel().getSelectedItem().equals("Keine User gefunden")) {
                String email = cropMail(nutzerListe.getSelectionModel().getSelectedItem());
                // System.out.println(email); Zum checken der gecropten E-Mail

                for (int i = 0; i < alleUser.length(); i++) {
                    JSONObject tmp = alleUser.getJSONObject(i);
                    if (tmp.get("email").toString().equalsIgnoreCase(email)) {
                        user = tmp;
                        //System.out.println(user.toString());
                    }
                }
                FremderUser.getSpeicher().setEmail(user.get("email").toString());
                FremderUser.getSpeicher().setVorname(user.get("vorname").toString());
                FremderUser.getSpeicher().setNachname(user.get("nachname").toString());
                FremderUser.getSpeicher().setSichtbarkeit(user.get("sichtbarkeitsstufen").toString());
                //System.out.println(FremderUser.getSpeicher().getSichtbarkeit().toString());

                FXMLLoader laden = new FXMLLoader(getClass().getResource("/AndersProfil.fxml"));
                Stage stage = (Stage) profilButton.getScene().getWindow();
                Scene scene = new Scene(laden.load());
                stage.setScene(scene);
            }

            else{
                popUp.setAlertType(Alert.AlertType.ERROR);
                popUp.setHeaderText("Fehler!");
                popUp.setContentText("Bitte Nutzer in Liste auswählen");
                popUp.showAndWait();
            }
        }
    }

    public void startseitePressed(ActionEvent event) throws IOException{
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startseiteButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }
    private String cropMail(String toCrop){
        String email="";
        int counter;

        for(int i=0; i<toCrop.length(); i++){
            if(toCrop.charAt(i) == 32){
                break;
            }
            email= email + toCrop.charAt(i);
        }
        return email;
    }

    public void listResult(MouseEvent mouseEvent) {
    }
}
