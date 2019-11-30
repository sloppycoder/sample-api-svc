package net.vino9.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
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

    private String getDecodedToken() {
        return AccessTokenUtils.getDecodedJwtToken();
    }

    @Scheduled(fixedRate = 3000)
    public void fixedRateSch() {
        log.info(String.format("my name is %s, my pass is %s", myName, myPass));
    }
}
