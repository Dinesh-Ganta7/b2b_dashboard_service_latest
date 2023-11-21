package com.stg.b2b.master.manager;

import com.stg.b2b.entity.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ManagerController {

    @Autowired
    ManagerService managerService;

    /**
     * Fetching list of managers with ll6Manager
     *
     * @param ll6Manager
     * @return Manager
     */
    @QueryMapping
    Manager getManagerList(@Argument String ll6Manager){
        return managerService.getManagerList(ll6Manager);
    }

    /**
     * Fetching List of ll6Managers
     *
     * @return Manager
     */
    @QueryMapping
    List<Manager> getLL6List(){return managerService.getLL6List();}

    @MutationMapping
    Manager addManager(@Argument ManagerDto managerDto){
        return managerService.addManager(managerDto);
    }

    @MutationMapping
    Manager updateManager(@Argument ManagerDto managerDto){
        return managerService.updateManager(managerDto);
    }

    @QueryMapping
    List<Manager> getLL5List(){
        return managerService.getLL5List();
    }

}
