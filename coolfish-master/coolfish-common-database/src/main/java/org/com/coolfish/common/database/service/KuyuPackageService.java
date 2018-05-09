package org.com.coolfish.common.database.service;

import org.com.coolfish.common.database.repository.KuyuPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KuyuPackageService {
    @Autowired
    private KuyuPackageRepository repository;
 
    @Transactional(readOnly = true)
    public Integer findUsetime(Integer id) {
        return repository.findUsetime(id);
    }

}
