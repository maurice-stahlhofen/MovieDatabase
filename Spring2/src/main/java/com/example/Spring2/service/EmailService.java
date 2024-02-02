package com.example.Spring2.service;

import com.example.Spring2.FehlermeldungPackage.Fehlermeldung;
import com.example.Spring2.FilmeinladungPackage.Filmeinladung;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;


@Service
public class EmailService {

    @Autowired
   private JavaMailSender javaMailSender;

    public void sendEmail(String email, String name, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("internet.film.datenbank@gmail.com");
        message.setTo(email);
        message.setSubject("2FA Movie-Chat");
        message.setText("Hallo "+name+",\n\ndein Code lautet: "+code+"\n\nViele Grüße\n\nDas Movie-Chat-Team");
        javaMailSender.send(message);
    }


    public void sendAdminBenachrichtigung(Fehlermeldung fehlermeldung) throws IOException, ParseException {
        // Formatieren der Nachricht der mail
        String nachricht = fehlermeldung.getNachricht();
        if(fehlermeldung.getNachricht() == null){ nachricht = "---"; }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("internet.film.datenbank@gmail.com");
        message.setSubject("Neue Fehlerbenachrichtigung");
        message.setText("Es ist eine neue Fehlerbenachrichtigung eingegangen.\n\n"
                + "Datum: " + fehlermeldung.getDate() + "\nFilm Name: "
                + fehlermeldung.getFilmName() + "\nNachricht: " + nachricht);

        // Versenden der Mail an alle Admins
        LinkedList<String> mailliste = new LinkedList<>();
        JSONArray adminlist = this.getAdmin();

        for (int i = 0; i < adminlist.size(); i++) {
            JSONObject tmp = (JSONObject) adminlist.get(i);
            mailliste.add(tmp.get("email").toString());
        }

        for (String mail : mailliste){
            //System.out.println(mail);
            message.setTo(mail);
            javaMailSender.send(message);
        }

    }

    public void sendFilmeinladung(Filmeinladung filmeinladung){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("internet.film.datenbank@gmail.com");
        message.setTo(filmeinladung.getAnNutzerEmail());
        message.setSubject("Neue Filmeinladung");
        message.setText("Der Nutzer "+ filmeinladung.getVonNutzerEmail()+" hat dich zum Film "+ filmeinladung.getFilm()+" am "+ filmeinladung.getDatum() + " um "+ filmeinladung.getUhrzeit()+ " Uhr eingeladen. \n\n" + filmeinladung.getNachricht());

        javaMailSender.send(message);
    }

    public void sendAngenommen(Filmeinladung filmeinladung){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("internet.film.datenbank@gmail.com");
        message.setTo(filmeinladung.getVonNutzerEmail());
        message.setSubject("Eine deiner Einladungen wurde angenommen");
        message.setText("Deine Einladung an "+filmeinladung.getAnNutzerEmail()+" zum Film"+ filmeinladung.getFilm()+" wurde angenommen");
        javaMailSender.send(message);
    }

    public JSONArray getAdmin() throws IOException, ParseException {
        // Holt die Daten der Admins aus der Datenbank
        //  Aufbau ähnlich wie im ClientServerHandler
        URL url=new URL("http://localhost:8080/controller/admin");
        HttpURLConnection connection=(HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        //zur Überprüfung, ob die Requests auch richtig verwertet werden:
        System.out.println("Initiated Request: " + url.toString());
        System.out.println("ResponseCode: " + connection.getResponseCode());

        BufferedReader input_from_connection= new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String input;
        //StringBuilder um die Methode append(String str) nutzern zu können
        StringBuilder response=new StringBuilder();

        //kopiert den gesamten Inhalt der jeweiligen Seite
        while((input=input_from_connection.readLine())!=null){
            response.append(input);
        }
        input_from_connection.close();
        //kopierte Seite wird weiter geschickt
        JSONParser par = new JSONParser();
        JSONArray json = (JSONArray) par.parse(response.toString());
        return json;
    }

}
