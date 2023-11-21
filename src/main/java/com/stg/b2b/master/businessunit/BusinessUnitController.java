package com.stg.b2b.master.businessunit;

import com.stg.b2b.entity.BusinessUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BusinessUnitController {

    @Autowired
    BusinessUnitService businessUnitService;

    /**
     * Fetching productList with businessUnitName
     *
     * @param buName
     * @return ProductLine
     */
    @QueryMapping
    List<ProductLineDto> getProductList(@Argument String buName){
        return businessUnitService.getProductList(buName);
    }

    /**
     * Fetching List of All BusinessUnits
     *
     * @return List of BusinessUnit
     */
    @QueryMapping
    List<BusinessUnitsDto> getBusinessUnitList(){return businessUnitService.getBusinessUnitList();}

    /**
     * Adding new Business Unit
     *
     * @param businessUnitDto
     * @return return newly added Business Unit
     */
    @MutationMapping
    BusinessUnit addBusinessUnit(@Argument BusinessUnitDto businessUnitDto){
        return businessUnitService.addBusinessUnit(businessUnitDto);
    }

    @MutationMapping
    BusinessUnit updateBusinessUnit(@Argument UpdateBusinessUnitDto updateBusinessUnitDto){
        return businessUnitService.updateBusinessUnit(updateBusinessUnitDto);
    }

    @MutationMapping
    BusinessUnit addProductLine(@Argument AddProductLineDto addProductLineDto){
        return businessUnitService.addProductLine(addProductLineDto);
    }

    @MutationMapping
    BusinessUnit updateProductLine(@Argument UpdateProductLineDto updateProductLineDto){
        return businessUnitService.updateProductLine(updateProductLineDto);
    }
}
