package com.stg.b2b.hrreports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DetailsDto {

    private int orderNo;
    private int noOfPositions;
}
