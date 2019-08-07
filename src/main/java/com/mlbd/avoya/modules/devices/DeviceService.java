package com.mlbd.avoya.modules.devices;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mlbd.avoya.Models.DeviceDTO;
import com.mlbd.avoya.Repositories.DeviceRepository;
import com.mlbd.avoya.schemas.Device;
import com.mlbd.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceService {
 
  private final DeviceRepository deviceRepository;
  
  public void addDevice(int id, DeviceDTO deviceDTO) {
    Device device = deviceRepository.findByDeviceId(deviceDTO.getDeviceId());
    
    if(device != null) {
      device.setUpdatedAt(LocalDateTime.now());
      device.setAccountId(id);
    }
    else {
      device = Device.builder()
                        .accountId(id)
                        .deviceId(deviceDTO.getDeviceId())
                     .build();
    }
    deviceRepository.save(device);
  }

  public void deleteDevice(int id, DeviceDTO deviceDTO) {
    Device device = deviceRepository.findByDeviceIdAndAccountId(deviceDTO.getDeviceId(), id);
    if(device == null) {
      throw new NotFoundException("devices.not_found");
    }
    deviceRepository.delete(device);
  }
}