package com.stg.b2b.repository;

import com.stg.b2b.master.businessunit.BusinessUnitsDto;
import com.stg.b2b.master.businessunit.ProductLineDto;
import com.stg.b2b.entity.BusinessUnit;
import com.stg.b2b.entity.BusinessUnitPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;


@Repository
public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, BusinessUnitPK>{

    @Query("Select distinct b.buName from BusinessUnit as b")
    Optional<List<String>> getAllBusinessUnits();


    @Query("SELECT bu.id.productLineId AS productLineId, bu.productLineName AS productLineName FROM BusinessUnit bu WHERE bu.buName= :stringParam ")
    Optional<List<ProductLineDto>> findProductLinesByBuName(String stringParam);

    @Query("SELECT bu FROM BusinessUnit bu WHERE bu.id.buId = :buId AND bu.id.productLineId = :productLineId")
    Optional<BusinessUnit> getBusinessUnit(Integer buId, Integer productLineId);


    @Query("SELECT DISTINCT(bu.id.buId) AS businessUnitId, bu.buName AS businessUnitName  FROM BusinessUnit bu")
    Optional<List<BusinessUnitsDto>> getBusinessUnitList();


    Optional<BusinessUnit> findByBuNameAndProductLineName(String stringParam1, String stringParam2);

    Optional<BusinessUnit> findByBuName(String stringParam);

    @Query(value="SELECT distinct bu_id as businessUnitId, bu_name as businessUnitName FROM business_unit WHERE bu_name = :stringParam", nativeQuery = true)
    Optional<BusinessUnitsDto> findDistinctBU(String stringParam);
}
