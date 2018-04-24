package org.com.coolfish.service;

import java.math.BigDecimal;
import java.util.List;

import org.com.coolfish.entity.KuyuAddPackage;
import org.com.coolfish.repository.KuyuAddPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KuyuAddPackageService {
    @Autowired
    private KuyuAddPackageRepository repository;

    @Transactional
    public void delete(Integer id) {
        repository.delete(id);
    }

    @Transactional(readOnly = true)
    public KuyuAddPackage get(Integer id) {
        return repository.findOne(id);
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
    public double SumFlow(String tel) {
        BigDecimal sum = repository.SumFlow(tel);
        if (sum == null) {
            return 0;
        } else {
            return sum.doubleValue();
        }

    }

    public BigDecimal SumFlowTal(String tel) {
        return repository.SumFlowTal(tel);
    }

}
