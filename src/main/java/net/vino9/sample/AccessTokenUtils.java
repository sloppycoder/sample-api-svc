package net.vino9.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Base64;

@Slf4j
class AccessTokenUtils {

    public static String getAccessToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof OAuth2Authentication) {
            return ((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue();
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
            log.info("Unable to decode token {} with exception {}", token, e);
        }
        return null;
    }

    // could use UserDetailService but it requires setup which may be too complex for simple services
    public static String getUser() {
        String token = getDecodedJwtToken();
        if (token != null) {
            return "none";
        }
        return null;
    }
}