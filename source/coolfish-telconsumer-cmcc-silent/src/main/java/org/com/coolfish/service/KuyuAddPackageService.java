package org.com.coolfish.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.com.coolfish.entity.KuyuAddPackage;
import org.com.coolfish.repository.KuyuAddPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KuyuAddPackageService {
    @Autowired
    private KuyuAddPackageRepository repository;

    @Transactional
    public void delete(Integer id) {
        repository.delete(id);
    }

    @Transactional(readOnly = true)
    public KuyuAddPackage get(Integer id) {
        return repository.findOne(id);
    }

    @Transactional
    public void save(KuyuAddPackage kuyu) {
        repository.saveAndFlush(kuyu);
    }

    @Transactional(readOnly = true)
    public List<KuyuAddPackage> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public BigDecimal sumFlow(String tel) {
      return repository.SumFlow(tel);
      
    }
    
    @Transactional(readOnly = true)
    public BigDecimal sumFlowTal(String tel) {
          return repository.SumFlowTal(tel);
    } 
    @Transactional
    public void upPackageTime(Date starttime,  Date endtime,Long id) {
       repository.upPackageTime(starttime,endtime,id);
  }
    @Transactional
    public List<KuyuAddPackage> findAllSlient(String tel) {
        return repository.findAllSlient(tel);
    } 

}
