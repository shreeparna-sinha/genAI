package com.genAi.springsecurityjwt.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.genAi.springsecurityjwt.model.User;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    User findByUserName(String username);
    User findByEmail(String email);
    User findByPhoneNo(String phoneNo);
}
