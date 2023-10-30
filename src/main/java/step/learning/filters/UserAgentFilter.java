package step.learning.filters;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class UserAgentFilter implements Filter {
    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String userAgent = req.getHeader("User-Agent");

        req.setAttribute("browser", userAgent);

        userAgent = userAgent.toLowerCase();
        String browserType = "unknown";
        if(userAgent.contains("mobile")){
            browserType = "mobile";
            if(userAgent.contains("android")){
                browserType += " (android)";
            }
            if(userAgent.contains("iphone")){
                browserType += " (ios)";
            }
        }
        else if(userAgent.contains("windows nt")){
            browserType = "desktop (win)";
        }
        else if(userAgent.contains("macintosh")){
            browserType = "desktop (mac os)";
        }
        else if(userAgent.contains("linux")){
            browserType = "desktop (linux)";
        }

        req.setAttribute("browserType", browserType);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
