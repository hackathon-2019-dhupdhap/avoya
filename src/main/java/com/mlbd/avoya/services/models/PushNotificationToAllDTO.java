package com.mlbd.avoya.services.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PushNotificationToAllDTO {

	private List<String> registrationIds;

	private String priority;
	
	private NotificationDataDTO data;
}
