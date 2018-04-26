package org.com.coolfish.common.database.service;

import java.util.List;

import org.com.coolfish.common.database.entity.KuyuCardSimstate;
import org.com.coolfish.common.database.repository.KuyuCardSimstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KuyuCardSimstateService {
    @Autowired
    private KuyuCardSimstateRepository repository;

    @Transactional
    public void delete(Integer id) {
        repository.delete(id);
    }

    @Transactional(readOnly = true)
    public KuyuCardSimstate get(Integer id) {
        return repository.findOne(id);
    }

    @Transactional
    public void save(KuyuCardSimstate kuyu) {
        repository.saveAndFlush(kuyu);
    }

    @Transactional(readOnly = true)
    public List<KuyuCardSimstate> findAll() {
        return repository.findAll();
    }

    
}
