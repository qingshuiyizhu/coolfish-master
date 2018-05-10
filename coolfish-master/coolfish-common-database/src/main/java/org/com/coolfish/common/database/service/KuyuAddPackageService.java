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
    public List<KuyuAddPackage> findFlashObject(Integer cardId) {

        return repository.findFlashObject(cardId);
    }

    @Transactional
    public void flashSilentTime(Date starttime ,Date endtime, Integer id) {
        repository.flashSlientTime(starttime,endtime, id);

    }

    @Transactional(readOnly = true)
    public List<KuyuAddPackage> findMonthlyHaveSumFlow() {

        return repository.findMonthlyHaveSumFlow();
    }

    @Transactional(readOnly = true)
    public BigDecimal findMonthlySumFlows(Integer cardid) {

        return repository.findMonthlySumFlows(cardid);
    }

    @Transactional(readOnly = true)
    public List<KuyuAddPackage> findEndTimeError() {
        return repository.findEndTimeError( );

    }
    @Transactional
    public void flushEndTime(Integer id, Date endTime) {
       repository.flushEndTime(id,endTime);
        
    }

}
