package com.stg.b2b.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CandidatePk {

    @Column(name = "candidate_file_id", nullable = false)
    private Integer fileId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "order_no",referencedColumnName = "order_no",nullable = false),
            @JoinColumn(name = "ord_broadcast_date",referencedColumnName = "ord_broadcast_date", nullable = false)
    })
    private Order order;
}
