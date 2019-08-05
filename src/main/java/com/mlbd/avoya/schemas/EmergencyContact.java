package com.mlbd.avoya.schemas;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "emergency_contacts")
@EqualsAndHashCode(callSuper = true)
public class EmergencyContact extends BaseEntity {

  private String contact_1;

  private String contact_2;

  private String contact_3;

  @OneToOne
  private User user;
  
}
