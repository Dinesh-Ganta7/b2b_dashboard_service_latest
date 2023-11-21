package com.stg.b2b.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


@Data
@Entity
@Table(name = "master")
@ToString()
@NoArgsConstructor
@AllArgsConstructor
public class Master implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "master_id", nullable = false)
    private Integer masterId;

    @Column(name = "master_category", length = 250)
    private String masterCategory;

    @Column(name = "master_name", length = 250)
    private String masterName;

}
