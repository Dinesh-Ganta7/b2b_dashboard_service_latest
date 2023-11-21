package com.stg.b2b.upload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotificationDto {
    private List<ExcelResponseDto> closingOrders;
    private List<ExcelResponseDto> newOrders;
}
