package org.com.coolfish.common.database.repository;

import java.util.List;

import org.com.coolfish.common.database.entity.KuyuAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KuyuAccountRepository extends JpaRepository<KuyuAccount, Integer> {
    @Query("select new KuyuAccount(e.id,e.text) from KuyuAccount e")
    List<KuyuAccount> findAccount();

    @Query("select new KuyuAccount(e.id,e.text) from KuyuAccount e where id=:id")
    KuyuAccount getOneOperator(@Param("id") Integer id);
}