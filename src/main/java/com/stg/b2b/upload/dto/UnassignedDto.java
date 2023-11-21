package com.stg.b2b.upload.dto;

import com.stg.b2b.entity.BusinessUnit;
import com.stg.b2b.entity.Manager;
import com.stg.b2b.entity.Master;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UnassignedDto {
    private Master orderType;
    private Master region;
    private BusinessUnit businessUnit;
    private Manager manager;
}
