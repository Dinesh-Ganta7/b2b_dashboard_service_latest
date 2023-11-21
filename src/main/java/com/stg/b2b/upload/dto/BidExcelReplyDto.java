package com.stg.b2b.upload.dto;

import com.stg.b2b.entity.BidInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BidExcelReplyDto {
    List<BidExcelDto> newBids;
    List<BidInfo> closingBids;
}
