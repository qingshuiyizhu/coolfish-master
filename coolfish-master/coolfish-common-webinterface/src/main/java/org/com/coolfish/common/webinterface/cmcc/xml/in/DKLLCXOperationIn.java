package org.com.coolfish.common.webinterface.cmcc.xml.in;

import org.com.coolfish.common.webinterface.cmcc.xml.content.DKLLCXContent;

/*
 * 物联网单卡实时流量查询 报文
 */
public class DKLLCXOperationIn extends OperationIn {
    private DKLLCXContent content;

    public DKLLCXContent getContent() {
        return content;
    }

    public void setContent(DKLLCXContent content) {
        this.content = content;
    }

}
