package filter;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import managedbean.AuthenticationManagedBean;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    @Inject
    private AuthenticationManagedBean authenticationManagedBean;

    private static final String APPLICATION_PATH = "/SuperForum-war";
    private static final String LOGIN_PATH = "/login.xhtml";
    private static final String REGISTER_PATH = "/register.xhtml";
    private static final String JAVAX_RESOURCE = "/javax.faces.resource";
    private static final String WEB_RESOURCE = "/webresources";

    public AuthenticationFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request1 = (HttpServletRequest) request;
        String path = request1.getRequestURI();
        System.out.println(request1.getServletPath());

        // TODO: clean up this code.. it's damn messy
        if (path.startsWith(APPLICATION_PATH + LOGIN_PATH) 
                || path.startsWith(APPLICATION_PATH + REGISTER_PATH)
                || path.startsWith(APPLICATION_PATH + JAVAX_RESOURCE)
                || path.startsWith(APPLICATION_PATH + WEB_RESOURCE)) {
            chain.doFilter(request, response);
        } else if (authenticationManagedBean == null || authenticationManagedBean.getUserId() == -1) {
            ((HttpServletResponse) response).sendRedirect(APPLICATION_PATH + LOGIN_PATH);
        } else {
            //authenticated - continue
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //do nothing
    }

    @Override
    public void destroy() {
        //do nothing
    }
}
