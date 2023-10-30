package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.AuthTokenDao;
import step.learning.dao.UserDao;
import step.learning.dto.entities.AuthToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Singleton
public class AuthServlet extends HttpServlet {
    private final static Gson gson = new GsonBuilder().serializeNulls().create();
    private final UserDao userDao;
    private final AuthTokenDao authTokenDao;


    @Inject
    public AuthServlet(UserDao userDao, AuthTokenDao authTokenDao) {
        this.userDao = userDao;
        this.authTokenDao = authTokenDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        if(login == null || password == null){
            sendResponse(resp, 400, "Login and password are required");
            return;
        }
        if(login.isEmpty() || password.isEmpty()){
            sendResponse(resp, 400, "Login and password cannot be empty");
            return;
        }

        AuthToken token = authTokenDao.getTokenByCredentials(login, password);
        if(token == null){
            sendResponse(resp, 401, "Credentials rejected");
            return;
        }
        String jsonToken = gson.toJson(token);
        String encodedToken = Base64.getUrlEncoder().encodeToString(jsonToken.getBytes());
        sendResponse(resp, 202, encodedToken);
    }

    protected void sendResponse(HttpServletResponse resp, int statusCode, Object body) throws ServletException, IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        gson.toJson(body, resp.getWriter());
    }

        @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(authTokenDao.install()){
            resp.getWriter().print("Created");
        }
        else{
            resp.getWriter().print("Error");

        }
    }
}
