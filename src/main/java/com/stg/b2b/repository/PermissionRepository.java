package com.stg.b2b.repository;

import com.stg.b2b.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    @Query(value = "SELECT permission_for FROM permission WHERE role_id = :integerParam", nativeQuery = true)
    public List<String> getPermissionsByRole(Integer integerParam);
}