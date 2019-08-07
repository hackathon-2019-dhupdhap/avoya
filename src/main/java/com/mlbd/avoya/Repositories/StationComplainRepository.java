package com.mlbd.avoya.Repositories;

import com.mlbd.avoya.schemas.StationComplain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationComplainRepository extends JpaRepository <StationComplain, Integer> {

}
