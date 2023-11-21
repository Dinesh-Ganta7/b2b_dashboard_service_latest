package com.stg.b2b.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * The primary key class for the position_tbl database table.
 */
@Data
@Embeddable
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PositionPK implements Serializable {
    @Column(name="position_no",nullable = false)
    private Integer positionNo;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "order_no",referencedColumnName = "order_no",nullable = false),
            @JoinColumn(name = "ord_broadcast_date",referencedColumnName = "ord_broadcast_date", nullable = false)
    })
    private Order order;
    
}
