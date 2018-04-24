package org.com.coolfish.repository;

import java.util.List;

import org.com.coolfish.entity.KuyuCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KuyuCardRepository extends JpaRepository<KuyuCard, Integer> {
    @Query("select new KuyuCard(e.id,e.tel,e.operatorid) from KuyuCard e where e.operatorid = :operatorid and e.type = 4")
    List<KuyuCard> findInA(@Param("operatorid") Integer operatorid);
}
