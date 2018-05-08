package org.com.coolfish.common.database.repository;

import java.math.BigDecimal;
import java.util.List;

import org.com.coolfish.common.database.entity.KuyuCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KuyuCardRepository extends JpaRepository<KuyuCard, Integer> {
    @Modifying
    @Query("update KuyuCard e set e.card_status=:status where e.id=:cardId")
    void editStatus(@Param("status") Integer status, @Param("cardId") Integer cardId);

    @Modifying
    @Query("update KuyuCard e set e.starttime= now() where e.tel=:tel")
    void upTime(@Param("tel") String tel);

    @Query("select new KuyuCard (e.id,e.operator_type,e.tel,e.operatorid,e.zid,sum(kap.sumflow),e.useflow,e.per,e.card_status,e.type,e.frequency) from KuyuCard e, KuyuAddPackage kap where e.card_type=2 and e.type =1 and kap.endtime > now() and kap.starttime < now() and e.id=kap.cardId group by kap.cardId")
    List<KuyuCard> findMonthlyCardMessage();

    @Query("select new KuyuCard (e.id,e.operator_type,e.tel,e.operatorid,e.zid,e.sumflow,e.useflow,e.per,e.card_status,e.type,e.frequency) from KuyuCard e where e.card_type=2 and e.frequency=1 and e.type = 2 and e.card_status =2")
    List<KuyuCard> findSilentCardMessage();

    @Query("select new KuyuCard (e.id,e.operator_type,e.tel,e.operatorid,e.zid,e.sumflow,e.useflow,e.per,e.card_status,e.type,e.frequency) from KuyuCard e where e.card_type=2  and e.type = 4 and e.operatorid in (:ids)")
    List<KuyuCard> findEmptyCardMessage(@Param("ids") Integer[] ids);

    @Modifying
    @Query("update KuyuCard e set e.useflow =:userFlow ,e.sumflow=:sumFlow where e.id=:cardid")
    void flashDayFlow(@Param("cardid") Integer cardid, @Param("userFlow") BigDecimal userFlow,
            @Param("sumFlow") BigDecimal sumFlow);

    @Modifying
    @Query("update KuyuCard e set e.packageid =:packageid where e.id=:cardid")
    void flashDayFlow(@Param("packageid") Integer packageid, @Param("cardid") Integer cardid);
   
    @Query("select new KuyuCard (e.id,e.operator_type,e.tel,e.operatorid,e.zid,e.sumflow,e.useflow,e.per,e.card_status,e.type,e.frequency) from KuyuCard e where e.card_type=2 ")
    List<KuyuCard> findAllNoSumFlow();
    
    @Query("select new KuyuCard (e.id,e.operator_type,e.tel,e.operatorid,e.zid,e.sumflow,e.useflow,e.per,e.card_status,e.type,e.frequency) from KuyuCard e where e.card_type=2 and e.type=1")
    List<KuyuCard> findMonthlyNullSumFlow();
 

}
