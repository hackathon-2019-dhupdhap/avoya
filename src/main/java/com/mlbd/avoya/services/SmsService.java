package com.mlbd.avoya.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SmsService {

  // Find your Account Sid and Auth Token at twilio.com/console
  public static final String ACCOUNT_SID =
      "AC889b54468dd47ba6bec860e364aaaf7c";
  public static final String AUTH_TOKEN =
      "66a0d9c16f0a95edebe8ebd7f96ce3fa";

  public boolean sendSms(String reveiver, String body) {
    return this.sendSms(Arrays.asList(reveiver), body);
  }

  public boolean sendSms(List<String> reveivers, String body) {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

    for (String sendTo : reveivers) {
      Message message = Message
          .creator(new PhoneNumber(sendTo), // to
              new PhoneNumber("+15629916632"), // from
              body)
          .create();
    }
    log.info("successfully sent sms");
    return true;
  }

}
