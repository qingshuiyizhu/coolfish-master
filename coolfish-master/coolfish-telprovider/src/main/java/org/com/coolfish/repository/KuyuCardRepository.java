package org.com.coolfish.repository;

import java.util.List;

import org.com.coolfish.entity.KuyuCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface KuyuCardRepository extends JpaRepository<KuyuCard, Integer> {
     @Query("select new KuyuCard(e.id,e.operator_type,e.tel,e.operatorid,e.zid,sum(kap.sumflow),e.useflow,e.per,e.card_status,e.type,e.frequency,e.store) from KuyuCard e, KuyuAddPackage kap where e.card_type=2 and e.type in(1,2) and kap.endtime>now() and e.tel=kap.card group by kap.card")
     List<KuyuCard> findCardMessage();
    
     @Modifying
     @Query("update KuyuCard e set e.store=0")
    void flashCardStore();
}
