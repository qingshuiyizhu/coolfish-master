package org.com.coolfish.repository;

import java.util.List;

import org.com.coolfish.entity.KuyuOperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KuyuOperatorRepository extends JpaRepository<KuyuOperator, Integer> {
     @Query("select new KuyuOperator(e.id,e.text) from KuyuOperator e")
     List<KuyuOperator> findAccount (); 
}
