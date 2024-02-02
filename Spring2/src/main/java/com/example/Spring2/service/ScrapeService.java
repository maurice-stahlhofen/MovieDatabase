package com.example.Spring2.service;

import com.example.Spring2.Controller.LogWriter;
import com.example.Spring2.entities.FilmScrape;
import com.example.Spring2.repository.ScrapeSet;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

//aus dem Arbeitspaket
@Service
public class ScrapeService implements ScrapeSet {

    WebClient client = new WebClient();
    HtmlPage imdb;

    public ScrapeService() {
    }

    @Override
    public Set<FilmScrape> getFilmByDatum(String anfang, String ende, String anzahl) throws IOException {
        return getFilmybyDatumAndKategorie(anfang, ende, "", anzahl);
    }

    @Override
    public Set<FilmScrape> getFilmByKategorie(String genre, String anzahl) throws IOException {
        return getFilmybyDatumAndKategorie("1980", "2022", genre, anzahl);
    }

    @Override
    public Set<FilmScrape> getFilmybyDatumAndKategorie(String anfang, String ende, String genre, String anzahl) throws IOException {
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        // Liste zum zwischenspeichern der importierten Filme
        Set<FilmScrape> filmList = new HashSet<>();

        int startYear = Integer.parseInt(anfang);
        int endYear = Integer.parseInt(ende);
        int numberofFilms = Integer.parseInt(anzahl);


        int years = (endYear - startYear) + 1; // Anzahl Jahre im Zeitraum
        int value = numberofFilms / years; // Filme pro Jahr im Zeitraum für gleichmäßige Verteilung
        int rest = numberofFilms % years;
        if (rest > 0) {
            value++;
        }
        if (rest == 0) {
            rest--;
        }
        int value2 = value;

        //Aufrufen der Film-Liste auf imdb jedes Jahres im angegebenen Zeitraum
        for (int i = startYear; i <= endYear; i++) {
            if (rest > 0) {
                rest--;
            } else if (rest == 0) {
                value--;
                value2--;
                rest--;
            }
            //Aufrufen der Film-Liste auf imdb für die Anzahl der Filme jedes Jahres
            for (int p = 1; p <= value; p = p + 50) {
                // Aufrufen der Internetseite eines Jahres, jede Seite ist auf 50 Einträge begrenzt
                String URL = "https://www.imdb.com/search/title/?title_type=feature&year="
                        + i + "-01-01," + i + "-12-31&genres=" + genre + "&start=" + p;
                imdb = client.getPage(URL);

                int count = 50; // Notwendig um die anzahl der Filme eines Jahres in jedem Jahr abzurufen
                if (value2 >= 50) {
                    value2 = value2 - 50;
                } else {
                    count = value2;
                }

                // Daten aus dem Interet importieren
                // Liste der Filme auf der Seite speichern
                HtmlDivision list = imdb.getFirstByXPath("//*[@id=\"main\"]/div/div[3]/div");
                List<DomNode> imdbList = list.getChildNodes();

                if (imdbList.isEmpty()) {
                    System.out.println("no entry found");
                } else {
                    // Anzahl der Filme des Jahres importieren und als Film-Objekt
                    // in der Liste zwischenspeichern
                    for (int j = 1; j <= 2 * count; j = j + 2) {

                        FilmScrape film = new FilmScrape();
                        DomNode filmItem = imdbList.get(j);

                        // Daten eines Filmes Importieren und als Film
                        // Objekt in der Film Liste zweischenspeichern
                        HtmlAnchor name = filmItem.getFirstByXPath("div[3]/h3/a");
                        if (name != null) {
                            film.setName(name.asNormalizedText());
                        } else {
                            film.setName("nicht gefunden");
                        }
///div[3]/h3/a
                        HtmlSpan erscheinungsdatum =
                                filmItem.getFirstByXPath("div[3]/h3/span[2]");
                        if (erscheinungsdatum != null) {

                          String alt =   erscheinungsdatum.asNormalizedText();
                          String neu = "";
                          for(int k = 0; k < alt.length(); k++){
                              String tmp = ""+alt.charAt(k);
                              if(tmp.matches("[0-9]")){
                                  neu=neu+tmp;
                              }
                          }

                            film.setErscheinungsdatum(neu);
                        } else {
                            film.setErscheinungsdatum("nicht gefunden");
                        }

                        HtmlSpan filmLänge = filmItem.getFirstByXPath("div[3]/p[1]/span[3]");
                        if (filmLänge != null) {
                            String alt =   filmLänge.asNormalizedText();
                            String neu = "";
                            for(int k = 0; k < alt.length(); k++){
                                String tmp = ""+alt.charAt(k);
                                if(tmp.matches("[0-9]")){
                                    neu=neu+tmp;
                                }
                            }
                            if (neu != "") {
                                film.setFilmlaenge(neu);
                            } else {
                                filmLänge = filmItem.getFirstByXPath("div[3]/p[1]/span[1]");
                                if (filmLänge != null) {
                                    alt =   filmLänge.asNormalizedText();
                                    neu = "";
                                    for(int k = 0; k < alt.length(); k++){
                                        String tmp = ""+alt.charAt(k);
                                        if(tmp.matches("[0-9]")){
                                            neu=neu+tmp;
                                        }
                                    }
                                    if (neu != "") {
                                        film.setFilmlaenge(neu);
                                    } else {
                                        film.setFilmlaenge("0");
                                    }
                                } else {
                                    film.setFilmlaenge("0");
                                }
                            }

                        } else {
                            film.setFilmlaenge("0");
                        }

                        HtmlSpan kategorie = filmItem.getFirstByXPath("div[3]/p[1]/span[5]");
                        if (kategorie != null) {
                            film.setKategorie(kategorie.asNormalizedText());
                        } else {
                            kategorie = filmItem.getFirstByXPath("div[3]/p[1]/span[3]");
                            if (kategorie != null) {
                                film.setKategorie(kategorie.asNormalizedText());
                            } else {
                                film.setKategorie("nicht gefunden");
                            }
                        }

                        HtmlAnchor director = filmItem.getFirstByXPath("div[3]/p[3]/a[1]");
                        if (director != null) {
                            film.setRegisseur(director.asNormalizedText());
                        } else {
                            film.setRegisseur("nicht gefunden");
                        }

                        HtmlAnchor cast = filmItem.getFirstByXPath("div[3]/p[3]/a[2]");
                        if (cast == null) {
                            film.setCast("nicht gefunden");
                        } else {
                            List<String> castList = new LinkedList<>();
                            int c = 3;
                            while (cast != null) {
                                castList.add(cast.asNormalizedText());
                                cast = filmItem.getFirstByXPath("div[3]/p[3]/a[" + c + "]");
                                c++;
                            }
                            film.setCast(castList.toString());
                        }

                        // hinzufügen des Film zur Film-Liste
                        filmList.add(film);
                    }
                }
                // Client schlies die Verbindung
                client.close();
            }
            value2 = value;
        }
        LogWriter writer = new LogWriter();
        writer.write((Set<FilmScrape>) filmList);
        // zurückgeben der Liste mit allen importierten Film-Objekten
        return filmList;
    }
}
