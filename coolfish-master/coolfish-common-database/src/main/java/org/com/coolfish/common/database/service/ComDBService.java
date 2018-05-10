package org.com.coolfish.common.database.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

    @Autowired
    private KuyuPackageService kuyuPackageService;

    // 记录每日流量使用量
    public void MonthlyEveryDayLogWriter(MsisdnMessage message) {
        KuyuCard kuyuCard = kuyuCardService.get(message.getCardid());
        log.info("物联网卡ID[{}]记录每日流量使用量,更新前记录：{}", message.getCardid(), kuyuCard.toString());
        if (kuyuCard != null) {
            KuyuFlowDetail kuyuFlowDetail = new KuyuFlowDetail();
            kuyuFlowDetail.setTel(message.getIphone());
            kuyuFlowDetail.setCardid(message.getCardid());
            kuyuFlowDetail.setTime(new Date());
            kuyuFlowDetail.setApiflow(new BigDecimal(message.getUseflow()));
            kuyuFlowDetail.setDayflow(DecimalTools.sub(message.getUseflow(), kuyuCard.getUseflow()));
            // 切换到该套餐到现在的总使用量
            kuyuFlowDetail.setUseflow(new BigDecimal(message.getUseflow()));
            log.info("物联网卡ID[{}]记录每日流量使用量,KuyuFlowDetail表中最新插入记录：{}", message.getCardid(),
                    kuyuFlowDetail.toString());
            kuyuFlowDetailService.save(kuyuFlowDetail);
         
            kuyuCard.setSumflow(new BigDecimal(message.getSumflow()));
            kuyuCard.setUseflow(new BigDecimal(message.getUseflow()));
            kuyuCardService.flashFlows(message.getCardid(), new BigDecimal(message.getUseflow()),
                    new BigDecimal(message.getSumflow()));
        }
    }

    public void flashSlientStatus(Integer cardId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟
        List<KuyuAddPackage> addPackages = kuyuAddPackageService.findFlashObject(cardId);
        log.info("物联网卡ID[{}]沉默期订购套餐个数[{}]", cardId, addPackages.size());
        for (KuyuAddPackage kuyuAddPackage : addPackages) {
            log.info("物联网卡ID[{}]沉默期套餐修改前数据：{}", cardId, kuyuAddPackage.toString());
            if(kuyuAddPackage.getPackageid()!= null && kuyuAddPackage.getPackageid() > 0) {
                Integer usetime = kuyuPackageService.findUsetime(kuyuAddPackage.getPackageid());
                if (usetime != null && usetime > 0) {
                    long apartTime = usetime * 24 * 60 * 60 * 1000L;
                    kuyuAddPackage.setStarttime(new Date());
                    kuyuAddPackage.setEndtime(new Date(kuyuAddPackage.getStarttime().getTime() + apartTime));
                    log.info("物联网卡ID[{}]开始时间[{}],套餐有效天数[{}]结束时间数据[{}]", cardId,  sdf.format(kuyuAddPackage.getStarttime()),
                            usetime,  sdf.format(kuyuAddPackage.getEndtime()));
                    kuyuAddPackageService.flashSilentTime(kuyuAddPackage.getStarttime(),kuyuAddPackage.getEndtime(), kuyuAddPackage.getId());
                }else {
                    log.error("物联网卡ID[{}]处理沉默期出错，套餐包数据使用天数异常",cardId);
                }
  
            }else {
                log.error("物联网卡ID[{}]处理沉默期出错，套餐包id[{}]异常",cardId,kuyuAddPackage.getPackageid());
            }
          
        }
    }
}
