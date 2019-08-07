package com.mlbd.avoya.modules.devices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.mlbd.avoya.Models.DeviceDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeviceController {
  
  private final DeviceService deviceService;
  
  @RequestMapping(value = "{id}/devices" ,method = RequestMethod.POST)
  public ResponseEntity<?> addDevice(@PathVariable("id") final int id, @RequestBody DeviceDTO deviceDTO) {
    deviceService.addDevice(id, deviceDTO);
    return new ResponseEntity<>(HttpStatus.OK);
  }
  
  @RequestMapping(method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteDevice(@PathVariable("id") final int id, @RequestBody DeviceDTO deviceDTO) {
    deviceService.deleteDevice(id, deviceDTO);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
