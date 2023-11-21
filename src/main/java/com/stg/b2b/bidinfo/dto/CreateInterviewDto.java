package com.stg.b2b.bidinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.time.LocalDateTime;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInterviewDto {

    private Integer bidNo;
    private Integer interviewId;
    private LocalDateTime interviewDate;
    private String interviewResult;
}
