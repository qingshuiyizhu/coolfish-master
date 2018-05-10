package org.com.coolfish.common.database.repository;

import java.math.BigDecimal;
import java.util.List;

import org.com.coolfish.common.database.entity.KuyuCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KuyuCardRepository extends JpaRepository<KuyuCard, Integer> {
    /*
     * 1更新KuyuCard的卡状态
     * 
     */
    @Modifying
    @Query("update KuyuCard e set e.card_status=:status where e.id=:cardId")
    void editStatus(@Param("status") Integer status, @Param("cardId") Integer cardId);

    /*
     * 2查询在用池卡月套餐
     */
   /* @Query("select new KuyuCard (e.id,e.operator_type,e.tel,e.operatorid,e.zid,sum(kap.sumflow),e.useflow,e.per,e.card_status,e.type,e.frequency) from KuyuCard e, KuyuAddPackage kap where e.card_type=2 and e.type =1 and kap.endtime > now() and kap.starttime < now() and e.id=kap.cardId group by kap.cardId")
    List<KuyuCard> findMonthlyCardMessage();    
    
    */
    @Query("select new KuyuCard (e.id,e.operator_type,e.tel,e.operatorid,e.zid,sum(kap.sumflow),e.useflow,e.per,e.card_status,e.type,e.frequency) from KuyuCard e, KuyuAddPackage kap where e.card_type=2 and e.operator_type in (1,2) and e.type =1 and kap.uid = 50 and kap.starttime < now() and kap.endtime > now() and kap.status =2 and kap.type in (1,3) and e.id=kap.cardId group by kap.cardId")
    List<KuyuCard> findMonthlyCardMessage();    
   /*
    * 3查询池卡累计套餐处于沉默期的号码
    */

    @Query("select new KuyuCard (e.id,e.operator_type,e.tel,e.operatorid,e.zid,e.sumflow,e.useflow,e.per,e.card_status,e.type,e.frequency) from KuyuCard e where e.card_type=2 and e.frequency=1 and e.type = 2 and e.card_status =2")
    List<KuyuCard> findSilentCardMessage();

    /*
     * 4查询移动池卡空套餐type=4
     */
    @Query("select new KuyuCard (e.id,e.operator_type,e.tel,e.operatorid,e.zid,e.sumflow,e.useflow,e.per,e.card_status,e.type,e.frequency) from KuyuCard e where e.card_type=2  and e.type = 4 and e.operatorid in (:ids)")
    List<KuyuCard> findEmptyCardMessage(@Param("ids") Integer[] ids);

    // 5每天更新数据库KuyuCard表里的使用流量和总流量
    @Modifying
    @Query("update KuyuCard e set e.useflow =:userFlow ,e.sumflow=:sumFlow where e.id=:id")
    void flashFlows(@Param("id") Integer  id, @Param("userFlow") BigDecimal userFlow,
            @Param("sumFlow") BigDecimal sumFlow);

    /*
     * 6查询所有的池卡月套餐
     */
    @Query("select new KuyuCard (e.id,e.operator_type,e.tel,e.operatorid,e.zid,e.sumflow,e.useflow,e.per,e.card_status,e.type,e.frequency) from KuyuCard e where e.card_type=2 and e.type=1")
    List<KuyuCard> findMonthlyNullSumFlow();

}
