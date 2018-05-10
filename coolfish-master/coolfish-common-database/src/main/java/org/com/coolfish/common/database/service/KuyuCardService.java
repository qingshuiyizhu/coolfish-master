package org.com.coolfish.common.database.service;

import java.math.BigDecimal;
import java.util.List;

import org.com.coolfish.common.database.entity.KuyuCard;
import org.com.coolfish.common.database.repository.KuyuCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KuyuCardService {
    @Autowired
    private KuyuCardRepository repository;

    @Transactional
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public KuyuCard get(Integer id) {
        return repository.getOne(id);
    }

    @Transactional
    public void save(KuyuCard kuyu) {
        repository.saveAndFlush(kuyu);
    }

    @Transactional(readOnly = true)
    public List<KuyuCard> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void editStatus(Integer status, Integer cardId) {
        repository.editStatus(status, cardId);
    }

    @Transactional(readOnly = true)
    public List<KuyuCard> findMonthlyCardMessage() {
        return repository.findMonthlyCardMessage();
    }
 

    @Transactional(readOnly = true)
    public List<KuyuCard> findSilentCardMessage() {
        return repository.findSilentCardMessage();
    }

    @Transactional(readOnly = true)
    public List<KuyuCard> findEmptyCardMessage(Integer[] ids) {
        return repository.findEmptyCardMessage(ids);
    }
   
    @Transactional
    public void flashFlows(Integer id, BigDecimal userFlow, BigDecimal sumFlow) {
        repository.flashFlows(id, userFlow, sumFlow);

    } 
   
    @Transactional(readOnly = true)
    public List<KuyuCard> findMonthlyNullSumFlow() {
         
        return repository.findMonthlyNullSumFlow();
    }

}
