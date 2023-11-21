package com.stg.b2b.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidInfoSummaryDTO {

   private int today ;
   private int thisWeek;

   private int thisMonth;

   private int previousMonth;
}
