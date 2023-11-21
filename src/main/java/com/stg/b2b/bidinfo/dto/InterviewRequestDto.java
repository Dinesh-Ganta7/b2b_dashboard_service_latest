package com.stg.b2b.bidinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InterviewRequestDto {
    private Integer positionNo;
    private Integer orderNo;
    private String bidFirstName;
    private String bidLastName;
    private Date ordBroadcastDate;
}
