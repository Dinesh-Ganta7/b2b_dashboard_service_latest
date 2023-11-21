package com.stg.b2b.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bid_info")
@ToString
public class BidInfo {

    @Id
    @Column(name = "bid_no")
    private Integer bidNo;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "order_no",referencedColumnName = "order_no",nullable = false),
            @JoinColumn(name = "ord_broadcast_date",referencedColumnName = "ord_broadcast_date", nullable = false)
    })
    private Order order;

    @Column(name = "bid_status",length = 250)
    private String bidStatus;

    @Column(name = "bid_name", length = 250)
    private String bidName;

    @Column(name = "bid_received_date")
    private Date bidReceivedDate;

    @Column(name="bid_over_target")
    private Boolean bidOverTarget;

    @Column(name = "bid_po_number",length = 250)
    private String bidPoNumber;

    @Column(name="bid_active_status")
    private Boolean bidActiveStatus;

    @OneToMany(mappedBy = "bidInfo")
    @JsonManagedReference(value = "bid_interview")
    private List<Interview> interviews;

    @Column(name = "bid_external_or_internal", length = 250)
    private String bidExternalOrInternal;

    @Column(name = "bid_skill_set", length = 250)
    private  String bidSkillSet;

    @Column(name = "bid_created_by",length = 250)
    private String bidCreatedBy;

    @Column(name = "bid_created_at")
    private Date bidCreatedAt;

    @Column(name = "bid_updated_by",length = 250)
    private String bidUpdatedBy;

    @Column(name = "bid_updated_at")
    private Date bidUpdatedAt;

}
