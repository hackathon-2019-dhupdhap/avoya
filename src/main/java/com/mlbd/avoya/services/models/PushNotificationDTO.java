package com.mlbd.avoya.services.models;

import lombok.Data;

@Data
public class PushNotificationDTO {

	private String to;

	private String priority;
	
	private NotificationDataDTO data;
}
