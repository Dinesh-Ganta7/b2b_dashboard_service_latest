package com.stg.b2b.dashboard.dto;

import java.sql.Date;

public interface BidsByReceivedDateMapper {

    int getBidsCount();

    Date getBidReceivedDate();

}
