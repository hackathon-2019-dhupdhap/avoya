package com.mlbd.avoya.Repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.mlbd.avoya.schemas.Station;

@Repository
public interface StationRepository extends JpaRepository<Station, Integer> {

  @Query(
      value = "select station FROM Station station where MBRContains(GeomFromText(Concat('LineString(',:northEastPointX,' ',:northEastPointY,', ',:southWestPointX,' ',:southWestPointY,')')), location) = true and abs(:latitude) <= 90.0 and abs(:longitude) <= 180.0 order by GLength(GeomFromText(Concat('LineString(',:latitude,' ',:longitude,', ',X(location),' ',Y(location),')'))) asc",
      countQuery = "select count(station) FROM Station station where MBRContains(GeomFromText(Concat('LineString(',:northEastPointX,' ',:northEastPointY,', ',:southWestPointX,' ',:southWestPointY,')')), location) = true and abs(:latitude) <= 90.0 and abs(:longitude) <= 180.0")
  List<Station> search(@Param("latitude") double latitude, @Param("longitude") double longitude,
      @Param("northEastPointX") double northEastPointX,
      @Param("northEastPointY") double northEastPointY,
      @Param("southWestPointX") double southWestPointX,
      @Param("southWestPointY") double southWestPointY);
}
