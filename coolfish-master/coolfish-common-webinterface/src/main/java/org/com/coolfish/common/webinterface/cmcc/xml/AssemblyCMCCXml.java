package org.com.coolfish.common.webinterface.cmcc.xml;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.com.coolfish.common.webinterface.cmcc.xml.content.DKLLCXContent;
import org.com.coolfish.common.webinterface.cmcc.xml.content.DTLLCContent;
import org.com.coolfish.common.webinterface.cmcc.xml.content.QrycycleContent;
import org.com.coolfish.common.webinterface.cmcc.xml.content.QrystatusContent;
import org.com.coolfish.common.webinterface.cmcc.xml.content.QryuserContent;
import org.com.coolfish.common.webinterface.cmcc.xml.content.QryuserinfoContent;
import org.com.coolfish.common.webinterface.cmcc.xml.content.SLLZXJGContent;
import org.com.coolfish.common.webinterface.cmcc.xml.content.SuspendtestContent;
import org.com.coolfish.common.webinterface.cmcc.xml.content.TFJContent;
import org.com.coolfish.common.webinterface.cmcc.xml.content.UserstatuscontrolContent;
import org.com.coolfish.common.webinterface.cmcc.xml.in.DKLLCXOperationIn;
import org.com.coolfish.common.webinterface.cmcc.xml.in.DTLLCOperationIn;
import org.com.coolfish.common.webinterface.cmcc.xml.in.QrycycleOperationIn;
import org.com.coolfish.common.webinterface.cmcc.xml.in.QrystatusOperationIn;
import org.com.coolfish.common.webinterface.cmcc.xml.in.QryuserOperationIn;
import org.com.coolfish.common.webinterface.cmcc.xml.in.QryuserinfoOperationIn;
import org.com.coolfish.common.webinterface.cmcc.xml.in.SLLZXJGOperationIn;
import org.com.coolfish.common.webinterface.cmcc.xml.in.SuspendtestOperationIn;
import org.com.coolfish.common.webinterface.cmcc.xml.in.TFJOperationIn;
import org.com.coolfish.common.webinterface.cmcc.xml.in.UserstatuscontrolOperationIn;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;


/**
 * 
 * @author LINGHUI
 * @desc 中国移动发送数据xml的封装类
 *
 */
@Service
public class AssemblyCMCCXml {
    // xml头部
    private String XML_HEADER = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    /*
     * 10 物联网用户成员号码归属查询
     * 
     * 请求参数 物联网号码msisdn 服务类型service 操作类型 oprtype
     */
    public String UserstatuscontrolQuery(String appId, String token, String groupid, String msisdn,
            String SUB_SERVICE_STATUS, String service, String oprtype) {
        UserstatuscontrolOperationIn operationIn = new UserstatuscontrolOperationIn();
        // 业务功能代码
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        operationIn.setApp_id(appId);
        operationIn.setAccess_token(token);
        // 请求类型
        operationIn.setReq_type("2");
        operationIn.setContent(
                new UserstatuscontrolContent(groupid, msisdn, SUB_SERVICE_STATUS, service, oprtype));
        return XMLString(operationIn, UserstatuscontrolOperationIn.class);
    }

    /*
     * 9 号码归属查询接口报文封装
     * 
     * 请求参数 telnum:号码 req_seq:流水号
     */
    public String qryuserXML(String appId, String token, String telnum, String req_seq) {
        QryuserOperationIn operationIn = new QryuserOperationIn();
        // 业务功能代码
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        operationIn.setApp_id(appId);
        operationIn.setAccess_token(token);
        operationIn.setProcess_code("cc_wlw_qryuser");
        operationIn.setReq_seq(req_seq);
        operationIn.setReq_type("1");
        operationIn.setContent(new QryuserContent(telnum));
        return XMLString(operationIn, QryuserOperationIn.class);
    }

    /*
     * 8 用户状态查询
     * 
     * 请求参数 telnum:号码 req_seq:流水号
     */
    public String queryStatus(String appId, String token, String groupid, String telnum, String req_seq) {
        // 报文
        QrystatusOperationIn operationIn = new QrystatusOperationIn();
        // 业务功能代码
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        operationIn.setProcess_code("cc_wlw_qrystatus");
        operationIn.setApp_id(appId);
        operationIn.setAccess_token(token);
        // 本次调用请求流水号
        operationIn.setReq_seq(req_seq);
        // 请求类型
        operationIn.setReq_type("1");
        operationIn.setContent(new QrystatusContent(groupid, telnum));

        return XMLString(operationIn, QrystatusOperationIn.class);
    }

    /*
     * 7 生命周期查询
     * 
     * 请求参数 telnum:号码 req_seq:流水号
     */
    public String qrycycleXML(String appId, String token, String groupid, String telnum, String req_seq) {
        QrycycleOperationIn operationIn = new QrycycleOperationIn();
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        operationIn.setApp_id(appId);
        operationIn.setAccess_token(token);
        operationIn.setProcess_code("cc_wlw_qrycycle");
        // 本次调用请求流水号
        operationIn.setReq_seq(req_seq);
        // 请求类型
        operationIn.setReq_type("1");
        operationIn.setContent(new QrycycleContent(groupid, telnum));

        return XMLString(operationIn, QrycycleOperationIn.class);
    }

