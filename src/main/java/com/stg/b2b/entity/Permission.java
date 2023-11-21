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
@Table(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="permission_id")
    private Integer permissionId;

    @Column(name="permission_for",length = 250)
    private String permissionFor;

    @ManyToOne
    @JoinColumn(name="role_id",referencedColumnName = "role_id")
    private Role role;
    @Column(name="permission_created_at")
    private Date permissionCreatedAt;

    @Column(name="permission_created_by",length = 250)
    private String permissionCreatedBy;

    @Column(name="permission_updated_at")
    private Date permissionUpdatedAt;

    @Column(name="permission_updated_by",length = 250)
    private String permissionUpdatedBy;

}
