package com.mlbd.avoya.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactModel {

  private String contact_1;

  private String contact_2;

  private String contact_3;
  
}
