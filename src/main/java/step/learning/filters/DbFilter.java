package step.learning.filters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.db.DbProvider;

import javax.servlet.*;
import java.io.IOException;
import java.sql.Connection;

@Singleton
public class DbFilter implements Filter {
    private final DbProvider dbProvider;

    @Inject
    public DbFilter(DbProvider dbProvider) {
        this.dbProvider = dbProvider;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Connection connection;
        try{
            connection = dbProvider.getConnection();
        }
        catch (Exception ignored){ connection = null; }
        if(connection == null){
            servletRequest.getRequestDispatcher("/static.html")
                    .forward(servletRequest, servletResponse);
        }
        else{
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
    }
}
