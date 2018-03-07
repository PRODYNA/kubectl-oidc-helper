package com.prodyna.auth.kube.oidc;

import com.prodyna.auth.kube.oidc.security.OidcConfig;
import com.prodyna.auth.kube.oidc.security.OpenIdConnectUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class KubectlOicdController {

    @Autowired
    OidcConfig oidcConfig;

    @GetMapping("/")
    public String kubectlOidc(Model model) {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        OpenIdConnectUserDetails userDetails = null;
        if (authentication.getPrincipal() instanceof OpenIdConnectUserDetails) {
            userDetails = (OpenIdConnectUserDetails) authentication.getPrincipal();
        }


        if (userDetails != null) {
            OAuth2AccessToken token = userDetails.getToken();
            Map<String, Object> additionalInformation = token.getAdditionalInformation();
            model.addAttribute("idtoken", additionalInformation.get("id_token") != null ? additionalInformation.get("id_token") : "n/a");
            model.addAttribute("refreshtoken", token.getRefreshToken() != null ? token.getRefreshToken() : "n/a");
        } else {
            model.addAttribute("idtoken", "n/a");
            model.addAttribute("refreshtoken", "n/a");
        }


        model.addAttribute("clientid", oidcConfig.getClientId());
        model.addAttribute("clientsecret", oidcConfig.getClientSecret());
        model.addAttribute("issuerurl", oidcConfig.getIssuerUri());

        model.addAttribute("name", userDetails.getUsername() != null ? userDetails.getUsername() : "Unknown");

        return "kubectl_oidc_info";
    }

}
