package com.stg.b2b.repository;


import com.stg.b2b.dashboard.dto.*;

import com.stg.b2b.entity.Position;
import com.stg.b2b.entity.PositionPK;
import com.stg.b2b.hrreports.dto.PositionByMonthDto;
import com.stg.b2b.util.QueryConstants;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface PositionRepository extends JpaRepository<Position, PositionPK> {

    @Query("select count(o.id.orderNo) as orderCount,sum(o.ordNoOfPositions) as noOfPositions ,up.ordfolRecruiter as recruiter from Order as o join OrderFollowUp as up ON o.id.orderNo=up.order.id.orderNo AND o.id.ordBroadcastDate = up.order.id.ordBroadcastDate WHERE o.id.orderNo IS NOT NULL AND o.ordNoOfPositions IS NOT NULL AND up.ordfolRecruiter IS NOT NULL AND  o.ordActiveStatus=1 GROUP BY up.ordfolRecruiter")
    List<Object[]> findAllRecruiters(@Param("ordfolRecruiter") String recruiterName);

    @Query("SELECT m.masterName FROM Master m WHERE m.masterCategory='region'")
    Optional<List<String>>getAllRegionNames();

    @Query("SELECT bu.buName FROM BusinessUnit bu GROUP BY bu.buName ")
    Optional<List<String>>getAllBuNames();

    @Query("SELECT p.id.order.region.masterName AS region, p.id.order.orderType.masterName AS positionType, COUNT(p.id.positionNo) AS noOfVacancy FROM Position AS p WHERE  p.id.order.ordActiveStatus = 1 AND p.posActiveStatus = 1 GROUP BY p.id.order.region, p.id.order.orderType")
    Optional<List<PositionCountByRegionAndType>> getPositionsCountByRegion();

    @Query("SELECT p.id.order.businessUnit.buName AS businessLineName, p.id.order.orderType.masterName AS positionType,  COUNT(p.id.positionNo) AS noOfVacancy FROM Position AS p WHERE  p.id.order.ordActiveStatus = 1 AND p.posActiveStatus = 1 GROUP BY  p.id.order.businessUnit, p.id.order.orderType")
    Optional<List<PositionCountByBUAndType>> getPositionsCountByBU();


    @Query("SELECT p.id.order.ordSkillGroup AS skillGroup, COUNT(p.id.positionNo) AS sumOFNoOfPositions   FROM Position AS p WHERE p.id.order.ordActiveStatus = 1 AND p.posActiveStatus = 1  GROUP BY p.id.order.ordSkillGroup")
    Optional<List<PositionBySkillGroupDTO>> getPositionsCountBySkillGroup();

    @Query("select o.id.orderNo,o.ordNoOfPositions from Order as o join OrderFollowUp as up ON o.id.orderNo=up.order.id.orderNo AND o.id.ordBroadcastDate = up.order.id.ordBroadcastDate WHERE o.ordNoOfPositions IS NOT NULL AND o.id.orderNo IS NOT NULL AND o.ordActiveStatus = 1  AND up.ordfolRecruiter=?1")
    List<Object[]> findDetails(String recruiterName);

    @Query("select ordSkillGroup,ordPreferredSkill,ordNoOfPositions from Order where ordActiveStatus=1 AND ordSkillGroup IS NOT NULL")
    List<Object[]> findPositionBySkill();

    @Query("SELECT DATE_FORMAT(o.id.ordBroadcastDate, '%Y-%m') AS month,SUM(o.ordNoOfPositions) AS ordNoOfPositions,COUNT(o.id.orderNo) AS orderNo FROM Order o WHERE o.id.ordBroadcastDate IS NOT NULL AND o.ordNoOfPositions IS NOT NULL AND o.id.orderNo IS NOT NULL AND o.ordActiveStatus = 1 GROUP BY month ORDER BY month DESC ")
    List<PositionByMonthDto> findPositionByMonth();

    @Query("SELECT DATE_FORMAT(o.id.ordBroadcastDate, '%Y-%m') AS month,SUM(o.ordNoOfPositions) AS ordNoOfPositions,COUNT(o.id.orderNo) AS orderNo FROM Order o WHERE o.id.ordBroadcastDate IS NOT NULL AND o.ordNoOfPositions IS NOT NULL AND o.id.orderNo IS NOT NULL  GROUP BY month ORDER BY month DESC")
    List<PositionByMonthDto> findOrdersByMonth();

    @Modifying
    @Transactional
    @Query(value="update Position p SET p.posActiveStatus=false ,p.posUpdatedBy=:updatedBy,p.posUpdatedAt=:updatedDate where p.id.order.id.orderNo = :orderNo and  p.id.order.id.ordBroadcastDate = :broadcastDate")
    void updateStatusByOrderNoAndBroadcastDate(Integer orderNo, Date broadcastDate,String updatedBy,Date updatedDate);
    @Query(value = "SELECT p FROM Position p WHERE p.id.order.id.orderNo = :orderNo and p.id.order.id.ordBroadcastDate= :broadcastDate")
    List<Position> findPositionsByOrderNoAndBroadcastDate(int orderNo,Date broadcastDate);

    @Query(value = "SELECT p FROM Position p WHERE p.id.order.id.orderNo = :orderNo and p.id.positionNo= :positionNo and p.posActiveStatus = true")
    Position findPositionsByOrderNoAndPositionNo(int orderNo,int positionNo);
    @Query(value = "SELECT p FROM Position p WHERE p.id.positionNo in :positionNos and p.posActiveStatus=1 ")
    List<Position> getRequiredPositions(List<Integer> positionNos);

    @Query(value = "SELECT " +
            "CASE " +
            "WHEN :managerType = 'll6_manager' THEN manager.ll6_manager " +
            "WHEN :managerType = 'll5_manager' THEN manager.ll5_manager " +
            "WHEN :managerType = 'll4_manager' THEN manager.ll4_manager " +
            "WHEN :managerType = 'll3_manager' THEN manager.ll3_manager " +
            "WHEN :managerType = 'll2_manager' THEN manager.ll2_manager " +
            "END AS managerColumn, " +
            "COUNT(position_no) AS noOfPositions " +
            "FROM position " +
            "INNER JOIN order_t ON position.order_no = order_t.order_no " +
            "INNER JOIN manager ON order_t.manager_id = manager.manager_id " +
            "WHERE order_t.ord_active_status = 1 AND position.pos_active_status = 1 GROUP BY managerColumn", nativeQuery = true)
    Optional<List<PositionByManagerQueryResultMapper>> getPositionByManager(@Param("managerType") String managerType);

    @Query("SELECT COUNT(p.id.positionNo) FROM Position as p where p.id.order.ordActiveStatus = :activeStatus AND p.posActiveStatus = :activeStatus")
    Optional<Integer> findPositionsCount(Boolean activeStatus);

    @Query("SELECT p.id.order.orderType.masterName AS positionType  , COUNT(p.id.positionNo) AS noOfPositions FROM Position AS p WHERE  p.id.order.ordActiveStatus = 1 AND p.posActiveStatus = 1 GROUP BY p.id.order.orderType")
    Optional<List<PositionsTypeDTO>> findPositionsByType();


    @Query("SELECT COUNT(p.id.positionNo) as positionsCount, p.id.order.id.ordBroadcastDate as broadcastDate FROM Position AS p WHERE  p.id.order.ordActiveStatus = :activeStatus AND p.posActiveStatus = :activeStatus GROUP BY p.id.order.id.ordBroadcastDate" )
    Optional<List<OpenPositionCountByBroadcastDateMapper>> findPositionsCountByBroadcastDate(boolean activeStatus);

    @Query("SELECT p FROM Position as p where p.id.order.ordActiveStatus = 1 AND p.posActiveStatus = 1")
    Optional<List<Position>> findAllOpenPositions();

    @Query(value = "SELECT" +
            " generated_dates.year," +
            " MONTHNAME(DATE_FORMAT(CONCAT(generated_dates.year, '-', generated_dates.month, '-01'), '%Y-%m-%d')) AS month," +
            " IFNULL(position_counts.position_count, 0) AS positionsCount" +
            " FROM (" +
            " SELECT" +
            " YEAR(DATE_SUB(NOW(), INTERVAL n MONTH)) AS year," +
            " MONTH(DATE_SUB(NOW(), INTERVAL n MONTH)) AS month" +
            " FROM (" +
            " SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL" +
            " SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL" +
            " SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL" +
            " SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11)" +
            " AS months)" +
            " AS generated_dates" +
            " LEFT JOIN (" +
            " SELECT" +
            " YEAR(ord_broadcast_date) AS year," +
            " MONTH(ord_broadcast_date) AS month," +
            " COUNT(*) AS position_count" +
            " FROM" +
            " position" +
            " WHERE" +
            " ord_broadcast_date >= DATE_SUB(NOW(), INTERVAL 11 MONTH) AND pos_active_status = :positionActiveStatus" +
            " GROUP BY" +
            " YEAR(ord_broadcast_date), MONTH(ord_broadcast_date))" +
            " AS position_counts" +
            " ON generated_dates.year = position_counts.year AND generated_dates.month = position_counts.month" +
            " ORDER BY" +
            " generated_dates.year, generated_dates.month;", nativeQuery = true)
    Optional<List<PositionsByMonthMapper>>findPositionsByMonth(boolean positionActiveStatus);

    @Query(value = QueryConstants.GET_REGION_ORDER_TYPES, nativeQuery = true)
    Optional<List<String>> getOrderTypeByRegion();

    @Query(value = QueryConstants.GET_BU_ORDER_TYPES, nativeQuery = true)
    Optional<List<String>> getOrderTypeByBusinessUnit();

    @Query(value =
            "SELECT pos.ord_broadcast_date AS date, COUNT(pos.position_no) AS count " +
                    "FROM position pos " +
                    "JOIN order_t ord ON pos.order_no = ord.order_no AND pos.ord_broadcast_date = ord.ord_broadcast_date " +
                    "WHERE pos.ord_broadcast_date BETWEEN :startDate AND :endDate " +
                    "AND ord.ord_active_status = :b " +
                    "GROUP BY pos.ord_broadcast_date " +
                    "ORDER BY pos.ord_broadcast_date",
            nativeQuery = true)
    List<CountWDateDto> getPositionCountWDates(Date startDate, Date endDate, boolean b);
    @Query(value =
            "SELECT pos.ord_broadcast_date AS date, COUNT(pos.position_no) AS count " +
                    "FROM position pos " +
                    "JOIN order_t ord ON pos.order_no = ord.order_no AND pos.ord_broadcast_date = ord.ord_broadcast_date " +
                    "WHERE pos.ord_broadcast_date BETWEEN :startDate AND :endDate " +
                    "GROUP BY pos.ord_broadcast_date " +
                    "ORDER BY pos.ord_broadcast_date",
            nativeQuery = true)
    List<CountWDateDto> getPositionCountWDates(Date startDate, Date endDate);
}