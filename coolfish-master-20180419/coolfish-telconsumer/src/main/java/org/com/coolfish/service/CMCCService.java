package org.com.coolfish.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.com.coolfish.cmcc.vo.in.DKLLCXOperationIn;
import org.com.coolfish.cmcc.vo.in.DTLLCOperationIn;
import org.com.coolfish.cmcc.vo.in.QrycycleOperationIn;
import org.com.coolfish.cmcc.vo.in.QrystatusOperationIn;
import org.com.coolfish.cmcc.vo.in.QryuserOperationIn;
import org.com.coolfish.cmcc.vo.in.QryuserinfoOperationIn;
import org.com.coolfish.cmcc.vo.in.SLLZXJGOperationIn;
import org.com.coolfish.cmcc.vo.in.SuspendtestOperationIn;
import org.com.coolfish.cmcc.vo.in.TFJOperationIn;
import org.com.coolfish.cmcc.vo.in.UserstatuscontrolOperationIn;
import org.com.coolfish.cmcc.vo.in.content.DKLLCXContent;
import org.com.coolfish.cmcc.vo.in.content.DTLLCContent;
import org.com.coolfish.cmcc.vo.in.content.QrycycleContent;
import org.com.coolfish.cmcc.vo.in.content.QrystatusContent;
import org.com.coolfish.cmcc.vo.in.content.QryuserContent;
import org.com.coolfish.cmcc.vo.in.content.QryuserinfoContent;
import org.com.coolfish.cmcc.vo.in.content.SLLZXJGContent;
import org.com.coolfish.cmcc.vo.in.content.SuspendtestContent;
import org.com.coolfish.cmcc.vo.in.content.TFJContent;
import org.com.coolfish.cmcc.vo.in.content.UserstatuscontrolContent;
import org.com.coolfish.config.CMCCParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;
/**
 * 
 * @author LINGHUI
 * @desc 中国移动发送数据xml的封装类
 *
 */
