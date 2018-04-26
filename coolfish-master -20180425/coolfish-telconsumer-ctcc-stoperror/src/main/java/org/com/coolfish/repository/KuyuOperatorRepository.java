package org.com.coolfish.repository;

import java.util.List;

import org.com.coolfish.entity.KuyuOperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KuyuOperatorRepository extends JpaRepository<KuyuOperator, Integer> {
    @Query("select new KuyuOperator(e.id,e.text) from KuyuOperator e")
    List<KuyuOperator> findAccount();

    @Query("select new KuyuOperator(e.id,e.text) from KuyuOperator e where id=:id")
    KuyuOperator getOneOperator(@Param("id") Long id);
}
