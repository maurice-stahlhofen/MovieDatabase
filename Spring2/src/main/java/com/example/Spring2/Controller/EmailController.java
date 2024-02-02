package com.example.Spring2.Controller;

import com.example.Spring2.FehlermeldungPackage.Fehlermeldung;
import com.example.Spring2.FilmeinladungPackage.Filmeinladung;
import com.example.Spring2.TwoFA.TwoFAGenerator;
import com.example.Spring2.service.EmailService;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path ="/api")
public class EmailController {

    @Autowired
    EmailService emailService;

    // 2FA Code verschicken
    // Email und Vorname vom Client wird übergeben
    @GetMapping("/send2FAViaEmail/{email}/{name}")
    public String sendEmail(@PathVariable String email, @PathVariable String name) {
        TwoFAGenerator newCode = new TwoFAGenerator();

        // gibt Daten weiter zum Verschicken
        emailService.sendEmail(email, name, String.valueOf(newCode.getNumber()));
        // Code geht zurück zum Client
        return String.valueOf(newCode.getNumber());
    }


    @PostMapping("/sendAdminBenachrichtigug")
    public void sendAdminBenachrichtigug(@RequestBody Fehlermeldung fehlermeldung) throws IOException, ParseException {
        emailService.sendAdminBenachrichtigung(fehlermeldung);
    }

    @PostMapping("/sendFilmeinladung")
    public void sendFilmeinladung(@RequestBody Filmeinladung filmeinladung){
        emailService.sendFilmeinladung(filmeinladung);
    }

    @PostMapping("sendAngenommen")
    public void sendAngenommen(@RequestBody Filmeinladung filmeinladung){
        emailService.sendAngenommen(filmeinladung);
    }

}