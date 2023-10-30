package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.services.hash.HashService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class IocServlet extends HttpServlet {
    private final HashService hashService;

    @Inject
    public IocServlet(@Named("Digest-hash") HashService hashService) {
        this.hashService = hashService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("hash", hashService.hash("123"));
        req.setAttribute("page-body", "ioc.jsp");
        req.getRequestDispatcher("/WEB-INF/_layout.jsp")
                .forward(req, resp);
    }
}
