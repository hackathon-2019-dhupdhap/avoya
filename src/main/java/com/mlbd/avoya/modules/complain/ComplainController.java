package com.mlbd.avoya.modules.complain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.mlbd.avoya.Models.ComplainDTO;
import com.mlbd.avoya.Repositories.ComplainRepository;
import com.mlbd.avoya.Repositories.EmergencyContactRepository;
import com.mlbd.avoya.Repositories.StationComplainRepository;
import com.mlbd.avoya.Repositories.StationRepository;
import com.mlbd.avoya.Repositories.UserRepository;
import com.mlbd.avoya.modules.devices.NotificationService;
import com.mlbd.avoya.modules.stations.StationService;
import com.mlbd.avoya.schemas.Complain;
import com.mlbd.avoya.schemas.Station;
import com.mlbd.avoya.schemas.StationComplain;
import com.mlbd.avoya.schemas.User;
import com.mlbd.avoya.services.SmsService;
import com.mlbd.avoya.services.models.NotificationDataDTO;
import com.mlbd.repositories.AccountRepository;
import com.mlbd.repositories.RoleRepository;
import com.mlbd.repositories.RoleUserRepository;
import com.mlbd.schemas.Account;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
  private final NotificationService notificationService;
  
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<?> create(@RequestBody final ComplainDTO complainDTO) {

    User user = userRepository.findByAccountId(complainDTO.getAccountId());
    Account account = accountRepository.getOne(complainDTO.getAccountId());
    if(user == null) {
      log.info("Invalid account id");
      return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
    }

    //List<Station> stations = new ArrayList<>();
    List<Station> stations = stationService.search(complainDTO.getLat(), complainDTO.getLon(), 2000);
    
    Station station1 = stationRepository.getOne(1);
    stations.add(station1);

    this.sms(user, account, stations, complainDTO);

    GeometryFactory geometryFactory = new GeometryFactory();
    Point point = geometryFactory.createPoint(new Coordinate(complainDTO.getLon(), complainDTO.getLat()));

    Complain complain = Complain.builder().
        status("ongoing").location(point).accountId(user.getAccountId()).build();

    complainRepository.save(complain);

    for(Station station: stations) {
      StationComplain stationComplain = StationComplain.builder().complain(complain).station(station).build();
      stationComplainRepository.save(stationComplain);
    }
    this.sendNotification(user, account, stations);
    return new ResponseEntity<>( HttpStatus.OK);
  }

  @PreAuthorize("hasAnyRole('POLICE')")
  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  public ResponseEntity<?> get(@PathVariable("id") final int id) {
    Complain complain = complainRepository.getOne(id);
    ComplainDTO complainDTO = ComplainDTO.builder().accountId(complain.getAccountId())
        .lat(complain.getLocation().getY()).lon(complain.getLocation().getY())
        .status(complain.getStatus()).build();
    return new ResponseEntity<ComplainDTO>(complainDTO, HttpStatus.OK);
  }
  
  private void sendNotification(User user, Account account, List<Station> stations) {
    NotificationDataDTO notification = NotificationDataDTO.builder().name(user.getName()).message("Help Request").build();
    List<Integer> accountIds = stations.stream().map(Station::getAccountId).collect(Collectors.toList());
    notificationService.sendNotification(accountIds, notification);
  }
  
  private void sms(User user, Account account, List<Station> stations, ComplainDTO complainDTO) {
    String deeplink = "http://www.google.com/maps/place/";
    deeplink+=complainDTO.getLat();
    deeplink+=",";
    deeplink+=complainDTO.getLon();
    
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
    log.info("Sending sms to : {}", reveivers);
    smsService.sendSms(reveivers, body);
    log.info("Message sent");
  }

  

}
