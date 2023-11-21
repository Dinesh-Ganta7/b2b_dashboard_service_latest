package com.stg.b2b.bidinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BidInfoDto {
    private Integer bidNo;
    private Integer orderNo;
    private String bidName;
    private String bidStatus;
    private Date bidReceivedDate;
    private Boolean bidOverTarget;
    private String bidPoNumber;
    private Boolean bidActiveStatus;
    private String bidExternalOrInternal;
    private  String bidSkillSet;
    private Date ordBroadcastDate;


}
