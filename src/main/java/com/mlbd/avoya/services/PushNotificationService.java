package com.mlbd.avoya.services;


import java.net.URI;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.mlbd.avoya.services.models.NotificationDataDTO;
import com.mlbd.avoya.services.models.PushNotificationDTO;
import com.mlbd.avoya.services.models.PushNotificationToAllDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PushNotificationService {
  
  public static final String PRIORITY = "normal";
  public static String FCM_API_ENDPOINT= "https://fcm.googleapis.com/fcm/send";
  public static String FCM_SERVER_KEY = "AAAAsKVYPLw:APA91bHkyC00kn5sLE3bkFJRWKSFBLDgE2Bag2Z_CRRneOjjOcXCPIjDr2n0gahyBtt5uBqHuJx2y9EhtMNbbn-vwJeZbAR97cotjPMZZO-AxVIUtwjrp8CwHolCy-MJgYA95iL3lFZZ";
  
  private final RestTemplate restTemplate;
  
  
  /**
   * Send notification to devices.
   *
   * @param deviceIds the device ids
   * @param notification the notification
   */
  public void sendNotificationToDevices(List<String> deviceIds, NotificationDataDTO notificationDataDTO) {
    if(!deviceIds.isEmpty()) {
      PushNotificationToAllDTO dto = PushNotificationToAllDTO.builder()
                                                             .data(notificationDataDTO)
                                                             .priority(PRIORITY)
                                                             .registrationIds(deviceIds)
                                                             .build();
      this.sendPushNotificationToAll(dto);
    }
  }

  /**
   * Send push notification.
   *
   * @param pushNotificationDTO the push notification DTO
   * @return the response entity
   * @throws JSONException the JSON exception
   */
  public ResponseEntity<String> sendPushNotification(PushNotificationDTO pushNotificationDTO) throws JSONException {

    JSONObject requestBody = this.getRequestBody(pushNotificationDTO);
    HttpHeaders headers = this.getHttpHeaders();
    ResponseEntity<String> fcmResponse =
        this.makeRestCall(URI.create(FCM_API_ENDPOINT), headers, requestBody.toString());

    return fcmResponse;

  }

  /**
   * Send push notification to all.
   *
   * @param pushNotificationToAllDTO the push notification to all DTO
   */
  public void sendPushNotificationToAll(PushNotificationToAllDTO pushNotificationToAllDTO) {
    ResponseEntity<String> fcmResponse = null;
    try {
      JSONObject requestBody = this.getRequestBodyForAll(pushNotificationToAllDTO);
      HttpHeaders headers = this.getHttpHeaders();
      fcmResponse = this.makeRestCall(URI.create(FCM_API_ENDPOINT), headers, requestBody.toString());
    } catch (JSONException e) {
      log.info("Error Message: " + e.getMessage());
    }
    log.info("FCM Response: " + fcmResponse.getBody());
  }

  /**
   * Gets the HTTP headers.
   *
   * @return the HTTP headers
   */
  private HttpHeaders getHttpHeaders() {

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    httpHeaders.set(HttpHeaders.AUTHORIZATION, "key=" + FCM_SERVER_KEY);
    return httpHeaders;
  }

  /**
   * Gets the request body of push notification.
   *
   * @param pushNotificationDTO the push notification DTO
   * @return the request body
   * @throws JSONException the JSON exception
   */
  private JSONObject getRequestBody(PushNotificationDTO pushNotificationDTO) throws JSONException {

    JSONObject body = new JSONObject();
    body.put("to", pushNotificationDTO.getTo());
    body.put("priority", pushNotificationDTO.getPriority());
    body.put("data", this.getNotificationDataBody(pushNotificationDTO.getData()));
    return body;
  }

  /**
   * Gets the request body of push notification for all.
   *
   * @param pushNotificationToAllDTO the push notification to all DTO
   * @return the request body for all
   * @throws JSONException the JSON exception
   */
  private JSONObject getRequestBodyForAll(PushNotificationToAllDTO pushNotificationToAllDTO)
      throws JSONException {

    JSONObject body = new JSONObject();
    body.put("registration_ids", pushNotificationToAllDTO.getRegistrationIds());
    body.put("priority", pushNotificationToAllDTO.getPriority());
    body.put("data", this.getNotificationDataBody(pushNotificationToAllDTO.getData()));
    return body;
  }

  /**
   * Gets the notification data body.
   *
   * @param dataDTO the data DTO
   * @return the notification data body
   * @throws JSONException the JSON exception
   */
  private Object getNotificationDataBody(NotificationDataDTO dataDTO) throws JSONException {
    JSONObject data = new JSONObject();
    data.put("id", dataDTO.getId());
    data.put("name", dataDTO.getName());
    data.put("message", dataDTO.getMessage());
    return data;
  }

  /**
   * Make rest call.
   *
   * @param uri the URI
   * @param headers the headers
   * @param requestBody the request body
   * @return the response entity
   */
  private ResponseEntity<String> makeRestCall(URI uri, HttpHeaders headers, String requestBody) {

    ResponseEntity<String> fcmResponse = null;

    try {
      HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
      fcmResponse = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
    } catch (HttpClientErrorException exception) {
      exception.printStackTrace();
    }

    return fcmResponse;

  }
}
