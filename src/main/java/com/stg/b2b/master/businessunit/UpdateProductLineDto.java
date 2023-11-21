package com.stg.b2b.master.businessunit;

import lombok.Data;

@Data
public class UpdateProductLineDto {
    String buName;
    String existingProductLineName;
    String updatedProductLineName;
}