@Service
public class CMCCService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CMCCParam param;


    // xml头部
    private String XML_HEADER = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n";

    /*
     * 10 物联网用户成员号码归属查询
     * 
     * 请求参数 物联网号码msisdn 服务类型service 操作类型 oprtype
     */
    public String UserstatuscontrolQuery(String msisdn, String SUB_SERVICE_STATUS, String service,
            String oprtype) {
        String desc = "物联网用户成员号码归属查询:" + "物联网号码msisdn: " + msisdn + ",SUB_SERVICE_STATUS:"
                + SUB_SERVICE_STATUS + ",服务类型service:" + service + ",操作类型 oprtype:" + oprtype;
        logger.info(desc);
        // 报文
        UserstatuscontrolOperationIn operationIn = new UserstatuscontrolOperationIn();
        // 业务功能代码
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        operationIn.setApp_id(param.getAppid());
        operationIn.setAccess_token(param.getToken());

        // 请求类型
        operationIn.setReq_type("2");
        operationIn.setContent(new UserstatuscontrolContent(param.getGroupid(), msisdn, SUB_SERVICE_STATUS,
                service, oprtype));
        return XMLString(operationIn, desc, UserstatuscontrolOperationIn.class);
    }

    /*
     * 9 物联网用户成员号码归属查询
     * 
     * 请求参数 telnum:号码 req_seq:流水号
     */
    public String QryuserQuery(String telnum, String req_seq) {
        String desc = "物联网用户成员号码归属查询:" + "telnum:号码 " + telnum + ",req_seq:流水号" + req_seq;
        logger.info(desc);
        // 报文
        QryuserOperationIn operationIn = new QryuserOperationIn();
        // 业务功能代码
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        operationIn.setApp_id(param.getAppid());
        operationIn.setAccess_token(param.getToken());
        // 本次调用请求流水号
        operationIn.setReq_seq(req_seq);
        // 请求类型
        operationIn.setReq_type("1");
        operationIn.setContent(new QryuserContent(telnum));
        return XMLString(operationIn, desc, QryuserOperationIn.class);
    }

    /*
     * 8 用户状态查询
     * 
     * 请求参数 telnum:号码 req_seq:流水号
     */
    public String QrystatusQuery(String telnum, String req_seq) throws Exception {
        String desc = "用户状态查询:" + "telnum:号码 " + telnum + ",req_seq:流水号" + req_seq;
        logger.info(desc);
        // 报文
        QrystatusOperationIn operationIn = new QrystatusOperationIn();
        // 业务功能代码
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        operationIn.setApp_id(param.getAppid());
        operationIn.setAccess_token(param.getToken());
        // 本次调用请求流水号
        operationIn.setReq_seq(req_seq);
        // 请求类型
        operationIn.setReq_type("1");
        operationIn.setContent(new QrystatusContent(param.getGroupid(), telnum));

        return XMLString(operationIn, desc, QrystatusOperationIn.class);
    }

    /*
     * 7 生命周期查询
     * 
     * 请求参数 telnum:号码 req_seq:流水号
     */
    public String QrycycleQuery(String telnum, String req_seq) throws Exception {
        String desc = "生命周期查询:" + "telnum:号码 " + telnum + ",req_seq:流水号" + req_seq;
        logger.info(desc);
        // 报文
        QrycycleOperationIn operationIn = new QrycycleOperationIn();
        // 业务功能代码
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        operationIn.setApp_id(param.getAppid());
        operationIn.setAccess_token(param.getToken());
        // 本次调用请求流水号
        operationIn.setReq_seq(req_seq);
        // 请求类型
        operationIn.setReq_type("1");
        operationIn.setContent(new QrycycleContent(param.getGroupid(), telnum));

        return XMLString(operationIn, desc, QrycycleOperationIn.class);
    }

    /*
     * 6 测试期强制结束
     * 
     * 请求参数 telnum:号码
     */
    public String SuspendtestQuery(String telnum) throws Exception {
        String desc = "测试期强制结束:" + " telnum:号码" + telnum;
        logger.info(desc);
        // 报文
        SuspendtestOperationIn operationIn = new SuspendtestOperationIn();
        // 业务功能代码
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        operationIn.setApp_id(param.getAppid());
        operationIn.setAccess_token(param.getToken());
        // 请求类型
        operationIn.setReq_type("2");
        operationIn.setContent(new SuspendtestContent(param.getGroupid(), telnum));

        return XMLString(operationIn, desc, SuspendtestOperationIn.class);
    }

    /*
     * 5 查询 物联网用户基础信息及状态
     * 
     * 请求参数 ddr_city:地市编码 msisdn: 物联网号码 req_seq:流水号
     */
    public String QryuserinfoQuery(String ddr_city, String msisdn, String req_seq) {
        String desc = "查询物联网用户基础信息及状态:" + "ddr_city:地市编码 " + ddr_city + ",物联网号码:" + msisdn + ",req_seq:流水号"
                + req_seq;
        logger.info(desc);
        // 报文
        QryuserinfoOperationIn operationIn = new QryuserinfoOperationIn();
        // 业务功能代码
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        operationIn.setApp_id(param.getAppid());
        operationIn.setAccess_token(param.getToken());
        // 本次调用请求流水号
        operationIn.setReq_seq(req_seq);
        // 请求类型
        operationIn.setReq_type("1");
        operationIn.setContent(new QryuserinfoContent(ddr_city, param.getGroupid(), msisdn));

        return XMLString(operationIn, desc, QryuserinfoOperationIn.class);
    }

    /*
     * 4.受理类执行结果查询
     * 
     * 请求参数 ddr_city ：地市编码 orderid: 订单编号 req_seq:流水号
     */
    public String SLLZXJGQuery(String ddr_city, String orderid, String req_seq) {
        String desc = "受理类执行结果查询:" + "ddr_city:地市编码 " + ddr_city + ",操作类型:" + orderid + ",req_seq:流水号"
                + req_seq;
        logger.info(desc);
        // 报文
        SLLZXJGOperationIn operationIn = new SLLZXJGOperationIn();
        // 业务功能代码
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        operationIn.setApp_id(param.getAppid());
        operationIn.setAccess_token(param.getToken());
        // 本次调用请求流水号
        operationIn.setReq_seq(req_seq);
        // 请求类型
        operationIn.setReq_type("1");
        operationIn.setContent(new SLLZXJGContent(ddr_city, orderid));

        return XMLString(operationIn, desc, SLLZXJGOperationIn.class);
    }

    /*
     * 3.物联网集团成员停复机
     * 
     * 请求参数 service_number ：物联网号码(取值是数字串) cycle: 查询月份 (yyyymmdd) req_seq:流水号
     */
    public String TFJQuery(String telnum, String oprtype, String reason,String appid,String token,String groupid) {
        String desc = "物联网集团成员停复机:" + "号码为：" + telnum + ",操作类型:" + oprtype + ",reason:操作原因:" + reason;
        logger.info(desc);
        // 报文
        TFJOperationIn operationIn = new TFJOperationIn();
        // 业务功能代码
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        operationIn.setApp_id(appid);
        operationIn.setAccess_token(token);
        // 请求类型
        operationIn.setReq_type("2");
        operationIn.setContent(new TFJContent(groupid, telnum, oprtype, reason));

        return XMLString(operationIn, desc, TFJOperationIn.class);
    }
    public String TFJQuery(String telnum, String oprtype, String reason) {
        String desc = "物联网集团成员停复机:" + "号码为：" + telnum + ",操作类型:" + oprtype + ",reason:操作原因:" + reason;
        logger.info(desc);
        // 报文
        TFJOperationIn operationIn = new TFJOperationIn();
        // 业务功能代码
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        operationIn.setApp_id(param.getAppid());
        operationIn.setAccess_token(param.getToken());
        // 请求类型
        operationIn.setReq_type("2");
        operationIn.setContent(new TFJContent(param.getGroupid(), telnum, oprtype, reason));

        return XMLString(operationIn, desc, TFJOperationIn.class);
    }

    /*
     * 2.物联网单卡实时流量查询 实时查询集团成员单卡流量
     * 
     * 查询参数 service_number ：物联网号码(取值是数字串) cycle: 查询月份 (yyyymmdd) req_seq:流水号
     */
    public String DKLLCXQuery(String service_number, String cycle, String req_seq,String appid,String token) {
        String desc = "物联网单卡实时流量查询:" + "查询号码为：" + service_number + ",查询月数为:" + cycle + ",req_seq:请求流水号:"
                + req_seq;
        logger.info(desc);
        // 报文
        DKLLCXOperationIn operationIn = new DKLLCXOperationIn();
        // 业务功能代码
        operationIn.setProcess_code("OPEN_QRYINTERNETUSERGPRS");
        operationIn.setContent(new DKLLCXContent(service_number, cycle));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        // 本次调用请求流水号0_9834345
        operationIn.setReq_seq(req_seq);
        operationIn.setApp_id(appid);
        operationIn.setAccess_token(token);
        // 请求类型
        operationIn.setReq_type("1");
        return XMLString(operationIn, desc, DKLLCXOperationIn.class);
    }
    
    
    public String DKLLCXQuery(String service_number, Integer cycle, String req_seq) {
        String desc = "物联网单卡实时流量查询:" + "查询号码为：" + service_number + ",查询月数为:" + cycle + ",req_seq:请求流水号:"
                + req_seq;
        logger.info(desc);
        // 报文
        DKLLCXOperationIn operationIn = new DKLLCXOperationIn();
        // 业务功能代码
        operationIn.setProcess_code("OPEN_QRYINTERNETUSERGPRS");
        operationIn.setContent(new DKLLCXContent(service_number, String.valueOf(cycle)));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        // 本次调用请求流水号0_9834345
        operationIn.setReq_seq(req_seq);
        operationIn.setApp_id(param.getAppid());
        operationIn.setAccess_token(param.getToken());
        // 请求类型
        operationIn.setReq_type("1");
        return XMLString(operationIn, desc, DKLLCXOperationIn.class);
    }

    /*
     * 1.物联网动态流量池实时情况查询 qrytype：查询类型 eccode ：客户编号 cycle:查询月份
     * 
     */
    public String DTLLCQuery(String qrytype, String eccode, String cycle, String req_seq) {
        String desc = "物联网动态流量池实时情况查询:" + "qrytype：查询类型：" + qrytype + ",eccode ：客户编号:" + eccode
                + ",cycle:查询月份:" + cycle + ",req_seq:请求流水号:" + req_seq;
        logger.info(desc);
        // 报文
        DTLLCOperationIn operationIn = new DTLLCOperationIn();
        // 业务功能代码
        operationIn.setProcess_code("OPEN_QRYINTERNETGRPPOOLGPRS");
        operationIn.setContent(new DTLLCContent(qrytype, eccode, cycle));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String req_time = sdf.format(new Date());
        operationIn.setReq_time(req_time);
        // 本次调用请求流水号0_9834345
        operationIn.setReq_seq(req_seq);
        operationIn.setApp_id(param.getAppid());
        operationIn.setAccess_token(param.getToken());
        // 请求类型
        operationIn.setReq_type("1");
        return XMLString(operationIn, desc, DTLLCOperationIn.class);

    }

    public String XMLString(Object operationIn, String desc, Class<?> clazz) {
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
        logger.info(desc + "发送xml数据为:\n" + xml);
        return xml.toString();

    }
}
