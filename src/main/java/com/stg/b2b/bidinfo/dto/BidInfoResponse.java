package com.stg.b2b.bidinfo.dto;

import java.sql.Date;

public interface BidInfoResponse {
    Integer getBidNo();
    String getBidName();
    String getBidStatus();
    Date getBidReceivedDate();
    Boolean getBidOverTarget();
    String getBidPoNumber();
    Boolean getBidActiveStatus();
    String getBidExternalOrInternal();
    String getBidSkillSet();
    Integer getOrderNo();

    Date getOrdBroadcastDate();




}
