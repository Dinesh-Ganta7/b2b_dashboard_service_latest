package com.stg.b2b.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "business_unit")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BusinessUnit {

    @EmbeddedId
    private BusinessUnitPK id;

    @Column(name="bu_name",length = 250)
    private String buName;

    @Column(name="product_line_name", length = 250)
    private String productLineName;


}
