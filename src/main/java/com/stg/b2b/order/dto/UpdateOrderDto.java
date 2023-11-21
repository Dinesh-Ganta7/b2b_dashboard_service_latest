package com.stg.b2b.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateOrderDto {
    private Integer orderNo;
    private String recruiter;
    private String skillGroup;
    private Boolean didCustomerReachedOut;
    private Integer probability;
    private Boolean submittedAboveTarget;
    private String region;
    private String orderType;
    private String businessUnit;
    private String productLine;
    private String manager;
    private Date broadcastDate;
    private String mailId;

}
