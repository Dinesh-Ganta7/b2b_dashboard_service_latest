package com.stg.b2b.dashboard.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenPositionsSummaryDTO {
    int allOpenPositions;

    int allClosedPositions;
    int totalActiveBids;

    int totalClosedBids;

    List<Integer> sourcing;

    List<Integer> notSourcing;

    int today;

    int thisWeek;

    int thisMonth;

    int previousMonth;

}
