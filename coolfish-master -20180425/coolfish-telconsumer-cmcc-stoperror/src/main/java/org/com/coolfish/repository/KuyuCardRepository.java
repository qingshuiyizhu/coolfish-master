package org.com.coolfish.repository;

import java.util.List;

import org.com.coolfish.entity.KuyuCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KuyuCardRepository extends JpaRepository<KuyuCard, Integer> {
    @Query("select new KuyuCard(e.operator_type,e.tel,e.operatorid,e.zid,e.sumflow,e.useflow,e.per,e.card_status,e.type) from KuyuCard e where e.card_type=2 and e.type in(1,2)")
    List<KuyuCard> findInA();

    @Modifying
    @Query("update KuyuCard e set e.card_status=:status where e.tel=:tel")
    void editStatus(@Param("status") Integer status,@Param("tel") String tel);
   
    @Modifying
    @Query("update KuyuCard e set e.starttime= now() where e.tel=:tel")
    void upTime(@Param("tel")String tel);
}
