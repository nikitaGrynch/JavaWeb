package step.learning.filters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.culture.ResourceProvider;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class CultureFilter  implements Filter {
    private FilterConfig filterConfig;

    private final ResourceProvider resourceProvider;

    @Inject
    public CultureFilter(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    )throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String path = req.getServletPath();
        Matcher matcher = Pattern.compile("^/(\\w\\w)/(.*)$").matcher(path);
        String culture = "uk";
        if(matcher.matches()){
            culture = matcher.group(1);
        }
        req.setAttribute("culture", culture);
        resourceProvider.setCulture(culture);

        filterChain.doFilter(servletRequest, servletResponse); // передача дальше
    }

    public void destroy() {
        this.filterConfig = null;
    }
}
