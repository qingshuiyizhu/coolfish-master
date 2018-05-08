package org.com.coolfish.common.spi.service;

import java.util.Date;

import org.com.coolfish.common.database.entity.KuyuCardSimstate;
import org.com.coolfish.common.database.service.KuyuCardService;
import org.com.coolfish.common.database.service.KuyuCardSimstateService;
import org.com.coolfish.common.model.CMCCOperator;
import org.com.coolfish.common.model.DisabledBean;
import org.com.coolfish.common.webinterface.service.CMCCRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author LINGHUI
 * @desc 中国移动http 请求API
 */
@Service
@Slf4j
public class CMCCSwitchService {

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private KuyuCardSimstateService kuyuCardSimstateService;

    @Autowired
    private CMCCRequestService requestService;
 

    public DisabledBean HandleDisabled(CMCCOperator operator, DisabledBean disabledBean) {
        log.info("移动物联网卡ID[{}]:[{}],操作类型[{}]", disabledBean.getCardId(), disabledBean.getOperatorType());
        disabledBean = requestService.disabledNumber(operator, disabledBean);

        if ("-1".equals(disabledBean.getResultCode())) {
            log.warn("电信物联网卡ID[{}]:[{}],解析返回消息失败", disabledBean.getCardId());
            disabledBean.setResultCode("-1");
            disabledBean.setResultMsg("解析返回消息失败");
            return disabledBean;
        }

        KuyuCardSimstate simsate = new KuyuCardSimstate();
        simsate.setAddtime(new Date());
        simsate.setCard(disabledBean.getIphone());
        simsate.setDid(0);
        simsate.setUid(50);
        simsate.setCardId(disabledBean.getCardId());
        simsate.setOperatorName(operator.getOperator_name());// 操作者
        simsate.setRemarks(disabledBean.getActionReason());// 操作原因
        simsate.setResult(disabledBean.getResultMsg());
        simsate.setSerialNumber(disabledBean.getSerialNumber());
        simsate.setSource(disabledBean.getSource());// 来源
        // 1：停机 2：复机
        // 停机成功 ，更新到数据库,并记录停机日志
        if ("1".equals(disabledBean.getOprtype())) {
            int exist = disabledBean.getResultMsg().indexOf("停机状态的用户不允许再停机");
            // 0 :成功
            if ("0".equals(disabledBean.getResultCode())) {
                log.info("移动物联网卡ID[{}],操作类型[{}],停机成功,更新状态和记录日志", disabledBean.getCardId(),
                        disabledBean.getOperatorType());
                simsate.setStatus(2);
                simsate.setType(1);
               
                kuyuCardService.editStatus(4, disabledBean.getCardId());
            } else if (exist != -1) {
                // 已经存在停机记录
                log.info("移动物联网卡ID[{}],操作类型[{}],已经停机,更新状态,记录日志", disabledBean.getCardId(),
                        disabledBean.getOperatorType());
                simsate.setStatus(1);
                simsate.setType(1);
                kuyuCardService.editStatus(4, disabledBean.getCardId());

            } else {
                log.info("移动物联网卡ID[{}],操作类型[{}],停机失败,更新状态,记录日志", disabledBean.getCardId(),
                        disabledBean.getOperatorType());
                simsate.setStatus(1);
                simsate.setType(1);
                kuyuCardService.editStatus(2, disabledBean.getCardId());
            }

        } else if ("2".equals(disabledBean.getOprtype())) {
            if ("0".equals(disabledBean.getResultCode())) {
                // 复机成功 ，更新到数据库,并记录停机日志
                log.info("移动物联网卡ID[{}],操作类型[{}],复机成功,更新状态和记录日志", disabledBean.getCardId(),
                        disabledBean.getOperatorType());

                simsate.setStatus(2);
                simsate.setType(2);
                kuyuCardService.editStatus(2, disabledBean.getCardId());
            } else {
                // 复机失败,记录复机日志
                log.info("移动物联网卡ID[{}],操作类型[{}],复机失败,不更新状态,记录日志", disabledBean.getCardId(),
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
