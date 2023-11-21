package com.stg.b2b.dashboard.dto;

import java.sql.Date;

public interface OpenPositionCountByBroadcastDateMapper {
    int getPositionsCount();
    Date getBroadcastDate();
}
