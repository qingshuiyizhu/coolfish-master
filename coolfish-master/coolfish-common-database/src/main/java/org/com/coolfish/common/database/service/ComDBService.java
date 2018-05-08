package org.com.coolfish.common.database.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.com.coolfish.common.database.entity.KuyuAddPackage;
import org.com.coolfish.common.database.entity.KuyuCard;
import org.com.coolfish.common.database.entity.KuyuFlowDetail;
import org.com.coolfish.common.message.MsisdnMessage;
import org.com.coolfish.common.util.DecimalTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class ComDBService {
    @Autowired
    private KuyuCardService kuyuCardService;
   
    @Autowired
    private KuyuAddPackageService kuyuAddPackageService;
 
    @Autowired
    private KuyuFlowDetailService kuyuFlowDetailService;

    // 记录每日流量使用量
    public void EveryDayLogWriter(MsisdnMessage message, double realApifow) {
        KuyuFlowDetail flowDetail = kuyuFlowDetailService.findLastRecord(message.getCardid());
        if (flowDetail != null) {
            log.info("物联网卡ID[{}]记录每日流量使用量,找到KuyuFlowDetail表中最新记录：{}", message.getCardid(), flowDetail.toString());
            Date now = new Date();
            long nd = 1000 * 20 * 60 * 60;// 20个小时的毫秒
            // 计算数据库最新记录和现在相隔的时间
            long divResult = (now.getTime() - (flowDetail.getTime().getTime())) - nd;
            String userflow = "0";
            try {
                userflow = String.valueOf(flowDetail.getUseflow());
            } catch (Exception e) {
            }
            double dayFlow = DecimalTools.sub(message.getUseflow(), userflow);
            if (divResult > 0) {
                KuyuFlowDetail kuyuFlowDetail = new KuyuFlowDetail();
                kuyuFlowDetail.setTel(message.getIphone());
                kuyuFlowDetail.setCardid(message.getCardid());
                kuyuFlowDetail.setTime(new Date());
                kuyuFlowDetail.setApiflow(new BigDecimal(Double.toString(realApifow)));
                kuyuFlowDetail.setDayflow(new BigDecimal(Double.toString(dayFlow)));
                // 切换到该套餐到现在的总使用量
                kuyuFlowDetail.setUseflow(new BigDecimal(message.getUseflow()));
                log.info("物联网卡ID[{}]记录每日流量使用量,KuyuFlowDetail表中最新插入记录：{}", message.getCardid(),
                        kuyuFlowDetail.toString());
                kuyuFlowDetailService.save(kuyuFlowDetail);
            }
        }
     
        KuyuCard kuyuCard = kuyuCardService.get(message.getCardid());
        log.info("物联网卡ID[{}]记录每日流量使用量,更新前记录：{}", message.getCardid(), kuyuCard.toString());
        if (kuyuCard != null) {
            kuyuCard.setSumflow(new BigDecimal(message.getSumflow()));
            kuyuCard.setUseflow(new BigDecimal(message.getUseflow()));
            kuyuCardService.flashDayFlow(message.getCardid(), new BigDecimal(message.getUseflow()),
                    new BigDecimal(message.getSumflow()));
        }
    }

     

    public void flashSlientStatus(String tel) {
        List<KuyuAddPackage> addPackages = kuyuAddPackageService.findFlashObject(tel);
        log.info("号码[{}]沉默期订购套餐个数[{}]", tel, addPackages.size());
        for (KuyuAddPackage kuyuAddPackage : addPackages) {
            if (null != kuyuAddPackage.getEndtime()) {
                Date start = null;
                if (null != kuyuAddPackage.getAddtime()) {
                    start = kuyuAddPackage.getAddtime();
                } else {
                    start = kuyuAddPackage.getStarttime();
                }
                if (null != start) {
                    log.info("号码[{}]沉默期套餐修改前数据：{}", tel, kuyuAddPackage.toString());
                    kuyuAddPackage
                            .setEndtime(new Date(start.getTime() + kuyuAddPackage.getEndtime().getTime()));
                    kuyuAddPackage.setStarttime(new Date());
                    log.info("号码[{}]沉默期套餐修改后数据：{}", tel, kuyuAddPackage.toString());
                    kuyuAddPackageService.flashSilentTime(kuyuAddPackage.getEndtime(),
                            kuyuAddPackage.getId());

                } else {
                    log.error("号码[{}]套餐id=[{}]没有套餐添加时间或者开始时间，无法处理沉默期：{}", tel, kuyuAddPackage.getId(),
                            kuyuAddPackage.toString());
                }

            } else {
                log.error("号码[{}]套餐id=[{}]没有结束套餐时间，无法处理沉默期：{}", tel, kuyuAddPackage.getId(),
                        kuyuAddPackage.toString());
            }

        }

    }
}
