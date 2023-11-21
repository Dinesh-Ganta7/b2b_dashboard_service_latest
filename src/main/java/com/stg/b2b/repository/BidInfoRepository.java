package com.stg.b2b.repository;

import com.stg.b2b.bidinfo.dto.BidInfoResponse;
import com.stg.b2b.dashboard.dto.BidsByReceivedDateMapper;
import com.stg.b2b.dashboard.dto.CountWDateDto;
import com.stg.b2b.entity.BidInfo;
import com.stg.b2b.hrreports.dto.BidDetailsResponse;
import com.stg.b2b.util.QueryConstants;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface BidInfoRepository extends JpaRepository<BidInfo, Integer> {

    @Query(value = QueryConstants.GET_BIDS_QUERY, nativeQuery = true)
    List<BidInfoResponse> getAllBids(Boolean status);

    Optional<Integer> countByBidActiveStatus(boolean bidActiveStatus);

    @Query("SELECT COUNT(b) FROM BidInfo AS b WHERE b.bidActiveStatus = 1")
    public Optional<Integer> getActiveBidsCount();


    @Query("SELECT " +
            "b.order.id.orderNo as orderNo," +
            "COUNT(DISTINCT(b.bidName)) AS countOfProfile," +
            "(SELECT GROUP_CONCAT(bid.bidName) FROM BidInfo bid WHERE bid.order.id.orderNo = b.order.id.orderNo) AS bidNameList," +
            "o.ordStratification AS ordStratification, o.ordSkillGroup AS ordSkillGroup, o.ordPreferredSkill AS ordPreferredSkill, o.id.ordBroadcastDate AS ordBroadcastDate FROM " +
            "BidInfo b " +
            "JOIN " +
            "Order o ON b.order.id.orderNo = o.id.orderNo " +
            "WHERE o.ordActiveStatus = 1 " +
            "GROUP BY  b.order.id.orderNo " +
            "ORDER By o.id.ordBroadcastDate DESC")
    List<BidDetailsResponse> findBidDetails();


//    @Query(value = "SELECT COUNT(DISTINCT(b.position_no)) FROM bid_info as b INNER JOIN \n" +
//            "position as p ON b.position_no = p.position_no\n" +
//            "WHERE b.bid_active_status = 1 and p.pos_active_status=1;", nativeQuery = true)
//    public Optional<Integer> getSourcingPositionCount();

//    @Query(value = "SELECT COUNT(DISTINCT b.order_no) FROM bid_info as b INNER JOIN order_t as o ON b.order_no = o.order_no WHERE b.bid_active_status = 1 AND o.ord_active_status = 1;", nativeQuery = true)
//    public Optional<Integer> getSourcingPositionCount();


//    @Query(value = "SELECT COUNT(DISTINCT CONCAT(b.order_no + ' ' + b.ord_broadcast_date)) FROM bid_info as b LEFT JOIN order_t as o ON\n" +
//            " b.order_no = o.order_no WHERE b.bid_active_status = 1 AND o.ord_active_status = 1;", nativeQuery = true)
//    public Optional<Integer> getSourcingPositionCount();

   @Query("SELECT DISTINCT (o.id.orderNo) FROM Order o WHERE o.id.orderNo IN (SELECT DISTINCT b.order.id.orderNo FROM BidInfo b WHERE b.bidActiveStatus = 1) AND o.ordActiveStatus = 1")
    public Optional<List<Integer>> getSourcingPositionCount();

//    @Query(value = "SElECT COUNT(DISTINCT CONCAT(o.order_no + ' ' + o.ord_broadcast_date)) FROM order_t as o WHERE CONCAT(o.order_no + ' ' + o.ord_broadcast_date) NOT IN \n" +
//            "(SElECT DISTINCT(CONCAT(b.order_no + ' ' + b.ord_broadcast_date)) FROM bid_info as b \n" +
//            "WHERE b.bid_active_status=1) AND o.ord_active_status = 1;", nativeQuery = true)
//    public Optional<Integer> getNonSourcingPositionCount();

    @Query("SELECT DISTINCT (o.id.orderNo) FROM Order o WHERE o.id.orderNo NOT IN (SELECT DISTINCT b.order.id.orderNo FROM BidInfo b WHERE b.bidActiveStatus = 1) AND o.ordActiveStatus = 1")
    public Optional<List<Integer>> getNonSourcingPositionCount();
    @Query("SELECT COUNT(b) AS bidsCount , b.bidReceivedDate AS bidReceivedDate FROM BidInfo AS b WHERE b.bidActiveStatus = :bidActiveStatus GROUP BY b.bidReceivedDate")
    public Optional<List<BidsByReceivedDateMapper>> getBidsByReceivedDate(Boolean bidActiveStatus);

    @Query(value="SELECT  b FROM BidInfo b WHERE b.bidActiveStatus = :bidActiveStatus ORDER BY b.bidReceivedDate DESC")
    List<BidInfo> findByBidActiveStatus(Boolean bidActiveStatus);


    BidInfo findByBidNo(Integer bidNo);

    @Modifying
    @Transactional
    @Query(value = "UPDATE bid_info SET bid_active_status = false WHERE bid_no NOT IN :bidNos", nativeQuery = true)
    void closeBidNos(List<Integer> bidNos);

    @Query(value =
            "SELECT bid.bid_received_date AS date, COUNT(bid.bid_no) AS count " +
                    "FROM bid_info bid " +
                    "WHERE bid.bid_received_date BETWEEN :startDate AND :endDate " +
                    "AND bid.bid_active_status = :activeStatus " +
                    "GROUP BY bid.bid_received_date " +
                    "ORDER BY bid.bid_received_date",
            nativeQuery = true)
    List<CountWDateDto> getBidsCountWDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("activeStatus") Boolean b);

    @Query(value =
            "SELECT bid.bid_received_date AS date, COUNT(bid.bid_no) AS count " +
                    "FROM bid_info bid " +
                    "WHERE bid.bid_received_date BETWEEN :startDate AND :endDate " +
                    "GROUP BY bid.bid_received_date " +
                    "ORDER BY bid.bid_received_date",
            nativeQuery = true)
    List<CountWDateDto> getBidsCountWDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}