package com.prodyna.auth.kube.oidc.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableOAuth2Client
public class OidcConfig {
    @Value("${oidc.clientId}")
    private String clientId;

    @Value("${oidc.clientSecret}")
    private String clientSecret;

    @Value("${oidc.issuerUri}")
    private String issuerUri;

    @Value("${oidc.accessTokenUri}")
    private String accessTokenUri;

    @Value("${oidc.userAuthorizationUri}")
    private String userAuthorizationUri;

    @Value("${oidc.redirectUri}")
    private String redirectUri;

    @Value("${oidc.scope}")
    private String scope;

    @Bean
    public OAuth2ProtectedResourceDetails openIdConnect() {
        String[] split = scope.split(",");
        List<String> scopeList = Arrays.asList(split);
        scopeList.forEach(s -> s.trim());

        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setAccessTokenUri(accessTokenUri);
        details.setUserAuthorizationUri(userAuthorizationUri);
        details.setScope(scopeList);
        details.setPreEstablishedRedirectUri(redirectUri);
        details.setUseCurrentUri(false);
        return details;
    }

    @Bean
    public OAuth2RestTemplate openIdTemplate(final OAuth2ClientContext clientContext) {
        return new OAuth2RestTemplate(openIdConnect(), clientContext);
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getAccessTokenUri() {
        return accessTokenUri;
    }

    public String getUserAuthorizationUri() {
        return userAuthorizationUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getScope() {
        return scope;
    }

    public String getIssuerUri() {
        return issuerUri;
    }
}
