package com.stg.b2b.master.businessunit;

import com.stg.b2b.entity.BusinessUnit;
import com.stg.b2b.entity.BusinessUnitPK;
import com.stg.b2b.exception.NotFoundException;
import com.stg.b2b.repository.BusinessUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessUnitServiceImpl implements BusinessUnitService {

    @Autowired
    BusinessUnitRepository businessUnitRepository;

    @Override
    public List<ProductLineDto> getProductList(String buName) throws NotFoundException {
        return businessUnitRepository.findProductLinesByBuName(buName).orElseThrow(() -> new NotFoundException("No Product Lines for " + buName));
    }

    @Override
    public List<BusinessUnitsDto> getBusinessUnitList() {
        return businessUnitRepository.getBusinessUnitList().orElseThrow(() -> new NotFoundException("No existing Business Units"));
    }

    @Override
    public BusinessUnit addBusinessUnit(BusinessUnitDto businessUnitDto) {
        Integer buIdsCount = businessUnitRepository.getBusinessUnitList().get().size();
        if(buIdsCount > 0){
            BusinessUnit businessUnit = new BusinessUnit();
            businessUnit.setId(new BusinessUnitPK());
            businessUnit.getId().setBuId(buIdsCount+1);
            businessUnit.getId().setProductLineId(1);
            businessUnit.setBuName(businessUnitDto.getBuName());
            businessUnit.setProductLineName("Not Known");
            return businessUnitRepository.save(businessUnit);
        } else throw new NotFoundException("No existing Business Units");

    }

    @Override
    public BusinessUnit updateBusinessUnit(UpdateBusinessUnitDto updateBusinessUnitDto) {
        BusinessUnit businessUnit = businessUnitRepository.findByBuName(updateBusinessUnitDto.getExistingBuName()).orElseThrow(() -> new NotFoundException("BusinessUnit Not Found"));
        businessUnit.setBuName(updateBusinessUnitDto.getUpdatedBuName());
        return businessUnitRepository.save(businessUnit);
    }

    @Override
    public BusinessUnit addProductLine(AddProductLineDto addProductLineDto) {
        BusinessUnitsDto businessUnit = businessUnitRepository.findDistinctBU(addProductLineDto.getBuName()).orElseThrow(() -> new NotFoundException("No Business Unit found"));
        Integer productLineIdsCount = businessUnitRepository.findProductLinesByBuName(addProductLineDto.getBuName()).get().size();
        if(productLineIdsCount >0){
            BusinessUnit newBusinessUnit = new BusinessUnit();
            newBusinessUnit.setId(new BusinessUnitPK());
            newBusinessUnit.getId().setBuId(businessUnit.getBusinessUnitId());
            newBusinessUnit.getId().setProductLineId(productLineIdsCount+1);
            newBusinessUnit.setBuName(businessUnit.getBusinessUnitName());
            newBusinessUnit.setProductLineName(addProductLineDto.getProductLineName());
            return businessUnitRepository.save(newBusinessUnit);
        } else throw new NotFoundException("No existing Product Lines");
    }

    @Override
    public BusinessUnit updateProductLine(UpdateProductLineDto updateProductLineDto) {
        BusinessUnit businessUnit = businessUnitRepository.findByBuNameAndProductLineName(updateProductLineDto.getBuName(), updateProductLineDto.getExistingProductLineName()).orElseThrow(() -> new NotFoundException("No Business Unit found for "+ updateProductLineDto.existingProductLineName));
        businessUnit.setProductLineName(updateProductLineDto.getUpdatedProductLineName());
        return businessUnitRepository.save(businessUnit);
    }


}
