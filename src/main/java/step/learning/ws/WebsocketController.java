package step.learning.ws;

import com.google.gson.*;
import com.google.inject.Inject;
import step.learning.dao.AuthTokenDao;
import step.learning.dao.ChatDao;
import step.learning.dto.entities.AuthToken;
import step.learning.dto.entities.ChatMessage;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@ServerEndpoint(
        value = "/chat",     // address: ws://localhost.../chat
        configurator = WebsocketConfigurator.class
)
public class WebsocketController {
    private static final Set<Session> sessions =
            Collections.synchronizedSet(new HashSet<>());
    private final AuthTokenDao authTokenDao;
    private final ChatDao chatDao;

    private final static Gson gson = new GsonBuilder().serializeNulls().create();

    @Inject
    public WebsocketController(AuthTokenDao authTokenDao, ChatDao chatDao) {
        this.authTokenDao = authTokenDao;
        this.chatDao = chatDao;
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig sec){
        chatDao.install();
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
            case "auth": {
                AuthToken token = authTokenDao.getTokenByBearer(data);
                if (token == null) {
                    sendToSession(session, 403, "Token rejected");
                    return;
                }
                session.getUserProperties().put("token", token);
                sendToSession(session, 202, token.getNik());
                //broadcast(token.getNik() + " joined");
                break;
            }
            case "chat": {
                AuthToken token = (AuthToken) session.getUserProperties().get("token");
                token = authTokenDao.renewToken(token);
                session.getUserProperties().put("token", token);
                String jsonToken = gson.toJson(token);
                String encodedToken = Base64.getUrlEncoder().encodeToString(jsonToken.getBytes());
                sendToSession(session, 203, encodedToken);
                ChatMessage chatMessage = new ChatMessage(token.getSub(), data);
                chatDao.add(chatMessage);
                broadcast(token.getNik() + ": " + data);
                break;
            }
            case "join": {
                AuthToken token = (AuthToken) session.getUserProperties().get("token");
                if (token == null) {
                    sendToSession(session, 403, "Token rejected");
                    return;
                }
                broadcast(token.getNik() + " joined");
                break;
            }
            case "load": { // load 10 last messages from db
                AuthToken token = (AuthToken) session.getUserProperties().get("token");
                if(token != null){
                    JsonObject response = new JsonObject();
                    response.addProperty("status", 200);
                    JsonArray array = new JsonArray();
                    for(ChatMessage chatMessage : chatDao.getLastMessages()){
                        array.add(chatMessage.toJsonObject());
                    }
                    response.add("data", array);
                    sendToSession(session, response);
                }
                else {
                    sendToSession(session, 403, "Token rejected");
                }
                break;
            }
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
        sendToSession(session, response);
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
