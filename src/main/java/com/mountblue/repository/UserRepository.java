package com.mountblue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mountblue.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);

	//User findByEmail(String email);
}
