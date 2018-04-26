package org.com.coolfish.common.database.repository;

import java.util.List;

import org.com.coolfish.common.database.entity.KuyuCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KuyuCardRepository extends JpaRepository<KuyuCard, Integer> {
    @Modifying
    @Query("update KuyuCard e set e.card_status=:status where e.tel=:tel")
    void editStatus(@Param("status") Integer status, @Param("tel") String tel);

    @Modifying
    @Query("update KuyuCard e set e.starttime= now() where e.tel=:tel")
    void upTime(@Param("tel") String tel);

    @Query("select new KuyuCard(e.id,e.operator_type,e.tel,e.operatorid,e.zid,sum(kap.sumflow),e.useflow,e.per,e.card_status,e.type,e.frequency) from KuyuCard e, KuyuAddPackage kap where e.card_type=2 and e.type =1 and kap.endtime>now()  and e.tel=kap.card group by kap.card")
    List<KuyuCard> findCardMessage();
   
    @Query("select new KuyuCard(e.id,e.operator_type,e.tel,e.operatorid,e.zid,e.sumflow,e.useflow,e.per,e.card_status,e.type,e.frequency) from KuyuCard e where e.card_type=2 and e.frequency=1 and e.type = 2 and e.card_status =2")
     List<KuyuCard> findSilentCardMessage();

    /*
     * @Modifying
     * 
     * @Query("update KuyuCard e set e.starttime,e.useflow=:useflow,e.sumflow=:sumflow where e.id=:id"
     * ) void flashUseFlow(@Param("id") Integer id, @Param("useflow") BigDecimal useflow,
     * 
     * @Param("sumflow") BigDecimal sumflow);
     */

}