    /*
     * 6 测试期强制结束
     * 
     * 请求参数 telnum:号码
     */
    public String querySuspendtestXML(String appId, String token, String groupid, String telnum) {
        SuspendtestOperationIn operationIn = new SuspendtestOperationIn();
        // 业务功能代码
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        operationIn.setApp_id(appId);
        operationIn.setAccess_token(token);
        operationIn.setProcess_code("cc_wlw_suspendtest");
        // 请求类型
        operationIn.setReq_type("2");
        operationIn.setContent(new SuspendtestContent(groupid, telnum));
        return XMLString(operationIn, SuspendtestOperationIn.class);
    }

    /*
     * 5 查询 物联网用户基础信息及状态
     * 
     * 请求参数 ddr_city:地市编码 msisdn: 物联网号码 req_seq:流水号
     */
    public String qryuserinfoXML(String appId, String token, String groupid, String ddr_city, String msisdn,
            String req_seq) {
        QryuserinfoOperationIn operationIn = new QryuserinfoOperationIn();
        // 业务功能代码
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        operationIn.setApp_id(appId);
        operationIn.setAccess_token(token);
        operationIn.setProcess_code("cc_qryuserinfo");
        operationIn.setReq_seq(req_seq);
        operationIn.setReq_type("1");
        operationIn.setContent(new QryuserinfoContent(ddr_city, groupid, msisdn));
        return XMLString(operationIn, QryuserinfoOperationIn.class);
    }

    /*
     * 4.封装查询受理类执行结果报文
     * 
     * 请求参数 ddr_city ：地市编码 orderid: 订单编号 req_seq:流水号
     */
    public String queryAcceptXML(String appId, String token, String ddr_city, String orderid,
            String req_seq) {
        SLLZXJGOperationIn operationIn = new SLLZXJGOperationIn();
        // 业务功能代码
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        operationIn.setApp_id(appId);
        operationIn.setAccess_token(token);
        operationIn.setProcess_code("cc_wlw_memorderresult");
        // 本次调用请求流水号
        operationIn.setReq_seq(req_seq);
        // 请求类型
        operationIn.setReq_type("1");
        operationIn.setContent(new SLLZXJGContent(ddr_city, orderid));

        return XMLString(operationIn, SLLZXJGOperationIn.class);
    }

    /*
     * 3.封装执行物联网集团成员停复机报文
     * 
     * 请求参数
     */
    public String tfjActionXml(String appId, String token, String groupid, String telnum, String oprtype,String reason) {
        TFJOperationIn operationIn = new TFJOperationIn();
        String req_time = sdf.format(new Date());
        operationIn.setProcess_code("cc_wlw_controlsr");
        operationIn.setReq_time(req_time);
        operationIn.setApp_id(appId);
        operationIn.setAccess_token(token);
        operationIn.setReq_seq(req_time);
        operationIn.setReq_type("2");
        operationIn.setContent(new TFJContent(groupid, telnum, oprtype, reason));
        return XMLString(operationIn, TFJOperationIn.class);
    }

    /*
     * 2.封装 查询物联网卡流量报文
     * 
     */
    public String queryDankaXml(String appId, String token, String service_number, String cycle,String req_seq) {
        DKLLCXOperationIn operationIn = new DKLLCXOperationIn();
        operationIn.setProcess_code("OPEN_QRYINTERNETUSERGPRS");
        operationIn.setContent(new DKLLCXContent(service_number, cycle));
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        operationIn.setReq_seq(req_seq);
      
        operationIn.setApp_id(appId);
        operationIn.setAccess_token(token);
        operationIn.setReq_type("1");
        return XMLString(operationIn, DKLLCXOperationIn.class);
    }

    /*
     * 1.物联网动态流量池实时情况查询 qrytype：查询类型 eccode ：客户编号 cycle:查询月份
     * 
     */
    public String queryPoolInfoXML(String appId, String token, String qrytype, String eccode, String cycle,
            String req_seq) {
        DTLLCOperationIn operationIn = new DTLLCOperationIn();
        operationIn.setProcess_code("OPEN_QRYINTERNETGRPPOOLGPRS");
        operationIn.setContent(new DTLLCContent(qrytype, eccode, cycle));
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);

        operationIn.setReq_seq(req_seq);
        operationIn.setApp_id(appId);
        operationIn.setAccess_token(token);

        operationIn.setReq_type("1");
        return XMLString(operationIn, DTLLCOperationIn.class);

    }

    public String XMLString(Object operationIn, Class<?> clazz) {
        // 创建xStream对象
        XStream xStream = new XStream();
        // 设置别名, 默认会输出全路径
        xStream.alias("operation_in", clazz);
        // 调用toXML 将对象转成字符串
        String s = xStream.toXML(operationIn);
        s = s.replace("__", "_");
        StringBuffer xml = new StringBuffer();
        xml.append(XML_HEADER);
        xml.append(s);
        return xml.toString();

    }
}
