package com.mlbd.avoya.schemas;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.vividsolutions.jts.geom.Point;
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
@Table(name = "stations")
@EqualsAndHashCode(callSuper = true)
public class Station extends BaseEntity {
  
  private static final long serialVersionUID = -247226863543466790L;
  
  private String name;
  
  private Point location;
  
  private String contact;
}
