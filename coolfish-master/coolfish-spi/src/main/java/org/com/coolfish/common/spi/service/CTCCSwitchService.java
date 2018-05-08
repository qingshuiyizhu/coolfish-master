package org.com.coolfish.common.spi.service;

import java.util.Date;

import org.com.coolfish.common.database.entity.KuyuCardSimstate;
import org.com.coolfish.common.database.service.KuyuCardService;
import org.com.coolfish.common.database.service.KuyuCardSimstateService;
import org.com.coolfish.common.model.CTCCOperator;
import org.com.coolfish.common.model.DisabledBean;
import org.com.coolfish.common.webinterface.service.CTCCResquestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/***
 * 
 * @author LINGHUI
 * @desc 中国电信的http get 请求API
 *
 */
@Service
@Slf4j
public class CTCCSwitchService {

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private KuyuCardSimstateService kuyuCardSimstateService;

    @Autowired
    private CTCCResquestService requestService;
 

    // 电信停复机
    public DisabledBean HandleDisabled(CTCCOperator operator, DisabledBean disabledBean) {
        log.info("电信物联网卡ID[{}]:[{}],操作类型[{}]", disabledBean.getCardId(), disabledBean.getOperatorType());
        disabledBean = requestService.disabledNumber(operator, disabledBean);
        if ("-1".equals(disabledBean.getResultCode())) {
            log.warn("电信物联网卡ID[{}]:[{}],解析返回消息失败", disabledBean.getCardId());
            disabledBean.setResultCode("-1");
            disabledBean.setResultMsg("解析返回消息失败");
            return disabledBean;
        }

        // 更新状态到redis上

        String card = disabledBean.getIphone() != null ? disabledBean.getIphone() : disabledBean.getIccid();
        KuyuCardSimstate simsate = new KuyuCardSimstate();
        simsate.setAddtime(new Date());
        simsate.setCard(card);
        simsate.setDid(0);
        simsate.setUid(50);
        simsate.setCardId(disabledBean.getCardId());
        simsate.setOperatorName(operator.getOperator_name());
        simsate.setRemarks(disabledBean.getActionReason());
        simsate.setResult(disabledBean.getResultMsg());
        simsate.setSerialNumber(disabledBean.getSerialNumber());
        simsate.setSource(disabledBean.getSource());
        // 1：停机 2：复机
        // 停机成功 ，更新到数据库,并记录停机日志
        if ("1".equals(disabledBean.getOprtype())) {

            int exist = disabledBean.getResultMsg().indexOf("已存在停机");
            if ("0".equals(disabledBean.getResultCode())) {
                log.info("电信物联网卡ID[{}],操作类型[{}],停机成功，更新状态和记录日志", disabledBean.getCardId(),
                        disabledBean.getOperatorType());

                // 停机成功 ，删除Redis数据，更新到数据库,并记录停机日志
                simsate.setStatus(2);
                simsate.setType(1);
                kuyuCardService.editStatus(4, disabledBean.getCardId());
            } else if ((exist != -1)) {
                log.info("电信物联网卡ID[{}],操作类型[{}],已经存在停机记录，更新状态和记录日志", disabledBean.getCardId(),
                        disabledBean.getOperatorType());
                // 已经存在停机记录
                simsate.setStatus(1);
                simsate.setType(1);
                kuyuCardService.editStatus(4, disabledBean.getCardId());

            } else {
                log.warn("电信物联网卡ID[{}],操作类型[{}],停机失败，更新状态，记录日志", disabledBean.getCardId(),
                        disabledBean.getOperatorType());

                // 停机失败,记录停机日志
                simsate.setStatus(1);
                simsate.setType(1);
                kuyuCardService.editStatus(2, disabledBean.getCardId());
            }
        } else if ("2".equals(disabledBean.getOprtype())) {
            if ("0".equals(disabledBean.getResultCode())) {
                log.info("电信物联网卡ID[{}],操作类型[{}],复机成功，更新状态和记录日志", disabledBean.getCardId(),
                        disabledBean.getOperatorType());
                simsate.setStatus(2);
                simsate.setType(2);
                kuyuCardService.editStatus(2, disabledBean.getCardId());
            } else {
                // 复机失败,记录复机日志
                log.info("电信物联网卡ID[{}],操作类型[{}],复机失败，更新状态，记录日志", disabledBean.getCardId(),
                        disabledBean.getOperatorType());
                simsate.setStatus(1);
                simsate.setType(2);
                kuyuCardService.editStatus(4, disabledBean.getCardId());
            }
        }
        kuyuCardSimstateService.save(simsate);
        return disabledBean;
    }

}
