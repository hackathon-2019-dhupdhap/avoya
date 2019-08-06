package com.mlbd.avoya.schemas;

import com.vividsolutions.jts.geom.Point;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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
@Table(name = "complains")
@EqualsAndHashCode(callSuper = true)
public class Complain extends BaseEntity{

  private static final long serialVersionUID = 4238324308978342620L;
  private Point location;
  
  private int status;

  @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
  private List<StationComplain> stationComplainList;

}
