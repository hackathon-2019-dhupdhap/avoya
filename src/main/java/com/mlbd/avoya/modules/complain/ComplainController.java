package com.mlbd.avoya.modules.complain;

import com.mlbd.avoya.Models.ComplainDTO;
import com.mlbd.avoya.Repositories.ComplainRepository;
import com.mlbd.avoya.Repositories.EmergencyContactRepository;
import com.mlbd.avoya.Repositories.StationComplainRepository;
import com.mlbd.avoya.Repositories.StationRepository;
import com.mlbd.avoya.Repositories.UserRepository;
import com.mlbd.avoya.modules.stations.StationService;
import com.mlbd.avoya.schemas.Complain;
import com.mlbd.avoya.schemas.Station;
import com.mlbd.avoya.schemas.StationComplain;
import com.mlbd.avoya.schemas.User;
import com.mlbd.avoya.services.SmsService;
import com.mlbd.repositories.AccountRepository;
import com.mlbd.repositories.RoleRepository;
import com.mlbd.repositories.RoleUserRepository;
import com.mlbd.schemas.Account;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "v1/complain", produces = MediaType.APPLICATION_JSON_VALUE)
public class ComplainController {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final AccountRepository accountRepository;
  private final RoleUserRepository roleUserRepository;
  private final EmergencyContactRepository emergencyContactRepository;
  private final StationRepository stationRepository;
  private final StationService stationService;
  private final StationComplainRepository stationComplainRepository;
  private final ComplainRepository complainRepository;
  private final SmsService smsService;
  
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<?> create(@RequestBody final ComplainDTO complainDTO) {

    User user = userRepository.findByAccountId(complainDTO.getAccountId());
    Account account = accountRepository.getOne(complainDTO.getAccountId());
    if(user == null) {
      log.info("Invalid account id");
      return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
    }

    //List<Station> stations = new ArrayList<>();
    System.out.println("entering");
    List<Station> stations = stationService.search(complainDTO.getLat(), complainDTO.getLon(), 100);
    System.out.println("done");
    System.out.println(stations);
    
    Station station1 = stationRepository.getOne(1);
    stations.add(station1);
    
    this.sms(user, account, stations);

    GeometryFactory geometryFactory = new GeometryFactory();
    Point point = geometryFactory.createPoint(new Coordinate(complainDTO.getLon(), complainDTO.getLat()));

    Complain complain = Complain.builder().
        status(0).location(point).accountId(user.getAccountId()).build();

    complainRepository.save(complain);

    for(Station station: stations) {
      StationComplain stationComplain = StationComplain.builder().complain(complain).station(station).build();
      stationComplainRepository.save(stationComplain);
    }
    
    return new ResponseEntity<>( HttpStatus.OK);
  }

  @PreAuthorize("hasAnyRole('POLICE')")
  @RequestMapping(value = "/view", method = RequestMethod.GET)
  public ResponseEntity<String> view() {
    log.info("Fetching details of user with id {}");
    System.out.println("hello");    
    return new ResponseEntity<String>("hello", HttpStatus.OK);
  }
  
  private void sms(User user, Account account, List<Station> stations) {
    String deeplink = "";
    String body = "Need emergency help, name: " + user.getName() + " contact: " + account.getEmail();
    if(user.getCurrentTracker()!= null) 
      body = body + " tracker: "+user.getCurrentTracker();
    body = body + " link: "+ deeplink;
    List<String> reveivers = new ArrayList<>();
    if(user.getEmergencyContact()!= null) {
      if (!StringUtils.isEmpty(user.getEmergencyContact().getContact_1()))
        reveivers.add(user.getEmergencyContact().getContact_1());
      if (!StringUtils.isEmpty(user.getEmergencyContact().getContact_2()))
        reveivers.add(user.getEmergencyContact().getContact_2());
      if (!StringUtils.isEmpty(user.getEmergencyContact().getContact_3()))
        reveivers.add(user.getEmergencyContact().getContact_3());
    }
    for(Station station: stations) {
      if(station != null && station.getContact()!= null && !station.getContact().isEmpty()) {
        reveivers.add(station.getContact());
      }
    }
    System.out.println(reveivers);
    smsService.sendSms(reveivers, body);
    log.info("Message sent");
  }

}
