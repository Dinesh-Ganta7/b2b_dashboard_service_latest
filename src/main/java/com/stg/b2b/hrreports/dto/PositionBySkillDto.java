package com.stg.b2b.hrreports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PositionBySkillDto {
    private String skillGroup;
    private int totalPositions;
    private List<SkillGroupResponseDto> skillSet;



}
