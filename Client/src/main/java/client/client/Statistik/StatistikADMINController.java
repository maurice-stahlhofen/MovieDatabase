package client.client.Statistik;

import Controller.ClientServerHandler;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.LinkedList;

import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;

public class StatistikADMINController implements Initializable {
    public Button backMainPage;
    public Button filmButton;
    public ImageView pdfButton;
    public TableView<StatistikModel> table;
    public TableColumn<StatistikModel, Integer> FilmID;
    public TableColumn<StatistikModel, String> Film;
    public TableColumn<StatistikModel, Double> durchBewertung;
    public TableColumn<StatistikModel, Integer> anzBewertung;
    public TableColumn<StatistikModel, Integer> anzGesehenFilm;
    public ImageView deleteStatistic;
    public ImageView deleteSelection;
    public ImageView refreshButton;

    private int anzahlBewertungen;

    private int summeBewertungsSterne;

    int counterRefresh = 0;

    public StatistikADMINController() throws MalformedURLException {
    }

    ClientServerHandler csh = new ClientServerHandler();

    public void backToStartpage(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/StartseiteAdminView.fxml"));
        Stage stage = (Stage) backMainPage.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    public void selectFilms(ActionEvent actionEvent) throws IOException {
        FXMLLoader laden = new FXMLLoader(getClass().getResource("/Statistik_Admin/SelektiereFilmeStatistikView.fxml"));
        Stage stage = (Stage) filmButton.getScene().getWindow();
        Scene scene = new Scene(laden.load());
        stage.setScene(scene);
    }

    // PDF erzeugen
    public void getPDF(MouseEvent mouseEvent) {

        Document document = new Document();
        try {
            LocalDate todaysDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String datum = todaysDate.format(formatter).toString();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(datum + "_Statistik.pdf"));
            document.open();

            Image image1 = Image.getInstance(getClass().getResource("/Image/film.png"));
            image1.setAbsolutePosition(500f, 775f);
            image1.scaleAbsolute(40, 40);
            document.add(image1);
            document.add(new Paragraph("Movie-Chat Statistik vom " + datum + ":"));
            //Wenn die Statistik leer ist
            if (statistikModels.size() == 0) {
                Paragraph leereTabelle = new Paragraph("Leere Tabelle - Keine Filme ausgewählt");
                leereTabelle.setSpacingBefore(50f);
                document.add(leereTabelle);
                document.close();
                writer.close();
                pdfGenerated();
                // Statistik ist nicht leer:
            } else {
                PdfPTable table = new PdfPTable(5); // 3 columns.
                table.setWidthPercentage(100); //weite der Tabelle 100%
                table.setSpacingBefore(30f); //Platz vor table
                table.setSpacingAfter(15f); //Platz nach table

                // Optische Einrichtung der Tabelle
                PdfPCell cell1 = new PdfPCell(new Paragraph("FilmID", FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.BLACK)));
                cell1.setBackgroundColor(BaseColor.ORANGE);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell cell2 = new PdfPCell(new Paragraph("Filmname", FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.BLACK)));
                cell2.setBackgroundColor(BaseColor.ORANGE);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell cell3 = new PdfPCell(new Paragraph("Durchschnittliche Bewertung", FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.BLACK)));
                cell3.setBackgroundColor(BaseColor.ORANGE);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell cell4 = new PdfPCell(new Paragraph("Anzahl Bewertungen", FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.BLACK)));
                cell4.setBackgroundColor(BaseColor.ORANGE);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell cell5 = new PdfPCell(new Paragraph("Anzahl Gesehen", FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.BLACK)));
                cell5.setBackgroundColor(BaseColor.ORANGE);
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);


                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);

                // https://stackoverflow.com/questions/47379778/how-to-write-a-tableview-in-a-pdf-with-java-with-itext
                for (int j = 0; j < statistikModels.size(); j++) {
                    table.addCell(String.valueOf(statistikModels.get(j).getFilmID()));
                    table.addCell(statistikModels.get(j).getFilmName());
                    table.addCell(String.valueOf(statistikModels.get(j).getDurchBewertung()));
                    table.addCell(String.valueOf(statistikModels.get(j).getAnzBewertung()));
                    table.addCell(String.valueOf(statistikModels.get(j).getAnzGesehenFilm()));
                }

                document.add(table);
                document.close();
                writer.close();
                pdfGenerated();
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void pdfGenerated() {
        try {
            // Quelle: https://www.youtube.com/watch?app=desktop&v=ZzwvQ6pa_tk
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Statistik_Admin/PopupPDFGenerated.fxml"));
            Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            System.out.println("Fenster kann nicht geladen werden");
        }
    }

    public void deleteTable(MouseEvent mouseEvent) throws IOException {
        StatistikZwischenspeicher.getSpeicher().setList(null);
        table.setItems(null);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (StatistikZwischenspeicher.getSpeicher().getList() != null) {

            List<Integer> tmp = StatistikZwischenspeicher.getSpeicher().getList();
            StringBuilder sb = new StringBuilder();
            tmp.forEach(sb::append);
            try {
                inserrtData();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            FilmID.setCellValueFactory(new PropertyValueFactory<>("filmID"));
            Film.setCellValueFactory(new PropertyValueFactory<>("filmName"));
            durchBewertung.setCellValueFactory(new PropertyValueFactory<>("durchBewertung"));
            anzBewertung.setCellValueFactory(new PropertyValueFactory<>("anzBewertung"));
            anzGesehenFilm.setCellValueFactory(new PropertyValueFactory<>("anzGesehenFilm"));

            table.setItems(statistikModels);


        } else {
            table.setItems(null);
        }

        Tooltip.install(deleteSelection, new Tooltip("Filmauswahl löschen"));
        Tooltip.install(deleteStatistic, new Tooltip("Statistik zurücksetzen"));
        Tooltip.install(refreshButton, new Tooltip("Seite aktualisieren"));
        Tooltip.install(pdfButton, new Tooltip("PDF erstellen"));

    }

    //Statistik mit Daten füllen
    public void inserrtData() throws IOException {
        //Auswahl abrufen
        List<Integer> tmp = StatistikZwischenspeicher.getSpeicher().getList();

        JSONArray filme = new JSONArray();
        try {
            filme = csh.sendGETRequest("/controller/film");
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (int i = 0; i < tmp.size(); i++) {
            int filmID = Integer.parseInt(tmp.get(i).toString());
            for (int k = 0; k < filme.length(); k++) {
                JSONObject oneFilm = filme.getJSONObject(k);
                // Filmid muss mit der aus der Auswahl übereinstimmen
                if (oneFilm.get("filmId").equals(filmID)) {
                    anzahlBewertungen = getAnzahlBewertungen(filmID);
                    if (summeBewertungsSterne == 0) {
                        //Um ArithmeticException zu vermeiden, wenn Sterne und Anzahl Bewertungen 0 sind
                        statistikModels.add(new StatistikModel(filmID, oneFilm.get("name").toString(), (double) summeBewertungsSterne, anzahlBewertungen, getAnzahlGesehenerFilme(filmID)));
                    } else {
                        //Berechnung der Statistik für eine Zeile
                        statistikModels.add(new StatistikModel(filmID, oneFilm.get("name").toString(), (double) summeBewertungsSterne / anzahlBewertungen, anzahlBewertungen, getAnzahlGesehenerFilme(filmID)));
                    }
                }

            }


        }

    }

    private ObservableList<StatistikModel> statistikModels = FXCollections.observableArrayList(
    );

    private ObservableList<StatistikModel> leereListe = FXCollections.observableArrayList(
    );

    public int getAnzahlBewertungen(int filmid) throws IOException {
        // Bewertungsarray zu einer FilmID wird geholt
        // Die Länge des Arrays zeigt die Anzahl der Bewertungen zu dem Film
        JSONArray filme = csh.sendGETRequest("/controller/bewertung/" + filmid);
        summeBewertungsSterne = getSummeVonSternen(filme);
        return anzahlBewertungen = filme.length();

    }

    public int getAnzahlGesehenerFilme(int filmid) throws IOException {
        JSONArray filme = new JSONArray();
        // Gesehenen Filme zu einer FilmID werden geholt
        // Die Länge des Arrays zeigt die Anzahl der gesehenen Filme
        filme = csh.sendGETRequest("/controller/geseheneFilme/" + filmid);
        return filme.length();
    }

    public int getSummeVonSternen(JSONArray jsonArray) {
        // jsonArray übergibt Bewertungen zu dem bestimmten Film
        int result = 0;
        for (int i = 0; i < jsonArray.length(); i++) {
            // Array wird durchgelaufen und aus jeder Bewertung werden die Sterne addiert
            JSONObject temp = jsonArray.getJSONObject(i);
            result += Integer.parseInt(temp.get("sterne").toString());
        }
        return result;
    }


    // Wechsel zum Popup, um Statistikdaten zu löschen
    public void deleteSelectedStatistic(MouseEvent mouseEvent) throws IOException {
        try {
            counterRefresh = 0;
            // Quelle: https://www.youtube.com/watch?app=desktop&v=ZzwvQ6pa_tk
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Statistik_Admin/PopupLoescheStatistik.fxml"));
            Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            System.out.println("Fenster kann nicht geladen werden");
        }
    }

    // Statistikseite aktualisieren
    public void refreshTable(MouseEvent event) {
        if (counterRefresh < 1) {
            statistikModels = leereListe;
            table.setItems(null);
            if (StatistikZwischenspeicher.getSpeicher().getList() != null) {

                // Filmauswahl
                List<Integer> tmp = StatistikZwischenspeicher.getSpeicher().getList();
                StringBuilder sb = new StringBuilder();
                tmp.forEach(sb::append);
                try {
                    inserrtData();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                FilmID.setCellValueFactory(new PropertyValueFactory<>("filmID"));
                Film.setCellValueFactory(new PropertyValueFactory<>("filmName"));
                durchBewertung.setCellValueFactory(new PropertyValueFactory<>("durchBewertung"));
                anzBewertung.setCellValueFactory(new PropertyValueFactory<>("anzBewertung"));
                anzGesehenFilm.setCellValueFactory(new PropertyValueFactory<>("anzGesehenFilm"));

                table.setItems(statistikModels);


            } else {
                table.setItems(null);
            }
            counterRefresh++;
        } else {

        }


    }
}
