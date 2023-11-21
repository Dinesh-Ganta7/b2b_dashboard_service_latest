package com.stg.b2b.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MasterDto {
    Integer masterId;
    String masterCategory;
    String masterName;
}
