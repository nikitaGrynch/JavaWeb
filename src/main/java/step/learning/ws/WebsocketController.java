package step.learning.ws;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import step.learning.dao.AuthTokenDao;
import step.learning.dto.entities.AuthToken;

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
        JsonObject request = JsonParser.parseString(message).getAsJsonObject();
        String command = request.get("command").getAsString();
        String data = request.get("data").getAsString();
        switch (command){
            case "auth":
                AuthToken token = authTokenDao.getTokenByBearer(data);
                if(token == null){
                    sendToSession(session, 403, "Token rejected");
                    return;
                }
                session.getUserProperties().put("nik", token.getNik());
                sendToSession(session, 202, token.getNik());
                break;
            case "chat":
                broadcast(session.getUserProperties().get("nik") + ": " + data);
                break;
            default:
                sendToSession(session, 405, "Command unrecognized");
        }
    }

    @OnError
    public void onError(Throwable ex, Session session){
        System.err.println("onError: " + ex.getMessage());
    }

    public static void sendToSession(Session session, int status, String message){
        JsonObject response = new JsonObject();
        response.addProperty("status", status);
        response.addProperty("data", message);
        try {
            session.getBasicRemote().sendText(response.toString());
        } catch (Exception ex) {
            System.err.println("sendToSession: " + ex.getMessage());
        }
    }

    public static void sendToSession(Session session, JsonObject jsonObject){
        try {
            session.getBasicRemote().sendText(jsonObject.toString());
        } catch (Exception ex) {
            System.err.println("sendToSession: " + ex.getMessage());
        }
    }

    public static void broadcast(String message) {
        JsonObject response = new JsonObject();
        response.addProperty("status", 201);
        response.addProperty("data", message);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date javaDate = formatter.parse("2023-10-25 13:00:00");
        //Date javaDate = formatter.parse("2023-10-31 21:00:00");
        Date javaDate = new Date();
        String formattedDate = formatter.format(javaDate);
        response.addProperty("date", formattedDate);
        sessions.forEach(session -> {
            sendToSession(session, response);
        });
    }
}
