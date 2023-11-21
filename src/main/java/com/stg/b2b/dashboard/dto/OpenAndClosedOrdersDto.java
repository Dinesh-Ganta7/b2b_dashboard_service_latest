package com.stg.b2b.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAndClosedOrdersDto {

    int openOrders;
    int closedOrders;
}
