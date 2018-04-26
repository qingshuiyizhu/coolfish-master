package org.com.coolfish.common.database.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.com.coolfish.common.database.entity.KuyuAddPackage;
import org.com.coolfish.common.database.entity.KuyuCard;
import org.com.coolfish.common.database.entity.KuyuCardSimstate;
import org.com.coolfish.common.database.entity.KuyuFlowDetail;
import org.com.coolfish.common.message.MsisdnMessage;
import org.com.coolfish.common.util.DecimalTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ComDBService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private KuyuAddPackageService kuyuAddPackageService;

    @Autowired
    private KuyuCardSimstateService kuyuCardSimstateService;

    @Autowired
    private KuyuFlowDetailService kuyuFlowDetailService;

    // 记录每日流量使用量
    public void EveryDayLogWriter(MsisdnMessage message, double realApifow) {
        KuyuFlowDetail flowDetail = kuyuFlowDetailService.findLastRecord(message.getTel());
        logger.info("号码[{}]记录每日流量使用量,找到KuyuFlowDetail表中最新记录：{}", message.getTel(), flowDetail.toString());
        if (flowDetail != null) {
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
                kuyuFlowDetail.setTel(message.getTel());
                kuyuFlowDetail.setCardid(message.getCardid());
                kuyuFlowDetail.setTime(new Date());
                kuyuFlowDetail.setApiflow(new BigDecimal(Double.toString(realApifow)));
                kuyuFlowDetail.setDayflow(new BigDecimal(Double.toString(dayFlow)));
                // 切换到该套餐到现在的总使用量
                kuyuFlowDetail.setUseflow(new BigDecimal(message.getUseflow()));
                logger.info("号码[{}]记录每日流量使用量,KuyuFlowDetail表中最新插入记录：{}", message.getTel(),
                        kuyuFlowDetail.toString());
                kuyuFlowDetailService.save(kuyuFlowDetail);
            }
        }
        // kuyuCardService.flashUseFlow(message.getCardid(), message.getUseflow(),
        // message.getSumflow());
        KuyuCard kuyuCard = kuyuCardService.get(message.getCardid());
        logger.info("号码[{}]记录每日流量使用量,更新前记录：{}", message.getTel(), kuyuCard.toString());
        if (kuyuCard != null) {
            kuyuCard.setSumflow(new BigDecimal(message.getSumflow()));
            kuyuCard.setUseflow(new BigDecimal(message.getUseflow()));
            kuyuCardService.save(kuyuCard);
        }
    }

    // 停机成功 ，更新到数据库,并记录停机日志
    public void updateMsisdnSatus(MsisdnMessage message) {
        kuyuCardService.editStatus(4, message.getTel());
        KuyuCardSimstate simsate = new KuyuCardSimstate();
        simsate.setAddtime(new Date());
        simsate.setCardId(message.getCardid());
        simsate.setCard(message.getTel());
        simsate.setType(1);
        simsate.setResult(String.valueOf(0));
        simsate.setStatus(2);
        simsate.setOperatorName("流量监控停机");

        kuyuCardSimstateService.save(simsate);
    }

    public void flashSlientStatus(String tel) {
        List<KuyuAddPackage> addPackages = kuyuAddPackageService.findFlashObject(tel);
        logger.info("号码[{}]沉默期订购套餐个数[{}]",tel,addPackages.size());
        KuyuAddPackage addpackage = null;
        for (int i = 0; i < addPackages.size(); i++) {
             addpackage = addPackages.get(i);
            logger.info("号码[{}]沉默期套餐修改前数据：{}",tel,addpackage );
            long addEndTime = addpackage.getEndtime().getTime()
                    + (new Date().getTime() - addpackage.getAddtime().getTime());
            addpackage.setStarttime(new Date());
            addpackage.setEndtime(new Date(addEndTime));
            kuyuAddPackageService.save(addpackage);
            logger.info("号码[{}]沉默期套餐修改后数据：{}",tel,addpackage );
        }
    }

}
