package com.mlbd.avoya.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComplainDTO {
  
  private int accountId;
  
  private double lat;
  
  private double lon;
  
  private String status;

}
