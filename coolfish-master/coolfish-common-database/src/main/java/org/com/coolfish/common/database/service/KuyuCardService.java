package org.com.coolfish.common.database.service;

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

    @Transactional
    public void editStatus(Integer status, String tel) {
        repository.editStatus(status, tel);
    }

    @Transactional(readOnly = true)
    public List<KuyuCard> findCardMessage() {
        return repository.findCardMessage();
    }

    @Transactional(readOnly = true)
    public void flashUseFlow(Integer id, String useflow, String sumflow) {
        // repository.flashUseFlow(id, new BigDecimal(useflow), new BigDecimal(sumflow));

    }

    @Transactional(readOnly = true)
    public List<KuyuCard> findSilentCardMessage() {
        return repository.findSilentCardMessage();
    }

    public List<KuyuCard> findEmptyCardMessage(Integer[] ids) {
         return repository.findEmptyCardMessage(ids);
    }

}
