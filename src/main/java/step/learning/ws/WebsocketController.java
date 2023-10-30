package step.learning.ws;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(
        value = "/chat"     // address: ws://localhost.../chat
)
public class WebsocketController {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session){
        sessions.add(session);
    }
    @OnClose
    public void onClose(Session session){
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session){
        broadcast(message);
    }

    @OnError
    public void onError(Throwable ex, Session session){
        System.err.println("onError: " + ex.getMessage());
    }

    public static void broadcast(String message) {
        sessions.forEach(session -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception ex) {
                System.err.println("broadcast: " + ex.getMessage());
            }
        });
    }
}
