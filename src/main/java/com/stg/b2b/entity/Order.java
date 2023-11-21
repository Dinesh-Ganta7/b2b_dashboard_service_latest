package com.stg.b2b.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;


@Data
@Entity
@Table(name = "order_t")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    @EmbeddedId
    private OrderPk id;

    @Column(name = "ord_stratification", length = 250)
    private String ordStratification;

    @Column(name = "ord_no_of_positions")
    private Integer ordNoOfPositions;

    @Column(name = "ord_preferred_skill", length = 2000)
    private String ordPreferredSkill;

    @Column(name = "ord_skill_required", length = 2000)
    private String ordSkillRequired;

    @Column(name = "ord_job_description", length = 2000)
    private String ordJobDescription;

    @Column(name = "ord_skill_group", length = 250)
    private String ordSkillGroup;

    @Column(name = "ord_target_rate")
    private Float ordTargetRate;

    @Column(name = "ord_active_status")
    private Boolean ordActiveStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mas_order_type_id", referencedColumnName = "master_id")
    private Master orderType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mas_region_id", referencedColumnName = "master_id")
    private Master region;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", referencedColumnName = "manager_id")
    private Manager manager;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "bu_id", referencedColumnName = "bu_id")
            , @JoinColumn(name = "bu_product_line_id", referencedColumnName = "product_line_id")
    })
    private BusinessUnit businessUnit;

    @Column(name = "ord_created_by", length = 250)
    private String ordCreatedBy;

    @Column(name = "ord_created_at")
    private Date ordCreatedAt;

    @Column(name = "ord_updated_by", length = 250)
    private String ordUpdatedBy;

    @Column(name = "ord_updated_at")
    private Date ordUpdatedAt;

}
