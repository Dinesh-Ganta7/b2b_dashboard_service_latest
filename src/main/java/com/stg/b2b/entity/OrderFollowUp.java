package com.stg.b2b.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="order_follow_up")
@NamedQuery(name="OrderFollowUp.findAll", query="SELECT o FROM OrderFollowUp o")
public class OrderFollowUp {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="order_follow_up_id", unique = true,nullable = false)
    private Integer orderFollowUpId;

    @Column(name="ordfol_did_customer_reached_out")
    private Boolean ordfolDidCustomerReachedOut;

    @Column(name="ordfol_probability")
    private Integer ordfolProbability;

    @Column(name="ordfol_recruiter",length=250)
    private String ordfolRecruiter;

    @Column(name="ordfol_submitted_above_target")
    private Boolean ordfolSubmittedAboveTarget;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "order_no", referencedColumnName = "order_no",nullable = false),
            @JoinColumn(name = "ord_broadcast_date",referencedColumnName = "ord_broadcast_date",   nullable=false)
    })
    private Order order;
}
