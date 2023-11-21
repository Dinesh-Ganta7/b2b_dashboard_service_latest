package com.stg.b2b.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_id", unique=true, nullable=false)
    private Integer userId;

    @Column(name="user_domain_id", nullable=false, length=250)
    private String userDomainId;
    //uni-directional many-to-one association to RoleTbl
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="role_id", nullable=false,referencedColumnName = "role_id")
    private Role role;
    @Column(name="user_created_by", length=250)
    private String userCreatedBy;

    @Column(name="user_created_at")
    private Date userCreatedAt;

    @Column(name="user_updated_by", length=250)
    private String userUpdatedBy;

    @Column(name="user_updated_at")
    private Date userUpdatedAt;

}
