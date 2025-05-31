package com.rh_systems.employee_service.repository;

import com.rh_systems.employee_service.Entity.StatusPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusPermissionRepository extends JpaRepository<StatusPermission, Long> {

}
