package de.security.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import de.security.demo.model.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
	
	UserAccount findByUsername(String username);
	
}
