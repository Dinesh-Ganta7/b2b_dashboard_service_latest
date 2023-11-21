package com.stg.b2b.hrreports.dto;

import java.util.Date;
import java.util.List;

public interface BidDetailsResponse {
     Integer getOrderNo();
     int getCountOfProfile();
     List<String> getBidNameList();
     String getOrdStratification();
     String getOrdSkillGroup();
     String getOrdPreferredSkill();
     Date getOrdBroadcastDate();
}
