package com.stg.b2b.repository;

import com.stg.b2b.entity.OrderFollowUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;

public interface OrderFollowUpRepository extends JpaRepository<OrderFollowUp, Integer>{
    @Query(value="SELECT * FROM order_follow_up as f WHERE f.order_no= :orderNo AND f.ord_broadcast_date= :broadcastDate ",nativeQuery = true)
    public OrderFollowUp getOrderFollowUp(Integer orderNo, Date broadcastDate);
}
