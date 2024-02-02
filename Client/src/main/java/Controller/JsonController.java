package Controller;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.net.MalformedURLException;

public class JsonController {

    ClientServerHandler handler=new ClientServerHandler();

    public JsonController() throws MalformedURLException {}

    public JSONObject ueberpruefeLogin(String email) throws IOException {
        JSONObject rueckgabeJson=new JSONObject();
        JSONArray admin=new JSONArray();
        JSONArray nutzer=new JSONArray();

        try {
            admin = handler.sendGETRequest("/controller/admin");
            nutzer = handler.sendGETRequest("/controller/nutzer");
        }catch(Exception e){
            e.printStackTrace();
        }

       for (int i = 0; i < admin.length(); i++) {
            JSONObject tmp = admin.getJSONObject(i);
            if (tmp.get("email").toString().equalsIgnoreCase(email)) {
                rueckgabeJson = tmp;
            }
        }
        if(rueckgabeJson.toString().length()==0){
            return rueckgabeJson;
        }else {

            for (int k = 0; k < nutzer.length(); k++) {
                JSONObject tmp = nutzer.getJSONObject(k);
                if (tmp.get("email").toString().equalsIgnoreCase(email)) {
                    rueckgabeJson = tmp;
                }
            }

            return rueckgabeJson;
        }
   }

    //überprüft ob ein Film mit dem übergebenen String (hier name) in der datenbank vorhanden ist, wenn ja gibt
    //die methode dies zurück sonst nur ein leeres jsonobject
    //methode aktuell noch mit name anstatt id zu testzwecken
    public JSONObject ueberpruefeFilmId(int filmId) throws IOException {

        JSONObject idCheck = new JSONObject();
        JSONArray filme = new JSONArray();

        try {
            //funktioniert das richtig?
            filme = handler.sendGETRequest("/controller/film");
        }catch(Exception e){
            e.printStackTrace();
        }

        for (int i = 0; i < filme.length(); i++) {
            JSONObject helper = filme.getJSONObject(i);
            if (helper.getInt("filmId")==(filmId)) {
                idCheck = helper;
            }
        }
        return idCheck;
    }
}
