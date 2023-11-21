package com.stg.b2b.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "manager")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Manager {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="manager_id")
    private Integer managerId;

    @Column(name="ll2_manager",length = 250)
    private String ll2Manager;

    @Column(name="ll3_manager",length = 250)
    private String ll3Manager;

    @Column(name="ll4_manager",length = 250)
    private String ll4Manager;

    @Column(name="ll5_manager",length = 250)
    private String ll5Manager;

    @Column(name="ll6_manager",length = 250)
    private String ll6Manager;


}
