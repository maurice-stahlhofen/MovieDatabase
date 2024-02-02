package client.client.Soziales.Discussion;

import Controller.ClientServerHandler;
import client.client.Soziales.Discussion.entities.ChosenDiscussion;
import client.client.Soziales.Discussion.entities.ExistingDiscussions;
import client.client.Zwischenspeicher;
import client.client.login.AngemeldeterUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Disc_OpenController {
    public Button openDiscussion_Button;
    public Button startview;
    public ListView<ExistingDiscussions> discussion_list;
    public Button back_Button;
    public Label Fehlermeldung;

    private ClientServerHandler csh=new ClientServerHandler();
    private String email= AngemeldeterUser.getSpeicher().getEmail();
    JSONArray myDiscussions=new JSONArray();
    private List<Integer> groupIDs=new ArrayList<>();

    Alert popUp = new Alert(Alert.AlertType.INFORMATION);
    JSONArray alleDiskussionen = new JSONArray();
    JSONArray alleNutzer = new JSONArray();
    JSONObject angemeldeterUser;

    public Disc_OpenController() throws IOException {
        //lädt alle Gruppen in denen der Client Teilnehmer ist
        JSONArray myGroups=csh.sendGETRequest("/controller/diskussion/teilnehmer/findByParticipant/"+email);
        for(int i=0; i<myGroups.length(); i++){
            JSONObject found=myGroups.getJSONObject(i);
            //speichert die jeweiligen GroupIDs ab
            groupIDs.add(Integer.parseInt(found.get("groupID").toString()));
        }
    }

    public void initialize() throws IOException {
        myDiscussions.clear();
        discussion_list.getItems().clear();
        JSONArray alldisc=csh.sendGETRequest("/controller/diskussion/gruppe");

        for(int i=0; i<alldisc.length(); i++){
            JSONObject diskussion=alldisc.getJSONObject(i);
            Integer groupID=Integer.parseInt(diskussion.get("id").toString());
            for(Integer id:groupIDs){
                //überprüft, ob die Gruppe mit den Gruppen übereinstimmt, in denen
                //man Teilnehmer ist
                if(groupID.equals(id)){
                    ExistingDiscussions neu=new ExistingDiscussions();
                    neu.setCreatorMail(diskussion.get("creatorMail").toString());
                    neu.setCreatorName(diskussion.get("creatorName").toString());
                    neu.setID(Integer.parseInt(diskussion.get("id").toString()));
                    neu.setName(diskussion.get("name").toString());
                    neu.setPrivate(Boolean.parseBoolean(diskussion.get("isprivate").toString()));
                    neu.initializeTeilnehmerAnzahl();

                    discussion_list.getItems().add(neu);
                }
            }
        }
        if(discussion_list.getItems().size()==0){
            Fehlermeldung.setText("Keine Diskussionsforen gefunden!");
        }else{
            Fehlermeldung.setText("");
        }
    }


    public void openDiscussion(ActionEvent actionEvent) throws IOException {
        if(discussion_list.getSelectionModel().getSelectedItem()!=null){
            ExistingDiscussions clickedDisc=discussion_list.getSelectionModel().getSelectedItem();

            ChosenDiscussion.getChosenDiscussion().setID(clickedDisc.getID());
            ChosenDiscussion.getChosenDiscussion().setName(clickedDisc.getName());

            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Erfolg!");
            alert.setContentText("Das Diskussionsforum wurde erfolgreich ausgewählt!");
            alert.showAndWait();

            FXMLLoader laden = new FXMLLoader(getClass().getResource("/Discussion_ChatView.fxml"));
            Stage stage = (Stage) openDiscussion_Button.getScene().getWindow();
            Scene scene = new Scene(laden.load());
            stage.setScene(scene);

        }else{
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Ungültige Angabe!");
            alert.setContentText("Es wurde kein Diskussioneforum ausgewählt!");
            alert.showAndWait();

        }

    }

    public void switchtostartview(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteNutzerView.fxml"));
        Stage stage = (Stage) startview.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void back_View(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Discussion_MenueView.fxml"));
        Stage stage = (Stage) back_Button.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void inviteFriendPressed(ActionEvent actionEvent) throws IOException{
        //füllt den JSON Array mit allen Nutzern
        alleNutzer = csh.sendGETRequest("/controller/nutzer");

        //Überprüft den angemeldeten Nutzer
        for(int i = 0; i<alleNutzer.length(); i++){
            JSONObject temp = alleNutzer.getJSONObject(i);
            if(temp.get("email").toString().equals(AngemeldeterUser.getSpeicher().getEmail())){
                this.angemeldeterUser=temp;
            }
        }
        alleDiskussionen = csh.sendGETRequest("/controller/diskussion/gruppe");

        if(discussion_list.getSelectionModel().getSelectedItem() == null){
            popUp.setAlertType(Alert.AlertType.ERROR);
            popUp.setHeaderText("Fehler!");
            popUp.setContentText("Bitte Diskussionsgruppe aus der Liste auswählen");
            popUp.showAndWait();
        }
        //check ob privat
        else if(discussion_list.getSelectionModel().getSelectedItem().isPrivate()==true){
            //check ob creator oder moderator
            if(discussion_list.getSelectionModel().getSelectedItem().getCreatorMail().equals(email)){

                Zwischenspeicher.getSpeicher().setDiscId(discussion_list.getSelectionModel().getSelectedItem().getID());

                //leite zu fxml mit friendlist weiter
                FXMLLoader laden = new FXMLLoader(getClass().getResource("/Discussion_SendInvView.fxml"));
                Stage stage = (Stage) back_Button.getScene().getWindow();
                Scene scene = new Scene(laden.load());
                stage.setScene(scene);
                //send invite to user auf dieser seite
            }
            else{
                popUp.setAlertType(Alert.AlertType.ERROR);
                popUp.setHeaderText("Fehler!");
                popUp.setContentText("Dir fehlen die Berechtigungen für diese Diskussionsgruppe");
                popUp.showAndWait();
            }
        }

        else{
            popUp.setAlertType(Alert.AlertType.ERROR);
            popUp.setHeaderText("Fehler!");
            popUp.setContentText("Dies ist eine öffentliche Diskussionsgruppe");
            popUp.showAndWait();
        }

    }

    public void setModeratorPressed(ActionEvent actionEvent) throws IOException {
        //check ob privat
        //check ob creator oder moderator
        //leite zu fxml mit TeilnehmerList
        //status auf dieser seite veränderbar
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Discussion_TeilnehmerView.fxml"));
        Stage stage = (Stage) back_Button.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }
}
