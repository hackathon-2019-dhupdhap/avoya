package com.mlbd.avoya.modules.stations;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import com.mlbd.avoya.Repositories.StationRepository;
import com.mlbd.avoya.schemas.Station;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StationService {

  public static final int EARTH_RADIUS = 6371000;
  
  private final StationRepository stationRepository;

  public List<Station> search(double latitude, double longitude, int radius) {
    
    org.springframework.data.geo.Point northEastPoint = this.transform(latitude, longitude, 45, radius);
    org.springframework.data.geo.Point southWestPoint = this.transform(latitude, longitude, 225, radius);
    
    List<Station> stations = stationRepository.search(latitude, longitude, northEastPoint.getX(), northEastPoint.getY(),
        southWestPoint.getX(), southWestPoint.getY());
    return stations;
  }
  
  /**
   * 
   * @param point
   * @param angle
   * @param distance
   * @return {@link Point} newly transformed point
   */
  public Point transform(double x, double y, double angle, double distance) {
    double dist = distance / (double) EARTH_RADIUS;
    double radianAngle = Math.toRadians(angle);
    double lat1 = Math.toRadians(x);
    double lon1 = Math.toRadians(y);

    double lat2 = Math.asin(
        Math.sin(lat1) * Math.cos(dist) + Math.cos(lat1) * Math.sin(dist) * Math.cos(radianAngle));
    double lon2 = lon1 + Math.atan2(Math.sin(radianAngle) * Math.sin(dist) * Math.cos(lat1),
        Math.cos(dist) - Math.sin(lat1) * Math.sin(lat2));

    lon2 = (lon2 + 3. * Math.PI) % (2. * Math.PI) - Math.PI;

    return new Point(Math.toDegrees(lat2), Math.toDegrees(lon2));
  }


}
