package Controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientServerHandler {

    URL server=new URL("http://localhost:8080");

    public ClientServerHandler() throws MalformedURLException {
    }

    public JSONArray sendGETRequest(String zugang) throws IOException {
        URL url=new URL(server+zugang);
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
        return new JSONArray(response.toString());
    }
/*
Aufbau angelehnt an:
https://www.twilio.com/blog/5-moglichkeiten-fur-http-anfragen-java
betrifft dort den Abschnitt Java 11 HttpClient

Aufbau der Requests(auch die weiter unten) unter Asynchronous Example:
https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html

 */
    public void sendPostRequest(String zugang, JSONObject json) throws IOException, InterruptedException {
        HttpRequest request= HttpRequest.newBuilder(URI.create(server+zugang))
                .header("Content-type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                        .build();

        HttpClient client= HttpClient.newHttpClient();
        HttpResponse<String> response=client.send(request, HttpResponse.BodyHandlers.ofString());
        //zur Überprüfung, ob die Requests auch richtig verwertet werden
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }

    public void sendPutRequest(String zugang, JSONObject input) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(server+zugang))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(input.toString()))
                .build();
        HttpResponse<String> response =(HttpClient.newHttpClient()).send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
        System.out.println(response.body());
    }

    public void sendDeleteRequest(String zugang) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(server+zugang))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response =(HttpClient.newHttpClient()).send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
        System.out.println(response.body());
    }




    public String sendGETRequestGetString(String zugang) throws IOException {
        URL url=new URL(server+zugang);
        HttpURLConnection connection=(HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        //zur Überprüfung, ob die Requests auch richtig verwertet werden:
        System.out.println("Initiated Request: " + url.toString());
        System.out.println("ResponseCode: " + connection.getResponseCode());

        BufferedReader input_from_connection= new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String input = input_from_connection.readLine();

        return input;
    }
}
