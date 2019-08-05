package com.mlbd.avoya.Repositories;

import com.mlbd.avoya.schemas.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  public User findByAccountId(int accountId);

}
