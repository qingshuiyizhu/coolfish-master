package org.com.coolfish.common.database.service;

import java.util.List;

import org.com.coolfish.common.database.entity.KuyuFlowDetail;
import org.com.coolfish.common.database.repository.KuyuFlowDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KuyuFlowDetailService {
    @Autowired
    private KuyuFlowDetailRepository repository;

    @Transactional
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public KuyuFlowDetail get(Integer id) {
        return repository.getOne(id);
    }

    @Transactional
    public void save(KuyuFlowDetail kuyu) {
        repository.saveAndFlush(kuyu);
    }

    @Transactional(readOnly = true)
    public List<KuyuFlowDetail> findAll() {
        return repository.findAll();
    }
   

    @Transactional(readOnly = true)
    public KuyuFlowDetail findLastRecord(Integer cardid) {
        return repository.findLastRecord(cardid);
    }
   

    
}
