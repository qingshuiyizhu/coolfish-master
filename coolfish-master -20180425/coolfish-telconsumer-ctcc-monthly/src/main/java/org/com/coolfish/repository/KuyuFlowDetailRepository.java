package org.com.coolfish.repository;

import java.util.Date;

import org.com.coolfish.entity.KuyuFlowDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KuyuFlowDetailRepository extends JpaRepository<KuyuFlowDetail, Integer> {
    @Query("select max(e.time) from KuyuFlowDetail e where e.tel =:tel")
    Date findMaxSaveTime(@Param("tel") String tel);
}