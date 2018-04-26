package org.com.coolfish.service;

import java.util.List;

import org.com.coolfish.entity.KuyuOperator;
import org.com.coolfish.repository.KuyuOperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KuyuOperatorService {
    @Autowired
    private KuyuOperatorRepository repository;

    @Transactional
    public void delete(Integer id) {
        repository.delete(id);
    }

    @Transactional(readOnly = true)
    public KuyuOperator get(Integer id) {
        return repository.findOne(id);
    }

    @Transactional
    public void save(KuyuOperator kuyu) {
        repository.saveAndFlush(kuyu);
    }

    @Transactional(readOnly = true)
    public List<KuyuOperator> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<KuyuOperator> findAccount() {
        return repository.findAccount();
    }

    @Transactional(readOnly = true)
    public KuyuOperator getOneOperator(Long id) {
        return repository.getOneOperator(id);
    }

}
