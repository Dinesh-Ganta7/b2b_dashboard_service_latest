package com.stg.b2b.dashboard.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClosedPositionsSummaryDTO {


    private int today ;
    private int thisWeek;

    private int thisMonth;

    private int previousMonth;

    private int closedPositionsCount;
}
