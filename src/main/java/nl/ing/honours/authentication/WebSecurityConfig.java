package nl.ing.honours.authentication;

import nl.ing.honours.session.SessionService;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity // same as @Configuration @EnableGlobalAuthentication
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SessionService sessionService;

    public WebSecurityConfig(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.addFilterAfter(new SessionIdFilter(this.sessionService), BasicAuthenticationFilter.class)
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/sessions").permitAll()
                .antMatchers("/**").authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(new CustomAuthenticationProvider());
    }
}
