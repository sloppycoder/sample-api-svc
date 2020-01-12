package net.vino9.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RefreshScope
@RequestMapping("/")
@Slf4j
public class ResourceApiController {

    private int iter = 0;

    // this value will be override from configmap when running inside container environment
    @Value("${custom.config:default}")
    String customConfigValue;

    @GetMapping("ping")
    public String ping(@RequestParam(required = false) Optional<Integer> delay) {
        String pong = "{ \"response\" : \"pong\" }";
        if (! delay.isPresent()) {
            return pong;
        }

        //delay n seconds then return, used to test shutdown behavior
        long d = Math.abs(delay.get()) * 1000L;
        log.info("ping {} ms", d);

        try {
            Thread.sleep(d);
        } catch (InterruptedException e) {
            log.warn("interrupted? is this even possible?");
        }

        return pong;
    }

    @Scheduled(fixedRate = 3000)
    public void fixedRateSch() {
        log.info("iter {}, custom.config = {}", iter, customConfigValue);
        iter++;
    }
}
