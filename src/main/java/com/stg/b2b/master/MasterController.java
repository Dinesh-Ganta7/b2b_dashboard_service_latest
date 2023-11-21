package com.stg.b2b.master;

import com.stg.b2b.entity.Master;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MasterController {

    @Autowired
    MasterService masterService;

    /**
     * Fetching master data using category Name
     *
     * @param categoryName
     * @return List of Master
     */
    @QueryMapping
    List<Master> getMasterData(@Argument String categoryName){
        return masterService.getMasterData(categoryName);
    }


    @QueryMapping
    List<Master> getAllMasterData(){
        return masterService.getAllMasterData();
    }

    @QueryMapping
    List<String> getMasterCategories(){
     return masterService.getMasterCategories();
    }


    /**
     * adding Master data
     *
     * @param masterDto
     * @return Master
     */
    @MutationMapping
    Master addMasterData(@Argument("masterInput") MasterDto masterDto){
        return masterService.addMasterData(masterDto);
    }

    @MutationMapping
    Master updateMaster(@Argument("masterUpdateInput") MasterUpdateDto masterUpdateDto){
        return masterService.updateMaster(masterUpdateDto);
    }

}
