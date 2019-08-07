package com.mlbd.avoya.modules.user;

import com.mlbd.avoya.Models.ComplainDTO;
import com.mlbd.avoya.Models.UserDTO;
import com.mlbd.avoya.Models.UserModel;
import com.mlbd.avoya.Repositories.EmergencyContactRepository;
import com.mlbd.avoya.Repositories.StationRepository;
import com.mlbd.avoya.Repositories.UserRepository;
import com.mlbd.avoya.conf.Auth;
import com.mlbd.avoya.schemas.Complain;
import com.mlbd.avoya.schemas.EmergencyContact;
import com.mlbd.avoya.schemas.Station;
import com.mlbd.avoya.schemas.StationComplain;
import com.mlbd.avoya.schemas.User;
import com.mlbd.repositories.AccountRepository;
import com.mlbd.repositories.RoleRepository;
import com.mlbd.repositories.RoleUserRepository;
import com.mlbd.schemas.Account;
import com.mlbd.schemas.Role;
import com.mlbd.schemas.RoleUser;
import com.mlbd.services.AccountService;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final AccountRepository accountRepository;
  private final RoleUserRepository roleUserRepository;
  private final EmergencyContactRepository emergencyContactRepository;
  private final Auth auth;
  private final StationRepository stationRepository;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Map<String, Integer>> create(@RequestBody final UserDTO userDTO) {

    log.info("Creating new user with request body - {}", userDTO);
    Account account = Account.builder().email(userDTO.getContactNumber())
        .password(new BCryptPasswordEncoder().encode(userDTO.getPassword()))
        .enabled(true)
        .isEmailVerified(true)
        .isProfileComplete(true)
        .build();
    account.setCreatedBy(0);
    account.setUpdatedBy(0);
    accountRepository.save(account);

    User user = User.builder()
        .accountId(account.getId())
        .address(userDTO.getAddress())
        .currentTracker(userDTO.getCurrentTracker())
        .name(userDTO.getName())
        .nid(userDTO.getNid())
        .build();
    user.setCreatedBy(0);
    user.setUpdatedBy(0);
    userRepository.save(user);
    
    Role role = roleRepository.findByName(userDTO.getRole());
    RoleUser roleUser = RoleUser.builder().role(role).build();
    roleUser.setAccount(account);
    role.setCreatedBy(0);
    role.setUpdatedBy(0);
    roleUserRepository.save(roleUser);    
    
    EmergencyContact emergencyContact = EmergencyContact.builder()
        .contact_1(userDTO.getContact_1())
        .contact_2(userDTO.getContact_2())
        .contact_3(userDTO.getContact_3())
        .user(user)
        .build();
    emergencyContact.setCreatedBy(0);
    emergencyContact.setUpdatedBy(0);
    emergencyContactRepository.save(emergencyContact);
        
    Map<String, Integer> output = new HashMap<>();
    output.put("User id", user.getId());
    return new ResponseEntity<Map<String, Integer>>(output, HttpStatus.CREATED);
  }

  @PreAuthorize("hasAnyRole('POLICE','CIVILIAN')")
  @RequestMapping(value = "/me", method = RequestMethod.GET)
  public ResponseEntity<?> me() {
    int account_id = auth.currentUser().getId();
    User user = userRepository.findByAccountId(account_id);
    if(user == null) {
      return new ResponseEntity<String>("Error occured", HttpStatus.OK);
    }
    Account account = accountRepository.getOne(account_id);
    UserModel userModel = UserModel.builder()
        .accountId(user.getAccountId())
        .address(user.getAddress())
        .contactNumber(account.getEmail())
        .currentTracker(user.getCurrentTracker())
        .name(user.getName())
        .nid(user.getNid())
        .build();

    if(user.getCurrentLocation() != null) {
      userModel.setLat(user.getCurrentLocation().getX());
      userModel.setLon(user.getCurrentLocation().getY());
    }

    return new ResponseEntity<UserModel>(userModel, HttpStatus.OK);
  }




  @RequestMapping(value = "{id}" ,method = RequestMethod.PATCH)
  public ResponseEntity<Map<String, Integer>> update(@PathVariable("id") final int id, @RequestBody final UserDTO userDTO) {
    log.info("Updating user with - {}", userDTO);
    
    User user = userRepository.findByAccountId(id);
    
    if((userDTO.getCurrentTracker() != null) && (!userDTO.getCurrentTracker().isEmpty())) {
      user.setCurrentTracker(userDTO.getCurrentTracker());
    }
    
    if((userDTO.getLatitude()!=null) && (userDTO.getLatitude()!=null)) {
      GeometryFactory geometryFactory = new GeometryFactory();
      Point point = geometryFactory.createPoint(new Coordinate(userDTO.getLongitude(), userDTO.getLatitude()));
      user.setCurrentLocation(point);
    }
    
    userRepository.save(user);

    Map<String, Integer> output = new HashMap<>();
    output.put("User id", user.getId());
    return new ResponseEntity<Map<String, Integer>>(output, HttpStatus.OK);
  }

  
  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  public ResponseEntity<?> get(@PathVariable("id") final int id) {
    log.info("Fetching details of user with id {}", id);
    User user = userRepository.findByAccountId(id);
    if(user == null) {
      log.info("Invalid user Id");
      return new ResponseEntity<String>("Invalid user Id", HttpStatus.OK);
    }
    Account account = accountRepository.getOne(id);
    UserModel userModel = UserModel.builder()
        .accountId(user.getAccountId())
        .address(user.getAddress())
        .contactNumber(account.getEmail())
        .currentTracker(user.getCurrentTracker())
        .name(user.getName())
        .nid(user.getNid())
        .build();
    
    if(user.getCurrentLocation() != null) {
      userModel.setLat(user.getCurrentLocation().getX());
      userModel.setLon(user.getCurrentLocation().getY());
    }
    
    return new ResponseEntity<UserModel>(userModel, HttpStatus.OK);
  }

  @RequestMapping(value = "/{id}/complain", method = RequestMethod.GET)
  public ResponseEntity<?> getByUserId(@PathVariable("id") final int id) {

    Station station = stationRepository.findByAccountId(id);
    if(station == null) {
      log.info("No station for user");
      return new ResponseEntity<>(HttpStatus.OK);
    }

    List<StationComplain> stationComplains = station.getStationComplainList();
    List<ComplainDTO> complains = new ArrayList<>();
    
    for(StationComplain stationComplain: stationComplains) {
      Complain complain = stationComplain.getComplain();
      ComplainDTO complainDTO = ComplainDTO.builder().accountId(complain.getAccountId())
          .lat(complain.getLocation().getY()).lon(complain.getLocation().getY())
          .status(complain.getStatus()).build();
      complains.add(complainDTO);
    }
    log.info("complains: {}", complains);
    return new ResponseEntity<List<ComplainDTO>>(complains, HttpStatus.OK);
  }

}
