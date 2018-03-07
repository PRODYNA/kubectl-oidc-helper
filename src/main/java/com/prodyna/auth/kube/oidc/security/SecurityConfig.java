package com.prodyna.auth.kube.oidc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@Configuration
@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @Bean
    public OpenIdConnectFilter openIdConnectFilter() {
        OpenIdConnectFilter filter = new OpenIdConnectFilter("/login");
        filter.setRestTemplate(restTemplate);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(new OAuth2ClientContextFilter(),
                AbstractPreAuthenticatedProcessingFilter.class)
            .addFilterAfter(openIdConnectFilter(),
                    OAuth2ClientContextFilter.class)
            .httpBasic()
            .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated();
    }
}
