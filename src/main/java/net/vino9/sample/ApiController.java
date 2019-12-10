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

    //  this will be override by env var SPRING_DATASOURCE_PASSWORD
    @Value("${spring.datasource.password}")
    String dbPassword;

    @RequestMapping("/whoami")
    public String decipherAccessToken() {
        return getDecodedToken();
    }

    private String getDecodedToken() {
        return AccessTokenUtils.getDecodedJwtToken();
    }

    @Scheduled(fixedRate = 3000)
    public void fixedRateSch() {
        log.info("my name is {}, my pass is {}", myName, myPass);
        log.info(" database password is {}", dbPassword);

    }
}
