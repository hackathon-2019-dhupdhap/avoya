package com.mlbd.avoya.conf;


import com.mlbd.avoya.Enum.RoleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrentUser {
  
  private int id;
  
  private String username;

  private String email;

  private boolean enabled;
  
  private RoleType role;
}
