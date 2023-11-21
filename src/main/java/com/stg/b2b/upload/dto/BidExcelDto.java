package com.stg.b2b.upload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidExcelDto {
    private Integer bidNumber;
    private String bidFirstName;
    private String bidLastName;
    private Integer orderNo;
    private Integer positionNo;
    private String bidStatus;
    private String bidReceivedDate;

}
