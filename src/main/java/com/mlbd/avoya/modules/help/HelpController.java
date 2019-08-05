package com.mlbd.avoya.modules.help;

import com.mlbd.models.AccountDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "v1/help", produces = MediaType.APPLICATION_JSON_VALUE)
public class HelpController {

  @PreAuthorize("hasAnyRole('POLICE')")
  @RequestMapping(value = "/view", method = RequestMethod.GET)
  public ResponseEntity<String> view() {
    log.info("Fetching details of user with id {}");
    System.out.println("hello");    
    return new ResponseEntity<String>("hello", HttpStatus.OK);
  }

}
