package com.stg.b2b.hrreports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SkillGroupResponseDto {
    private String name;
    private int noOfPositions;
}
