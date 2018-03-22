package nl.ing.honours.authentication;

import nl.ing.honours.session.SessionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SessionIdFilter extends GenericFilterBean {
    private static final String AUTHENTICATION_HEADER = "X-Session-ID";

    private SessionService sessionService;

    public SessionIdFilter(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        try {
            Long id = new Long(httpServletRequest.getHeader(AUTHENTICATION_HEADER));
            SecurityContextHolder.getContext().setAuthentication(new SessionIdAuthentication((sessionService.findSessionById(id))));
        } catch (NumberFormatException nfe) {
            System.out.println("Session ID invalid.");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
