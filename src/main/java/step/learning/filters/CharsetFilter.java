package step.learning.filters;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Singleton
public class CharsetFilter implements Filter {
    private FilterConfig filterConfig;
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    )throws IOException, ServletException {
        // прямой ход - к сервлетам
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String charset = StandardCharsets.UTF_8.name();
        req.setCharacterEncoding(charset);
        resp.setCharacterEncoding(charset);

        req.setAttribute("charset", charset);

        filterChain.doFilter(servletRequest, servletResponse); // передача дальше
        // после этого - обратный ход
    }

    public void destroy() {
        this.filterConfig = null;
    }
}
