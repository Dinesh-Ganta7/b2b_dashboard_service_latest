package com.stg.b2b.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@Entity
@Table(name = "position")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Position {

    @EmbeddedId
    private PositionPK id;

    @Column(name="pos_active_status")
    private Boolean posActiveStatus;

    @Column(name="pos_broadcast_date")
    private Date posBroadcastDate;

    @Column(name="pos_resume_limit")
    private Integer posResumeLimit;
    @Column(name="pos_created_at")
    private Date posCreatedAt;

    @Column(name="pos_created_by")
    private String posCreatedBy;

    @Column(name="pos_updated_at")
    private Date posUpdatedAt;

    @Column(name="pos_updated_by")
    private String posUpdatedBy;
}
