package org.com.coolfish.common.database.repository;

import java.util.Date;

import org.com.coolfish.common.database.entity.KuyuFlowDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KuyuFlowDetailRepository extends JpaRepository<KuyuFlowDetail, Integer> {

    @Query("select max(e.time) from KuyuFlowDetail e where e.tel =:tel")
    Date findMaxSaveTime(@Param("tel") String tel);
    @Query("select new KuyuFlowDetail(e.apiflow,e.time) from KuyuFlowDetail e where e.cardid =:cardid and e.time=(select max(del.time) from KuyuFlowDetail del)")
    KuyuFlowDetail findLastRecord(@Param("cardid") Integer cardid);
}