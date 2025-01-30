package com.saracoglu.student.system.security.repository;

import com.saracoglu.student.system.security.entity.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<SecurityUser, Long>{

//	@Query(value = "from User where username = :username")
	Optional<SecurityUser> findByUsername(String username);
}
