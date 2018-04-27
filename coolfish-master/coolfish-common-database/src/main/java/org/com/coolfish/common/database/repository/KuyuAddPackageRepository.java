package org.com.coolfish.common.database.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.com.coolfish.common.database.entity.KuyuAddPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KuyuAddPackageRepository extends JpaRepository<KuyuAddPackage, Integer> {
     @Query("select sum(e.sumflow) from KuyuAddPackage e where e.card =:card and e.status=2 and e.type in(1,4)")
     BigDecimal SumFlow(@Param("card") String card);
     @Query("select sum(e.sumflow) from KuyuAddPackage e where e.card =:card and e.endtime>now() and e.status=2 and e.type in(1,4)")
     BigDecimal SumFlowTal(@Param("card") String card);
     @Query("select new KuyuAddPackage (e.id,e.card,e.addtime,e.starttime,e.endtime,e.cardId)from KuyuAddPackage e where e.card =:card")
     List<KuyuAddPackage> findFlashObject(@Param("card") String card);
     
     @Modifying
     @Query("update KuyuAddPackage e set e.starttime = now() , e.endtime = :endtime where e.id=:id")
     void flashSlientTime(@Param("endtime") Date endtime,@Param("id") Integer card); 
}
