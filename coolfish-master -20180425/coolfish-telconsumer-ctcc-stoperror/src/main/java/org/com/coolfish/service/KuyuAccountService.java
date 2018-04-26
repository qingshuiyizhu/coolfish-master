package org.com.coolfish.service;

import java.util.List;

import org.com.coolfish.entity.KuyuAccount;
import org.com.coolfish.repository.KuyuAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KuyuAccountService {
    @Autowired
    private KuyuAccountRepository repository;

    @Transactional
    public void delete(Integer id) {
        repository.delete(id);
    }

    @Transactional(readOnly = true)
    public KuyuAccount get(Integer id) {
        return repository.findOne(id);
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
    public KuyuAccount getOneAccuount(Long id) {
        return repository.getOneAccuount(id);
    }

}
