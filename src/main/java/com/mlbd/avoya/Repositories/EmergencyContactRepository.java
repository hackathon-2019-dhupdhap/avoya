package com.mlbd.avoya.Repositories;

import com.mlbd.avoya.schemas.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Integer> {

  public EmergencyContact findByUserId(int userId);

}
