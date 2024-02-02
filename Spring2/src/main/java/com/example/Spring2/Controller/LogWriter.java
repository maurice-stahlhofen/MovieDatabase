package com.example.Spring2.Controller;

import com.example.Spring2.entities.FilmScrape;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
//vllt doch eher ein Service?
public class LogWriter {

    public LogWriter() {}

    private Logger logger = Logger.getLogger("MeinLogger");
    FileHandler handler;

    public void write(Set<FilmScrape> list){
        try {
            logger.setUseParentHandlers(false);

            handler = new FileHandler("gesammelteFilme.log");
            logger.addHandler(handler);

            handler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    String log = "";
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(record.getMillis());
                    log = log + dateFormat.format(date) + "\n";
                    log = log + this.formatMessage(record) + "\n";
                    return log;
                }
            } );

            logger.info("Es wurden folgende Filme von IMDB gesammelt:\n");

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int q = 1;
        for (FilmScrape film : list) {
            logger.info("Film Nummer: " + q+ "\n" + film.toString() + "\n");
            q++;
        }

    }

}

//https://stackoverflow.com/questions/15758685/how-to-write-logs-in-text-file-when-using-java-util-logging-logger
