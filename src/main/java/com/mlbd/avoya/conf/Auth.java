package com.mlbd.avoya.conf;

import com.mlbd.avoya.Enum.RoleType;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.context.WebApplicationContext;


import com.mlbd.models.AccountResource;
import com.mlbd.services.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Auth {

  public final AccountService accountService;

  @Bean
  @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
  public CurrentUser currentUser() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    OAuth2AuthenticationDetails userDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
    String token = userDetails.getTokenValue();
    JsonParser parser = JsonParserFactory.getJsonParser();
    Map<String, ?> tokenData = parser.parseMap(JwtHelper.decode(token).getClaims());
    Integer id = (Integer) tokenData.get("id");

    AccountResource accountResource = accountService.getCurrentById(id);
    CurrentUser currentUser = CurrentUser.builder().id(accountResource.getId()).email(accountResource.getEmail())
        .username(accountResource.getUsername()).enabled(accountResource.isEnabled())
        .role(RoleType.findByValue(accountResource.getRoleType().get(0))).build();
    log.info("Fetched current user with id {}", id);

    return currentUser;
  }
}
