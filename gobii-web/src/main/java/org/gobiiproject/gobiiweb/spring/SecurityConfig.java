package org.gobiiproject.gobiiweb.spring;

import org.gobiiproject.gobidomain.security.TokenManager;
import org.gobiiproject.gobidomain.security.impl.TokenManagerSingle;
import org.gobiiproject.gobidomain.services.AuthenticationService;
import org.gobiiproject.gobidomain.services.impl.AuthenticationServiceDefault;
import org.gobiiproject.gobidomain.services.impl.UserDetailsServiceImpl;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.types.GobiiAuthenticationType;
import org.gobiiproject.gobiiweb.security.TokenAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.filter.GenericFilterBean;


/**
 * Created by Phil on 3/22/2017.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    @Bean(name = "restAuthenticationManager")
    public AuthenticationManager authenticationManagerBean() throws Exception {

        AuthenticationManager returnVal = super.authenticationManagerBean();
        return returnVal;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.debug(true);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

        ConfigSettings configSettings = new ConfigSettings();
        GobiiAuthenticationType gobiiAuthenticationType = configSettings.getGobiiAuthenticationType();
        if (gobiiAuthenticationType.equals(GobiiAuthenticationType.TEST)) {

            authenticationManagerBuilder.userDetailsService(this.userDetailService());

        } else {

            String dnPattern = configSettings.getLdapUserDnPattern();
            String managerUser = configSettings.getLdapBindUser();
            String managerPassword = configSettings.getLdapBindPassword();
            String url = configSettings.getLdapUrl();

            if (gobiiAuthenticationType.equals(GobiiAuthenticationType.LDAP_CONNECT_WITH_MANAGER)) {

                authenticationManagerBuilder
                        .ldapAuthentication()
                        .userSearchBase("dc=mmaxcrc,dc=com")
                        .userSearchFilter(dnPattern)
                        .contextSource()
                        .managerDn(managerUser)
                        .managerPassword(managerPassword)
                        .url(url);

            } else if (gobiiAuthenticationType.equals(GobiiAuthenticationType.LDAP)) {

                authenticationManagerBuilder
                        .ldapAuthentication()
                        .groupSearchBase("ou=People,dc=maxcrc,dc=com")
                        //.userSearchBase()
                        .userSearchFilter(dnPattern)
                        .contextSource()
                        .url(url);


            }
        }
    } // configure()


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

