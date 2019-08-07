package com.mlbd.avoya.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "devices")
@EqualsAndHashCode(callSuper = true)
public class Device extends BaseEntity {
  
  private static final long serialVersionUID = 4627084453980115069L;

  @Column(name = "account_id")
  private int accountId;

  @Column(name = "device_id")
  private String deviceId;
}
