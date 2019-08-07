package com.mlbd.avoya.modules.devices;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mlbd.avoya.Repositories.DeviceRepository;
import com.mlbd.avoya.schemas.Device;
import com.mlbd.avoya.services.PushNotificationService;
import com.mlbd.avoya.services.models.NotificationDataDTO;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationService {
  
  private final DeviceRepository deviceRepository;
  private final PushNotificationService pushNotificationService;
  
  /**
   * Send notification to multiple user.
   *
   * @param accountIds the account ids
   * @param notification the notification
   */
  public void sendNotification(List<Integer> accountIds, NotificationDataDTO notification) {
    this.deleteExpiredDevices(accountIds);
    for (Integer accountId : accountIds) {
      pushNotificationService.sendNotificationToDevices(this.getAllDeviceIds(accountId), notification);
    }
  }
  
  /**
   * Gets the all device Ids of a user.
   *
   * @param accountId the account Id
   * @return the all device Ids
   */
  private List<String> getAllDeviceIds(Integer accountId){
    List<Device> devices = deviceRepository.findByAccountId(accountId);
    List<String> deviceIds = devices.stream()
                                    .map(Device::getDeviceId)
                                    .collect(Collectors.toList());
    return deviceIds;
  }
  
  /**
   * Delete expired devices.
   *
   * @param accountIds the account Ids
   */
  private void deleteExpiredDevices(List<Integer> accountIds) {
    LocalDateTime updatedAtExpiredTime = LocalDateTime.now().minusDays(15);
    deviceRepository.deleteByUpdatedAtLessThanAndAccountIdIn(updatedAtExpiredTime, accountIds);
  }
  
}
