package com.accenture.fsacl.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RefreshScope
@RequestMapping("/")
@Slf4j
public class ApiController {

    @Value("${custom.my-name:none}")
    String myName;

    @Value("${custom.my-password:none}")
    String myPass;

    @RequestMapping("/whoami")
    public String decipherAccessToken() {
        return getDecodedToken();
    }

    @RequestMapping("/hello")
    public String sayHello() {
        return String.format("Hello. I'm %s with %s", myName, myPass);
    }

    private String getDecodedToken() {
        return AccessTokenUtils.getDecodedJwtToken();
    }
}

@Slf4j
class AccessTokenUtils {

    public static String getAccessToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof OAuth2Authentication) {
            return ((OAuth2AuthenticationDetails) ((OAuth2Authentication) auth).getDetails()).getTokenValue();
        } else {
            log.info("{} is not an OAuth2 token", auth);
            return null;
        }
    }

    public static String getDecodedJwtToken() {
        String token = null;
        try {
            token = getAccessToken();
            if (token != null && !token.isEmpty()) {
                return new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
            }
        } catch (Exception e) {
        }
        log.info("Unable to decode token {}", token);
        return null;
    }

    // could use UserDetailService but it requires setup which may be too complex for simple services
    public static String getUser() {
        String token = getDecodedJwtToken();
        if (token != null) {
            return "dummy123";
        }
        return null;
    }
}