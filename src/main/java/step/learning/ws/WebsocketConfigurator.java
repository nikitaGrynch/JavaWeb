package step.learning.ws;

import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.lang.reflect.Field;

public class WebsocketConfigurator extends ServerEndpointConfig.Configurator {

    @Inject
    private static Injector injector;
    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return injector.getInstance(endpointClass);
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        super.modifyHandshake(sec, request, response);

        HttpServletRequest httpRequest = null;
        for(Field field: request.getClass().getDeclaredFields()) {
            if(HttpServletRequest.class.isAssignableFrom(field.getType())){
                field.setAccessible(true);
                try {
                    httpRequest = (HttpServletRequest) field.get(request);
                    break;
                } catch (IllegalAccessException ignored) { }
            }
        }
        if(httpRequest != null){
            String culture = (String) httpRequest.getAttribute("culture");
            sec.getUserProperties().put("culture", culture);
        }
    }
}
