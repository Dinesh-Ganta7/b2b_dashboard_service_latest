package com.stg.b2b.repository;


import com.stg.b2b.dashboard.dto.CountWDateDto;
import com.stg.b2b.entity.Order;
import com.stg.b2b.order.dto.OrderResponseDto;
import com.stg.b2b.util.QueryConstants;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = QueryConstants.GET_ALL_ORDERS_QUERY, nativeQuery = true)
    public List<OrderResponseDto> getOrdersList(Boolean status);

    @Query(value = QueryConstants.GET_SOURCING_ORDERS_QUERY, nativeQuery = true)
    public List<OrderResponseDto> getOrdersListBasedOnSourcingNotSourcing(List<Integer> orders);

    @Query(value= QueryConstants.GET_ORDER_QUERY, nativeQuery = true)
    public OrderResponseDto getOrderByPK(Integer integerParam, Date dateParam);

    @Query(value="SELECT o.* FROM order_t o WHERE o.order_no= :orderNo AND o.ord_broadcast_date = :bdDate",nativeQuery = true)
    public Order getOrder(Integer orderNo, Date bdDate);
    @Query("SELECT o.id.orderNo FROM Order AS o WHERE o.ordActiveStatus = 1 AND o.id.orderNo NOT IN (SELECT p.id.order.id.orderNo FROM Position AS p where p.posActiveStatus=1)")
    public Optional<List<Integer>> ordersWOPositions();
    public List<Order> findByOrdActiveStatus(boolean status);
    @Modifying
    @Transactional
    @Query(value="update Order o SET o.ordActiveStatus=false,o.ordUpdatedBy=:updatedBy,o.ordUpdatedAt=:updatedAt where o.id.orderNo = :orderNo and o.id.ordBroadcastDate = :broadcastDate ")
    public void updateStatusByOrderNoAndBroadcastDate(Integer orderNo , Date broadcastDate,String updatedBy,Date updatedAt);
    @Query(value="SELECT o FROM Order as o WHERE o.id.orderNo in :orderNos")
    List<Order> getRequiredOrders(List<Integer> orderNos);
    @Query(value="SELECT * FROM order_t AS o WHERE order_no = :orderNo and ord_active_status=true ORDER BY ord_broadcast_date DESC LIMIT 1", nativeQuery = true)
    Optional<Order> findByOrderNo(Integer orderNo);
    Optional<List<Order>> findAllByOrdActiveStatus(Boolean ordActiveStatus);


    //@Query("SELECT count(o) FROM Order o WHERE o.ordActiveStatus= :ordActiveStatus AND o.id.orderNo IN (SELECT DISTINCT p.id.order.id.orderNo FROM Position p)")
    @Query(value="SELECT COUNT(DISTINCT CONCAT(o.order_no, '|', o.ord_broadcast_date)) FROM order_t o WHERE o.ord_active_status = :ordActiveStatus ",nativeQuery = true)
    Optional<Integer> countByOrdActiveStatus(boolean ordActiveStatus);
    @Query(value="SELECT COUNT(DISTINCT CONCAT(o.order_no, '|', o.ord_broadcast_date)) FROM order_t o INNER JOIN position p ON o.order_no = p.order_no AND o.ord_broadcast_date = p.ord_broadcast_date WHERE o.ord_active_status = :ordActiveStatus",nativeQuery = true)
    Optional<Integer> countByOrdActiveStatusAndPosActiveStatus(boolean ordActiveStatus);

    @Query(value = "select count(*) from order_t where ord_broadcast_date = :todayDate and ord_active_status=1", nativeQuery = true)
    Optional<Integer> countByOrdCreatedAt(String todayDate);

    @Query(value = "select ord_broadcast_date as date , count(*) as count from order_t where ord_broadcast_date between :startDate and :endDate and ord_active_status = :b group by ord_broadcast_date", nativeQuery = true)
    List<CountWDateDto> getOrderCountWDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("b") boolean b);

    @Query(value = "select ord_broadcast_date as date , count(*) as count from order_t where ord_broadcast_date between :startDate and :endDate group by ord_broadcast_date", nativeQuery = true)
    List<CountWDateDto> getOrderCountWDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
