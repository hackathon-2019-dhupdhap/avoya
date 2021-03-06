package com.mlbd.avoya.Repositories;

import com.mlbd.avoya.schemas.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  User findByAccountId(int accountId);

}
