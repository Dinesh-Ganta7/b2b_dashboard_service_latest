package com.stg.b2b.master.manager;

import com.stg.b2b.entity.Manager;

import java.util.List;

public interface ManagerService {
    Manager getManagerList(String stringParam);

    List<Manager> getLL6List();

    Manager addManager(ManagerDto managerDto);

    Manager updateManager(ManagerDto managerDto);
    List<Manager> getLL5List();


}
