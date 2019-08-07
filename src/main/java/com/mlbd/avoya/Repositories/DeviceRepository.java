package com.mlbd.avoya.Repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mlbd.avoya.schemas.Device;

public interface DeviceRepository extends JpaRepository<Device, Integer>{
  
  Device findByDeviceId(String deviceId);
  
  Device findByDeviceIdAndAccountId(String deviceId, int accountId);
  
  List<Device> findByAccountId(int accountId);
  
  void deleteByUpdatedAtLessThanAndAccountId(LocalDateTime updatedAt, int accountId);
  
  void deleteByUpdatedAtLessThanAndAccountIdIn(LocalDateTime updatedAt, List<Integer> accountIds);
}
