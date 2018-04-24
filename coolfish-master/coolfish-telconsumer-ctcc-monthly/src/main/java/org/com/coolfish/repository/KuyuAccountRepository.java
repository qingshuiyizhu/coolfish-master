package org.com.coolfish.repository;

import java.util.List;

import org.com.coolfish.entity.KuyuAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KuyuAccountRepository extends JpaRepository<KuyuAccount, Integer> {
    @Query("select new KuyuAccount(e.id,e.text) from KuyuAccount e")
    List<KuyuAccount> findAccount();
}
