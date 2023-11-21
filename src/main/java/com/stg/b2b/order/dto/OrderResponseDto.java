package com.stg.b2b.order.dto;

import java.util.Date;

public interface OrderResponseDto {
    Integer getOrderNo();

    String getOrderType();
    String getRegionName();
    String getLl6Manager();
    String getBuName();
    String getProductLineName();

    Long getNoOfPositions();
    Date getBroadcastDate();
    Boolean getOrdActiveStatus();
    Boolean getPositionActiveStatus();
    String getJobDescription();
    String getPreferredSkills();
    String getSkillGroup();
    String getSkillRequired();
    String getStratification();
    Float getTargetRate();

    String getLl2Manager();
    String getLl3Manager();
    String getLl4Manager();
    String getLl5Manager();

    String getPositionNos();

    String getRecruiter();
    Integer getProbability();
    Boolean getSubmittedAboveTarget();
    Boolean getDidCustomerReachedOut();
}
