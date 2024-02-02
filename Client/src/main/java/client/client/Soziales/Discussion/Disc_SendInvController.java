package client.client.Soziales.Discussion;

import Controller.ClientServerHandler;
import client.client.Soziales.Einladungen.Entities.EinladungUser;
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
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

public class Disc_SendInvController {

    public ListView<EinladungUser> ListView_einladung;

    public Button backButton, homepageButton, refreshButton, sendInviteButton;

    ClientServerHandler csh = new ClientServerHandler();

    String clientemail= AngemeldeterUser.getSpeicher().getEmail();

    ObservableList<EinladungUser> list = FXCollections.observableArrayList();


    public Disc_SendInvController()throws MalformedURLException {
    }

    public void initialize() throws IOException {

        //zeigt die Freunde des angemeldeten Users an
        ListView_einladung.getItems().clear();
        list.removeAll(list);

        JSONArray freundeslisteArray = csh.sendGETRequest("/controller/freundesliste");
        JSONArray nutzerArray = csh.sendGETRequest("/controller/nutzer");

        for (int i = 0; i < freundeslisteArray.length(); i++) {
            JSONObject tmp = freundeslisteArray.getJSONObject(i);

            if (tmp.get("emailNutzer").toString().equals(AngemeldeterUser.getSpeicher().getEmail())) {
                for (int k = 0; k < nutzerArray.length(); k++) {
                    JSONObject tmpNutzer = nutzerArray.getJSONObject(k);

                    if (tmpNutzer.get("email").toString().equals(tmp.get("emailFreund").toString())) {
                        EinladungUser nutzer=new EinladungUser();
                        nutzer.setVorname(tmpNutzer.getString("vorname"));
                        nutzer.setNachname(tmpNutzer.getString("nachname"));
                        nutzer.setEmail(tmpNutzer.getString("email"));
                        list.add(nutzer);
                        // list.add(tmpNutzer.get("vorname").toString() + " " + tmpNutzer.get("nachname").toString());
                    }
                }
            }
        }

        ListView_einladung.getItems().addAll(list);
    }

    // aktualisiert die freundesliste
    public void refreshPressed(ActionEvent actionEvent) throws IOException {
        initialize();
    }


    public void sendInvitePressed(ActionEvent actionEvent) throws IOException, InterruptedException {

        if (ListView_einladung.getSelectionModel().getSelectedItem() != null) {

            //laedt alle existenten Einladungen und Teilnehmer in Diskussionsgruppen
            JSONArray einladungen = csh.sendGETRequest("/controller/diskussionseinladung");
            JSONArray teilnehmer = csh.sendGETRequest("/controller/diskussion/teilnehmer");

            EinladungUser chosenUser=ListView_einladung.getSelectionModel().getSelectedItem();

            if (einladungen.length() > 0) {

                boolean einladungexists = false;
                for (int i = 0; i < einladungen.length(); i++) {
                    JSONObject einladung = einladungen.getJSONObject(i);

                    String emailEmpfang = einladung.get("recipient").toString();
                    Integer chosengroup=einladung.getInt("diskussionsID");

                   System.out.println(emailEmpfang);
                   System.out.println(chosenUser.getEmail());
                   System.out.println(chosengroup);
                   System.out.println(Zwischenspeicher.getSpeicher().getDiscId());

                    //Ueberprueft, ob es schon eine Einladung an den Einzuladenden gibt
                    //Fall: Client schickt erneut eine Einladung
                    if (emailEmpfang.equals(chosenUser.getEmail()) && chosengroup == Zwischenspeicher.getSpeicher().getDiscId()) {
                        einladungexists = true;
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Ups!");
                        alert.setContentText("Du hast diesem Nutzer bereits eine Einladung in die Diskussionsgruppe geschickt.\n" +
                                "Bitte wähle einen anderen Nutzer aus!");
                        alert.showAndWait();
                    }

                    //Ueberprueft, ob der Einzuladende bereits in der Diskussionsgruppe ist
                    //Fall: Client schickt erneut eine Einladung
                    for(int j=0; j<teilnehmer.length();j++){
                        JSONObject kontrolle = teilnehmer.getJSONObject(j);
                        String partic = kontrolle.get("participant").toString();
                        Integer ausgesuchteGruppe = kontrolle.getInt("groupID");
                        if(partic.equals(chosenUser.getEmail()) && ausgesuchteGruppe == Zwischenspeicher.getSpeicher().getDiscId()){
                            einladungexists = true;
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setHeaderText("Ups!");
                            alert.setContentText("Dieser Nutzer ist bereits in der Diskussionsgruppe.\n" +
                                    "Bitte wähle einen anderen Nutzer aus!");
                            alert.showAndWait();
                        }
                    }
                }

                //Es existiert keine Einladung und der Einzuladende ist nicht in der Ausgewählten Diskussionsgruppe
                if(!einladungexists) {

                    JSONObject neweinladung = new JSONObject();

                    neweinladung.put("diskussionsID", Zwischenspeicher.getSpeicher().getDiscId());
                    neweinladung.put("recipient", chosenUser.getEmail());
                    neweinladung.put("sender", AngemeldeterUser.getSpeicher().getEmail());
                    csh.sendPostRequest("/controller/diskussionseinladung/add", neweinladung);


                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Erfolg!");
                    alert.setContentText("Die Einladung wurde verschickt!");
                    alert.showAndWait();
                    ListView_einladung.getSelectionModel().clearSelection();

                }
            } else {

                //es existieren noch überhaupt keine Einladungen

                boolean istTeilnehmer = false;
                for (int j = 0; j < teilnehmer.length(); j++) {
                    JSONObject kontrolle = teilnehmer.getJSONObject(j);
                    String partic = kontrolle.get("participant").toString();
                    Integer ausgesuchteGruppe = kontrolle.getInt("groupID");

                    System.out.println(partic);
                    System.out.println(chosenUser.getEmail());
                    System.out.println(ausgesuchteGruppe);
                    System.out.println(Zwischenspeicher.getSpeicher().getDiscId());

                    //Ueberprueft, ob der Einzuladende bereits in der  ausgesuchten Diskussionsgruppe ist
                    if (partic.equals(chosenUser.getEmail()) && ausgesuchteGruppe == Zwischenspeicher.getSpeicher().getDiscId()) {
                        istTeilnehmer = true;
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Ups!");
                        alert.setContentText("Dieser Nutzer ist bereits in der Diskussionsgruppe.\n" +
                                "Bitte wähle einen anderen Nutzer aus!");
                        alert.showAndWait();
                    }
                }

                //Es existiert keine Einladung und der Einzuladende ist nicht in der Ausgewaehlten Diskussionsgruppe
                if(!istTeilnehmer){

                    JSONObject neweinladung = new JSONObject();
                    neweinladung.put("sender", clientemail);
                    neweinladung.put("recipient", ListView_einladung.getSelectionModel().getSelectedItem().getEmail());
                    neweinladung.put("diskussionsID", Zwischenspeicher.getSpeicher().getDiscId());
                    csh.sendPostRequest("/controller/diskussionseinladung/add", neweinladung);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Erfolg!");
                    alert.setContentText("Die Einladung wurde verschickt!");
                    alert.showAndWait();
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Ungültige Angabe!");
            alert.setContentText("Es wurde kein Nutzer ausgewählt, dem eine Einladung zugeschickt werden soll!");
            alert.showAndWait();
        }
    }
    public void homepagePressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) homepageButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void backPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Discussion_OpenView.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }
}
