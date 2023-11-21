package com.stg.b2b.master.businessunit;

import com.stg.b2b.entity.BusinessUnit;

import java.util.List;

public interface BusinessUnitService {
    List<ProductLineDto> getProductList(String stringParam);

    List<BusinessUnitsDto> getBusinessUnitList();

    BusinessUnit addBusinessUnit(BusinessUnitDto businessUnitDto);

    BusinessUnit updateBusinessUnit(UpdateBusinessUnitDto updateBusinessUnitDto);

    BusinessUnit addProductLine(AddProductLineDto addProductLineDto);

    BusinessUnit updateProductLine(UpdateProductLineDto updateProductLineDto);
}
