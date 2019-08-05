package com.mlbd.avoya.Models;

import com.mlbd.avoya.Enum.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
  
  private String contactNumber;
  
  private String password;

  private String role;
  
  private String currentTracker;

  private String name;

  private String nid;

  private String address;

  private String contact_1;

  private String contact_2;

  private String contact_3;

}
