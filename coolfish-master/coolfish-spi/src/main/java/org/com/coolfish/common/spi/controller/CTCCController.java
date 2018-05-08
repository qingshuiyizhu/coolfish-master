package org.com.coolfish.common.spi.controller;

import org.com.coolfish.common.model.CTCCOperator;
import org.com.coolfish.common.model.DisabledBean;
import org.com.coolfish.common.model.UtilBean;
import org.com.coolfish.common.spi.service.CTCCSwitchService;
import org.com.coolfish.common.spi.service.GetKeyService;
import org.com.coolfish.common.webinterface.service.CTCCResquestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("ctcc")
public class CTCCController {

    @Autowired
    private CTCCResquestService requestService;

    @Autowired
    private CTCCSwitchService ctccSwitchService;

    @Autowired
    private GetKeyService getKey;

    // 查询流量
    @RequestMapping(value = "queryTraffic", method = RequestMethod.POST)
    public UtilBean queryTraffic(@RequestBody UtilBean requestBean) {
        CTCCOperator operator = getKey.getCTCCOperator(requestBean.getOperatorid(), requestBean.getZid());

        if (operator == null) {
            requestBean.setResultCode("-1");
            requestBean.setResultMsg("系统匹配供应商账号错误");
        } else {
            requestBean = requestService.queryTraffic(operator, requestBean);
        }
        return requestBean;
    }

    // 查询状态
    @RequestMapping(value = "queryStatus", method = RequestMethod.POST)
    public UtilBean queryStatus(@RequestBody UtilBean requestBean) {
        CTCCOperator operator = getKey.getCTCCOperator(requestBean.getOperatorid(), requestBean.getZid());
        if (operator == null) {
            requestBean.setResultCode("-1");
            requestBean.setResultMsg("系统匹配供应商账号错误");
        } else {
            requestBean = requestService.queryStatus(operator, requestBean);
        }
        return requestBean;

    }

    // 停复机
    @RequestMapping(value = "disabledNumber", method = RequestMethod.POST)
    public DisabledBean disabledNumber(@RequestBody DisabledBean requestBean) {
        CTCCOperator operator = getKey.getCTCCOperator(requestBean.getOperatorid(), requestBean.getZid());
        if (operator == null) {
            requestBean.setResultCode("-1");
            requestBean.setResultMsg("系统匹配供应商账号错误");
        } else {
            requestBean = ctccSwitchService.HandleDisabled(operator, requestBean);
        }
        return requestBean;
    }

}
