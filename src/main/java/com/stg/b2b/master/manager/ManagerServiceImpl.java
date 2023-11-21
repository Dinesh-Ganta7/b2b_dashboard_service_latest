package com.stg.b2b.master.manager;

import com.stg.b2b.entity.Manager;
import com.stg.b2b.exception.NotFoundException;
import com.stg.b2b.repository.ManagerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    ManagerRepository managerRepository;

    @Override
    public Manager getManagerList(String ll6Manager) throws NotFoundException{
        return managerRepository.findByLl6Manager(ll6Manager).orElseThrow(() -> new NotFoundException("Data Not Found"));
    }
    @Override
    public List<Manager> getLL5List() {
        return managerRepository.getLL5Manager().get();
    }

    @Override
    public List<Manager> getLL6List() {
        return managerRepository.findAll();
    }

    @Override
    public Manager addManager(ManagerDto managerDto) {
        return managerRepository.save(new Manager(managerDto.getManagerId(),managerDto.getLl2Manager(),managerDto.getLl3Manager(),managerDto.getLl4Manager(),managerDto.getLl5Manager(),managerDto.getLl6Manager())) ;
    }

    @Override
    public Manager updateManager(ManagerDto managerDto) {
      Manager manager = managerRepository.findByLl6Manager(managerDto.getLl6Manager()).orElseThrow(() -> new NotFoundException("Data Not Found")) ;
      System.out.println(manager.toString());
      Manager managerTemp = new Manager();
      if(manager != null){
          managerDto.setManagerId(manager.getManagerId());
          BeanUtils.copyProperties(managerDto,managerTemp);
          managerRepository.save(managerTemp);
      }
        return managerTemp;
    }
}
