package com.yu.blog.repository;

import com.yu.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
//import org.springframework.data.jpa.repository.Query;

// 자동으로 Bean으로 등록이 되므로 @Repository 쓸 필요 없다.
public interface UserRepository extends JpaRepository<User, Integer> {
    // JPA Naming 쿼리 : SELECT * FROM user WHERE username= 1?;
    Optional<User> findByUsername(String username);
}
