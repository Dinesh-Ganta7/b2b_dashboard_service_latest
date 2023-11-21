package com.stg.b2b.repository;

import com.stg.b2b.entity.Master;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MasterRepository extends JpaRepository<Master, Integer>{
    Optional<List<Master>> findByMasterCategory(String stringParam);

    Optional<Master> findByMasterName(String masterName);

    Optional<Master> findByMasterCategoryAndMasterName(String masterCategory,String masterName);


    @Query("SELECT DISTINCT(m.masterCategory) FROM Master m")
    List<String> getMasterCategories();



}
