package org.com.coolfish.common.spi.controller;

import org.com.coolfish.common.model.CMCCOperator;
import org.com.coolfish.common.model.DisabledBean;
import org.com.coolfish.common.model.UtilBean;
import org.com.coolfish.common.spi.service.CMCCSwitchService;
import org.com.coolfish.common.spi.service.GetKeyService;
import org.com.coolfish.common.webinterface.service.CMCCRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("cmcc")
public class CMCCController {
    @Autowired
    private CMCCRequestService requestService;

    @Autowired
    private CMCCSwitchService cmccSwitchService;

   
    @Autowired
    private GetKeyService getKey;

    // 流量使用量查询
    @RequestMapping(value = "queryTraffic", method = RequestMethod.POST)
    public UtilBean queryTraffic(@RequestBody UtilBean requestBean) {
        CMCCOperator operator = getKey.getCMCCOperator(requestBean.getOperatorid(), requestBean.getZid());
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
    public UtilBean queryStauts(@RequestBody UtilBean requestBean) {
        CMCCOperator operator = getKey.getCMCCOperator(requestBean.getOperatorid(), requestBean.getZid());
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
        CMCCOperator operator = getKey.getCMCCOperator(requestBean.getOperatorid(), requestBean.getZid());
        if (operator == null) {
            requestBean.setResultCode("-1");
            requestBean.setResultMsg("系统匹配供应商账号错误");
        } else {
            requestBean = cmccSwitchService.HandleDisabled(operator, requestBean);
            ;
        }
        return requestBean;
    }

   
}
