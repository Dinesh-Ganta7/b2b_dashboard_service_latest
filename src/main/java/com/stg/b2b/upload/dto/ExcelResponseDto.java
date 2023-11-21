package com.stg.b2b.upload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExcelResponseDto {
    private Integer orderNo;
    private String stratification;
    private Date broadcastDate;
}
