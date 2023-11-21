package com.stg.b2b.repository;

import com.stg.b2b.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer>{

    Optional<Manager> findByLl6Manager(String stringParam);
    @Query(value = "SELECT m.* " +
            "FROM manager AS m " +
            "INNER JOIN ( " +
            "    SELECT MAX(manager_id) AS manager_id, ll5_manager " +
            "    FROM manager " +
            "    GROUP BY ll5_manager, ll4_manager, ll3_manager, ll2_manager " +
            ") AS unique_ll5 " +
            "ON m.manager_id = unique_ll5.manager_id", nativeQuery = true)
    Optional<List<Manager>> getLL5Manager();
}
