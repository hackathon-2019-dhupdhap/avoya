package com.mlbd.avoya.schemas;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the users database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {


  private static final long serialVersionUID = 3623010331399837656L;

  @Column(name = "account_id")
  private int accountId;

  @Column(name = "current_tracker")
  private String currentTracker;

  private String name;
  
  private String nid;
  
  private String address;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private EmergencyContact emergencyContact;
  
  

}
