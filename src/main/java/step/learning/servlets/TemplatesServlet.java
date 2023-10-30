package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.AuthTokenDao;
import step.learning.dto.entities.AuthToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Restricted information Access Controller
 */
@Singleton
public class TemplatesServlet  extends HttpServlet {
    private static final byte[] buffer = new byte[8192];
    private final Logger logger;
    private final AuthTokenDao authTokenDao;

    @Inject
    public TemplatesServlet(Logger logger, AuthTokenDao authTokenDao) {
        this.logger = logger;
        this.authTokenDao = authTokenDao;
    }

    private String checkAuthToken(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if(token == null) {
            return "Authorization header required";
        }
        if(!token.startsWith("Bearer ")){
            return "Bearer Authorization scheme only";
        }
        token = token.replace("Bearer ", "");
        AuthToken authToken = authTokenDao.getTokenByBearer(token);
        if( authToken == null){
            return "Token rejected";
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        Servlet for multi-routing responds to all requests tpl/{...}
        req.getServletPath() - permanent part (/uk/tpl)
        req.getPathInfo() - variable part (/{...})
        PathInfo for this Servlet - file (template) name which is a subject of the request.
         */
        String tokenCheckError = this.checkAuthToken(req);
        if (tokenCheckError != null){
            sendResponse(resp, 401, tokenCheckError);
            return;
        }
        String tplName = req.getPathInfo();
        if(tplName == null || tplName.isEmpty()){
            sendResponse(resp, 400, "Resource name is required");
            return;
        }
        String suspiciousSymbolsPattern = "^[\\\\/:*?\"<>|]+&";
        if(Pattern.matches(suspiciousSymbolsPattern, tplName)){
            sendResponse(resp, 400, "Suspicious resource name");
            return;
        }
        URL tplUrl =  this.getClass().getClassLoader().getResource("tpl" + tplName);
        if( tplUrl == null || !Files.isRegularFile(
                Paths.get(
                        URLDecoder.decode(
                                tplUrl.getFile(),
                                StandardCharsets.UTF_8.name()))) ) {
            sendResponse(resp, 404, "Resource not located");
            return;
        }
        try(InputStream tplStream = tplUrl.openStream()) {
            resp.setContentType(URLConnection.getFileNameMap().getContentTypeFor(tplName));
            int bytesRead;
            OutputStream respStream = resp.getOutputStream();

            while((bytesRead = tplStream.read(buffer)) > 0) {
                respStream.write(buffer, 0, bytesRead);
            }
            respStream.close();
            resp.setStatus(200);
        }
        catch (IOException ex){
            logger.log(Level.SEVERE, ex.getMessage() + " -- " + tplName);
            sendResponse(resp, 500, "Look at server logs");
        }
    }

    protected void sendResponse(HttpServletResponse resp, int statusCode, String message) throws ServletException, IOException {
        resp.setStatus(statusCode);
        resp.setContentType("text/plain");
        resp.getWriter().print(message);
    }
}
