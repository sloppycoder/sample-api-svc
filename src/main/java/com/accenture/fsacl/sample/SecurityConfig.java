package com.accenture.fsacl.sample;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfig extends ResourceServerConfigurerAdapter {

    @Value("${api.resource.audience:https://fsacl.acn.dev/api}")
    String audience;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        log.info("==> api.resource.audience is {}", audience);
        resources.resourceId(audience);
    }

    // default configuration allows anonymous access to actuator endpoints
    // which may not be a good idea
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/actuator/**", "/hello").permitAll()
                .antMatchers("/**").authenticated()
        ;
    }

    protected static class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
        // custom code check claims in the token can go here
    }
}
