package com.jacekg.homefinances.role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDAO extends JpaRepository<Role, Long> {
	
	Role findByName(String name);
}
