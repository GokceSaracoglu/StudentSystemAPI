package com.saracoglu.student.system.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saracoglu.student.system.security.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{

//	@Query(value = "from User where username = :username")
	Optional<User> findByUsername(String username);
}
