package com.stg.b2b.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;



@Entity
@Table(name = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Role {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="role_id",unique=true, nullable=false)
    private Integer roleId;

    @Column(name="role_name",length = 250)
    private String roleName;

    @Column(name="role_created_at")
    private Date roleCreatedAt;

    @Column(name="role_created_by",length = 250)
    private String roleCreatedBy;

    @Column(name="role_updated_at")
    private Date roleUpdatedAt;

    @Column(name="role_updated_by",length = 250)
    private String roleUpdatedBy;

}
