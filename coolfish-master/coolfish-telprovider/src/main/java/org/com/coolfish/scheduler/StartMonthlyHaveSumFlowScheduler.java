package org.com.coolfish.scheduler;

import java.util.List;

import org.com.coolfish.common.database.entity.KuyuAddPackage;
import org.com.coolfish.common.database.entity.KuyuCard;
import org.com.coolfish.common.database.service.KuyuAddPackageService;
import org.com.coolfish.common.database.service.KuyuCardService;
import org.com.coolfish.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StartMonthlyHaveSumFlowScheduler {

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private KuyuAddPackageService kuyuaddPackageService;

    @Autowired
    private ActionService action;
    /*
     * 当月订购了套餐的，进行开机
     */

    public void startScheduler() {
        while (true) {
            startMonthlyHaveSumFlowScheduler();
        }
    }

    public void startMonthlyHaveSumFlowScheduler() {
        log.info("#####执行订购了当月套餐的号码进行开机任务#####");
        List<KuyuAddPackage> list = kuyuaddPackageService.findMonthlyHaveSumFlow();
        int number = list.size();
        KuyuCard card = null;
        log.info("进行订购了当月套餐的所有号码数量：{}", number);
        for (KuyuAddPackage addpackage : list) {
            log.info("剩余未处理号码个数：[{}],正在处理物联网卡ID[{}]数据:{}", number--, addpackage.getCardId(),
                    addpackage.toString());
       try {
                if (0 < addpackage.getCardId()) {
                    card = kuyuCardService.get(addpackage.getCardId());
                }
                if (card != null) {
                    // 进行开机
                    action.startMonthlyHaveSumFlowScheduler(card);
                }
            } catch (Exception e) {

            }

        }
    }
}
