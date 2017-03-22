package org.gobiiproject.gobiiweb.spring;

import org.gobiiproject.gobidomain.security.TokenManager;
import org.gobiiproject.gobidomain.security.impl.TokenManagerSingle;
import org.gobiiproject.gobidomain.services.AuthenticationService;
import org.gobiiproject.gobidomain.services.impl.AuthenticationServiceDefault;
import org.gobiiproject.gobidomain.services.impl.UserDetailsServiceImpl;
import org.gobiiproject.gobiiweb.security.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;
import sun.reflect.annotation.ExceptionProxy;

/**
 * Created by Phil on 3/22/2017.
 */

@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    @Bean(name = "restAuthenticationManager")
    public AuthenticationManager authenticationManagerBean() throws Exception {

        AuthenticationManager returnVal = super.authenticationManagerBean();
        return returnVal;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

        authenticationManagerBuilder.userDetailsService(this.userDetailService());
    }


    @Bean(name = "userDetailService")
    UserDetailsService userDetailService() {
        return new UserDetailsServiceImpl();
    }

    @Bean(name = "tokenManager")
    public TokenManager tokenManager() {
        return new TokenManagerSingle();
    }

    @Bean(name = "authenticationService")
    public AuthenticationService authenticationService() throws Exception {
        return new AuthenticationServiceDefault(this.authenticationManager(), this.tokenManager());
    }

    @Bean(name = "restAuthenticationFilter")
    public GenericFilterBean filterBean() throws Exception {
        return new TokenAuthenticationFilter(this.authenticationService(), "");
    }

}
