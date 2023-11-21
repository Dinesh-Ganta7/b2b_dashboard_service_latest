package com.stg.b2b.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionByBUDTO {
    private String businessUnit;
    private HashMap<String,Integer> orderTypeCount;
    private int grandTotal;

}
