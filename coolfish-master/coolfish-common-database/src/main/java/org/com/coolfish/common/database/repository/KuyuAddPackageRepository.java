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
    /*
     * 需要刷新沉默期的套餐
     */
    @Query("from KuyuAddPackage e where e.cardId =:cardId")
    List<KuyuAddPackage> findFlashObject(@Param("cardId") Integer cardId);

    /*
     * 刷新沉默期套餐开始时间和结束时间
     * 
     */
    @Modifying
    @Query("update KuyuAddPackage e set e.starttime = :starttime , e.endtime = :endtime where e.id=:id")
    void flashSlientTime(@Param("starttime") Date starttime, @Param("endtime") Date endtime, @Param("id") Integer id);

    @Query("select new KuyuAddPackage (e.id,e.card,e.addtime,e.starttime,e.endtime,e.cardId) from KuyuAddPackage e WHERE e.uid = 50 AND e.endtime LIKE '2018-05-31%' AND e.status =2 AND e.type =1 and e.starttime BETWEEN '2018-05-01 00:00:00' AND now()")
    List<KuyuAddPackage> findMonthlyHaveSumFlow();

    /*
     * 查找当月套餐的总流量
     */
    @Query("select sum(e.sumflow) from KuyuAddPackage e where e.cardId =:cardId and e.starttime < now() and e.endtime >now() and e.status= 2 and e.type in (1,3)")
    BigDecimal findMonthlySumFlows(@Param("cardId")Integer cardId);

    /*
     * 查询沉默期处理日期错误的日志
     */
    @Query("from KuyuAddPackage e WHERE e.endtime > :endtime ")
    List<KuyuAddPackage> findEndTimeError(@Param("endtime") Date endtime);

    @Modifying
    @Query("update KuyuAddPackage e set e.endtime = :endtime where e.id=:id")
    void flushEndTime(@Param("id") Integer id, @Param("endtime") Date endTime);
}
