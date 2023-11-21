package com.stg.b2b.master;

import com.stg.b2b.entity.Master;

import java.util.List;

public interface MasterService {
    List<Master> getMasterData(String stringParam);

    List<Master> getAllMasterData();

    Master addMasterData(MasterDto masterDto);

    Master updateMaster(MasterUpdateDto masterUpdateDto);

    List<String> getMasterCategories();

}
