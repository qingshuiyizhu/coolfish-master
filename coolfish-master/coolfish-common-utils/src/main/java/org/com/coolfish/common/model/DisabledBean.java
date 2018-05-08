package org.com.coolfish.common.model;

import java.io.Serializable;

//停复机Bean
public class DisabledBean extends UtilBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    
    // 1：停机 2：复机
    private String oprtype;

    // 1. 沉默期内号码停机 2. 判断某个号码套餐用完后停机或资源超量停机 3. 实名审核失败后停机 4. 发现机卡分离时停机或者机卡匹配异常停机 5．位置异常停机 6. 其他情况下停机
    // 7．复机。
    private String reason;

    public String getOprtype() {
        return oprtype;
    }

    public void setOprtype(String oprtype) {
        this.oprtype = oprtype;

    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
