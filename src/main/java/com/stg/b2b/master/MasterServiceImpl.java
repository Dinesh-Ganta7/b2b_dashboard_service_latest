package com.stg.b2b.master;

import com.stg.b2b.entity.Master;
import com.stg.b2b.exception.NotFoundException;
import com.stg.b2b.repository.MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MasterServiceImpl implements MasterService {

    @Autowired
    MasterRepository masterRepository;

    @Override
    public List<Master> getMasterData(String stringParam) throws NotFoundException {
        return masterRepository.findByMasterCategory(stringParam).orElseThrow(() -> new NotFoundException("Data Not Found"));
    }

    @Override
    public List<Master> getAllMasterData() {
        return masterRepository.findAll();
    }


    @Override
    public Master addMasterData(MasterDto masterDto) {
        return masterRepository.save(new Master(masterDto.getMasterId(),masterDto.getMasterCategory(), masterDto.getMasterName()));
    }

    @Override
    public Master updateMaster(MasterUpdateDto masterUpdateDto) {
        Master master  = masterRepository.findByMasterName(masterUpdateDto.getExistingMasterName()).get();
        Master masterTemp = new Master();
         if(master != null){
             masterTemp.setMasterId(master.getMasterId());
             masterTemp.setMasterCategory(master.getMasterCategory());
             masterTemp.setMasterName(masterUpdateDto.getUpdatedMasterName());
             masterRepository.save(masterTemp);
         }



        return masterTemp;
    }

    @Override
    public List<String> getMasterCategories() {
        return masterRepository.getMasterCategories();
    }
}
