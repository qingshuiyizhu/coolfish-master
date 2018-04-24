package org.com.coolfish.cmcc.service;

import java.util.List;

import org.com.coolfish.cmcc.entity.KuyuCard;
import org.com.coolfish.cmcc.repository.KuyuCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KuyuCardService {
    @Autowired
    private KuyuCardRepository repository;

    @Transactional
    public void delete(Integer id) {
        repository.delete(id);
    }

    @Transactional(readOnly = true)
    public KuyuCard get(Integer id) {
        return repository.findOne(id);
    }

    @Transactional
    public void save(KuyuCard kuyu) {
        repository.saveAndFlush(kuyu);
    }

    @Transactional(readOnly = true)
    public List<KuyuCard> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<KuyuCard> findInA(Integer operatorid) {
        return repository.findInA(operatorid);
    } 

}