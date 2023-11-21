package com.stg.b2b.master;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterUpdateDto {
    String existingMasterName;
    String updatedMasterName;
}
