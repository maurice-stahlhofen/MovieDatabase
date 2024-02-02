package UnitTestZyklus3.UnitTest;

import Controller.ClientServerHandler;
import client.client.Soziales.Chat.MovieChatController;
import client.client.Soziales.Chat.entities.AusgewaehlterChat;
import client.client.Soziales.Chat.entities.ChatUser;
import client.client.login.AngemeldeterUser;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZweiterUnitTest {
    MovieChatController movieChatController;
    String msg1;
    String msg2;

    @Before
    public void setUp() throws MalformedURLException {
        ClientServerHandler csh=new ClientServerHandler();
        AusgewaehlterChat.getAusgewaehlterChat().setChatID(100);
        AngemeldeterUser.getSpeicher().setEmail("isabel.juergens@gmx.de");
        ChatUser test=new ChatUser();
        test.setChatID(100);
        test.setEmail("isabel.juergens@stud.uni-due.de");
        test.setNachname("Jürgens");
        test.setVorname("Isabel");
        AusgewaehlterChat.getAusgewaehlterChat().setParticipant(test);
    }

    @Test
    public void chatTest() throws IOException, InterruptedException {
        //Nachricht an DB senden
        ClientServerHandler csh=new ClientServerHandler();
        JSONObject msg=new JSONObject();
        msg.put("chatID", 100);
        msg.put("participantEmail","isabel.juergens@gmx.de");
        msg.put("content", "hello world!");

        csh.sendPostRequest("/controller/nachrichten/add", msg);
        //Controller starten
        movieChatController=new MovieChatController();

        msg1=movieChatController.testMethod(100);
        AngemeldeterUser.getSpeicher().setEmail("isabel.juergens@stud.uni-due.de");
        AngemeldeterUser.getSpeicher().setVorname("Isabel");
        AngemeldeterUser.getSpeicher().setNachname("Jürgens");
        msg2=movieChatController.testMethod(100);
    }

    @After
    public void tearDown(){
        assertEquals(msg1, msg2);
    }
}
