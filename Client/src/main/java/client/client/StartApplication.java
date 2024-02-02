package client.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class StartApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{


        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/Registrierung.fxml"));

        primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Image/film.png"))));
        primaryStage.setTitle("Movie-Chat");
        primaryStage.setScene(new Scene(root, 600, 500));

        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }

}