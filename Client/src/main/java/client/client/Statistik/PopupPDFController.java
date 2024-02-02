package client.client.Statistik;

import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PopupPDFController implements Initializable {
    public Text reportTxt;
    public Text pdfText;
    public Text pdfNameText;
    public Button okButton;

    public void back(ActionEvent actionEvent) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDate todaysDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String datum = todaysDate.format(formatter).toString();
       pdfNameText.setText("Dateiname: "+ datum + "_Statistik.pdf");
    }
}
