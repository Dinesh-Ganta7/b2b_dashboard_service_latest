package com.stg.b2b.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphResponseDto {
    private Date startDate;
    private Date lastDate;

    private List<CountWDateDto> data;
}
