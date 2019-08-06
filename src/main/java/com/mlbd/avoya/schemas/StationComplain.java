package com.mlbd.avoya.schemas;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "station_complain")
public class StationComplain extends BaseEntity{

  private static final long serialVersionUID = 2863357799518453385L;
  @ManyToOne
  private Station station;
  
  @ManyToOne
  private Complain complain;

}
