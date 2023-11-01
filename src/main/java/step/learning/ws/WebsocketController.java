package step.learning.ws;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import step.learning.dao.AuthTokenDao;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(
        value = "/chat",     // address: ws://localhost.../chat
        configurator = WebsocketConfigurator.class
)
public class WebsocketController {
    private static final Set<Session> sessions =
            Collections.synchronizedSet(new HashSet<>());
    private final AuthTokenDao authTokenDao;

    @Inject
    public WebsocketController(AuthTokenDao authTokenDao) {
        this.authTokenDao = authTokenDao;
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig sec){
        String culture = (String) sec.getUserProperties().get("culture");
        if (culture == null){
            try {
                session.close();
            } catch (IOException ignored) { }
        }
        else{
            session.getUserProperties().put("culture", culture);
            sessions.add(session);
        }
    }

    @OnClose
    public void onClose(Session session){
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session){
        broadcast(message + session.getUserProperties().get("culture"));
    }

    @OnError
    public void onError(Throwable ex, Session session){
        System.err.println("onError: " + ex.getMessage());
    }

    public static void broadcast(String message) {
        sessions.forEach(session -> {
            try {
                JsonObject object = new JsonObject();
                object.addProperty("text", message);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Date javaDate = formatter.parse("2023-10-25 13:00:00");
                //Date javaDate = formatter.parse("2023-10-31 21:00:00");
                Date javaDate = new Date();
                String formattedDate = formatter.format(javaDate);
                object.addProperty("date", formattedDate);
                session.getBasicRemote().sendText(new Gson().toJson(object));
            } catch (Exception ex) {
                System.err.println("broadcast: " + ex.getMessage());
            }
        });
    }
}
