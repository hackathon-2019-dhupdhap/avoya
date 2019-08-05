package com.mlbd.avoya.Models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
  
  private String contactNumber;
  
  private int id;
  
  private int accountId;
  
  private String currentTracker;

  private String name;

  private String nid;

  private String address;
  
  private EmergencyContactModel emergencyContactModel;
  
  private int created_by;
  
  private LocalDateTime created_at;
  
  private int updated_by;

  private LocalDateTime updated_at;

}
