package org.com.coolfish.scheduler;

import java.math.BigDecimal;
import java.util.List;

import org.com.coolfish.common.database.entity.KuyuCard;
import org.com.coolfish.common.database.service.KuyuAddPackageService;
import org.com.coolfish.common.database.service.KuyuCardService;
import org.com.coolfish.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StopMonthlyNullSumFlow {

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private KuyuAddPackageService kuyuAddPackageService;

    @Autowired
    private ActionService action;

    public void stopMonthlyNullSumFlow() {
        while (true) {
            stopMonthlyNullSumFlowScheduler();
        }
    }

    public void stopMonthlyNullSumFlowScheduler() {
        log.info("#####执行定时计划任务：执行当月未订购套餐号码查询#####");
        /*
         * 当月未订购套餐的池卡月卡 进行停机
         */
        List<KuyuCard> list = kuyuCardService.findMonthlyNullSumFlow();
        int number = list.size();
        log.info("已订购月套餐的所有号码数量：{}", number);
        for (KuyuCard kuyuCard : list) {
            log.info("剩余未处理号码个数：[{}],正在处理物联网卡ID[{}]数据:{}", number--, kuyuCard.getId(), kuyuCard.toString());
            BigDecimal sumflow = null;
            try {
                sumflow = kuyuAddPackageService.findMonthlyNullSumFlow(kuyuCard.getId());
            } catch (Exception e) {

            }
            if (null == sumflow) {
                // 查询状态，正常，进行停机
                action.stopMonthlyNullSumFlow(kuyuCard);

            }
        }
    }
}
