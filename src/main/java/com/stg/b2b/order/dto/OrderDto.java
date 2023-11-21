package com.stg.b2b.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {
    private Integer orderNo;
    private  Integer orderTypeId;
    private String orderType;

    private  Integer regionId;
    private String regionName;

    private  Integer managerId;
    private String ll6Manager;

    private  Integer buId;
    private String buName;

    private Integer productLineId;
    private String productLineName;
    private Long noOfPositions;
    private Date broadcastDate;
    private Boolean ordStatus;
    private Boolean positionStatus;
    private String jobDescription;
    private String preferredSkills;
    private String skillGroup;
    private String skillRequired;
    private String stratification;
    private Float targetRate;
    private String ll2Manager;
    private String ll3Manager;
    private String ll4Manager;
    private String ll5Manager;

    private String positionNos;

    private String recruiter;
    private Integer probability;
    private Boolean submittedAboveTarget;
    private Boolean didCustomerReachedOut;

}

