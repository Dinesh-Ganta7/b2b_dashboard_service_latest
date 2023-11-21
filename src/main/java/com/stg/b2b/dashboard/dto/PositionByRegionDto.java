package com.stg.b2b.dashboard.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PositionByRegionDto {
    private String region;
    private HashMap<String,Integer> orderTypeCount;

    private int grandTotal;




}
