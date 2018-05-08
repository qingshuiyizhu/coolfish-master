package org.com.coolfish.common.database.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.com.coolfish.common.database.entity.KuyuAddPackage;
import org.com.coolfish.common.database.repository.KuyuAddPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KuyuAddPackageService {
    @Autowired
    private KuyuAddPackageRepository repository;

    @Transactional
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public KuyuAddPackage get(Integer id) {
        return repository.getOne(id);
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
    public BigDecimal SumFlow(String tel) {
        return repository.SumFlow(tel);

    }

    public BigDecimal SumFlowTal(String tel) {
        return repository.SumFlowTal(tel);
    }

    @Transactional(readOnly = true)
    public List<KuyuAddPackage> findFlashObject(String tel) {

        return repository.findFlashObject(tel);
    }

    @Transactional
    public void flashSilentTime(Date endtime, Integer id) {
        repository.flashSlientTime(endtime, id);

    }

    @Transactional(readOnly = true)
    public BigDecimal findSumFlows(Integer cardId) {
       
        return repository.findSumFlows(cardId);
    }
  
    @Transactional(readOnly = true)
    public BigDecimal findMonthlyNullSumFlow(Integer id) {
     
        return  repository.findMonthlyNullSumFlow(id);
    }
    @Transactional(readOnly = true)
    public List<KuyuAddPackage> findMonthlyHaveSumFlow() {
      
        return  repository.findMonthlyHaveSumFlow();
    }

}
