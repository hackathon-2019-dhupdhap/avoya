package com.mlbd.avoya.services.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDataDTO {
  
  private Integer id;
  
  private String name;
  
  private String message;
  
}
