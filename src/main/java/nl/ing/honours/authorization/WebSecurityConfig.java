package nl.ing.honours.authorization;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity()
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SessionIdFilter sessionIdFilter;

    public WebSecurityConfig(SessionIdFilter sessionIdFilter) {
        this.sessionIdFilter = sessionIdFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .addFilterAfter(this.sessionIdFilter, BasicAuthenticationFilter.class);
    }
}
