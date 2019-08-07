package com.mlbd.avoya.Repositories;

import com.mlbd.avoya.schemas.Complain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplainRepository extends JpaRepository<Complain, Integer> {

}
