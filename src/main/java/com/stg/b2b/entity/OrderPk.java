package com.stg.b2b.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Embeddable
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPk {
    @Column(name = "order_no", nullable = false)
    private Integer orderNo;

    @Column(name = "ord_broadcast_date", nullable = false)
    private Date ordBroadcastDate;
}
