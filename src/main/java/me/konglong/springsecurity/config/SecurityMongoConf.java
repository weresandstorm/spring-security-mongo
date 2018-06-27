/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.config;

import me.konglong.springsecurity.service.SecurityContextService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.token.ClientKeyGenerator;
import org.springframework.security.oauth2.client.token.DefaultClientKeyGenerator;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;

@Configuration
@ComponentScan(basePackages = {"me.konglong.springsecurity"})
public class SecurityMongoConf {

  @Bean
  public SecurityContextService securityContextService() {
    return new SecurityContextService();
  }

  @Bean
  public AuthenticationKeyGenerator authenticationKeyGenerator() {
    return new DefaultAuthenticationKeyGenerator();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public ClientKeyGenerator clientKeyGenerator() {
    return new DefaultClientKeyGenerator();
  }
}
