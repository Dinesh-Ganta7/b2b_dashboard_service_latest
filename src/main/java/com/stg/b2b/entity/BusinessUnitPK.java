package com.stg.b2b.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BusinessUnitPK implements Serializable {

    @Column(name="bu_id")
    private Integer buId;

    @Column(name="product_line_id")
    private Integer productLineId;

}
