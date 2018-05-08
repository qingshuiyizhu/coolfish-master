package org.com.coolfish.common.database.service;

import java.util.List;

import org.com.coolfish.common.database.entity.KuyuAccount;
import org.com.coolfish.common.database.repository.KuyuAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KuyuAccountService {
    @Autowired
    private KuyuAccountRepository repository;

    @Transactional
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public KuyuAccount get(Integer id) {
        return repository.getOne(id);
    }

    @Transactional
    public void save(KuyuAccount kuyu) {
        repository.saveAndFlush(kuyu);
    }

    @Transactional(readOnly = true)
    public List<KuyuAccount> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<KuyuAccount> findAccount() {
        return repository.findAccount();
    }

    @Transactional(readOnly = true)
    public KuyuAccount getOneAccuount(Integer id) {
        return repository.getOneAccuount(id);
    }

}
