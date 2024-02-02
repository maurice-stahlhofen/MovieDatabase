module client.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.json;
    requires java.net.http;
    requires java.sql;
    requires java.desktop;
    requires itextpdf;


    opens client.client to javafx.fxml;
    opens client.client.registrierung to javafx.fxml;
    exports client.client;
    exports client.client.Anlegen;
    opens client.client.Anlegen to javafx.fxml;
    exports client.client.login;
    opens client.client.login to javafx.fxml;
    exports client.client.StartseitenController;
    opens client.client.StartseitenController to javafx.fxml;
    exports client.client.Nutzeruebersicht;
    opens client.client.Nutzeruebersicht to javafx.fxml;
    exports client.client.Soziales;
    opens client.client.Soziales to javafx.fxml;
    exports client.client.Soziales.Chat;
    opens client.client.Soziales.Chat to javafx.fxml;
    exports client.client.Soziales.Chat.entities;
    opens client.client.Soziales.Chat.entities to javafx.fxml;
    exports client.client.Statistik;
    opens client.client.Statistik to javafx.fxml;
    exports client.client.Soziales.Einladungen;
    opens client.client.Soziales.Einladungen to javafx.fxml;
    exports client.client.Soziales.Einladungen.Entities;
    opens client.client.Soziales.Einladungen.Entities to javafx.fxml;
    exports client.client.Fehlermeldung;
    opens client.client.Fehlermeldung to javafx.fxml;
    exports client.client.FilmVerwalten;
    opens client.client.FilmVerwalten to javafx.fxml;
    exports client.client.Anzeigen;
    opens client.client.Anzeigen to javafx.fxml;
    exports client.client.Vorschlaege;
    opens client.client.Vorschlaege to javafx.fxml;
    opens client.client.Soziales.Discussion to javafx.fxml;
    exports client.client.Soziales.Discussion;
    opens client.client.Soziales.Discussion.entities to javafx.fxml;
    exports client.client.Soziales.Discussion.entities;
    exports client.client.Filmeinladung;
    opens client.client.Filmeinladung to javafx.fxml;


}