package org.com.coolfish.common.database.repository;

import org.com.coolfish.common.database.entity.KuyuPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KuyuPackageRepository extends JpaRepository<KuyuPackage, Integer> {
    /*
     *  查找套餐包的使用时间
     */
    @Query("select  e.usetime  from KuyuPackage e where e.id =:id")
    Integer findUsetime(@Param("id") Integer id);
 
}
