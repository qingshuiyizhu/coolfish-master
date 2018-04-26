package org.com.coolfish.service;

import java.util.List;

import org.com.coolfish.entity.KuyuFlowDetail;
import org.com.coolfish.repository.KuyuFlowDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KuyuFlowDetailService {
    @Autowired
    private KuyuFlowDetailRepository repository;

    @Transactional
    public void delete(Integer id) {
        repository.delete(id);
    }

    @Transactional(readOnly = true)
    public KuyuFlowDetail get(Integer id) {
        return repository.findOne(id);
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
    public KuyuFlowDetail findLastRecord(String tel) {
        return repository.findLastRecord(tel);
    }
   
}
