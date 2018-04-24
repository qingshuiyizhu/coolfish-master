package org.com.coolfish.entity;

public class MqJson {
    private String tel;

    // 对应的kuyu_operacor的主键ID
    private Integer operatorid;

    // 账号id
    private Integer zid;

    // 总流量
    private Double sumflow;

    // 已用流量
    private Double useflow;

    // 流量显示比例
    private Double per;

    // 重试次数
    private Integer retry = 0;

    // 是否存入数据库  否为0
    private Integer store = 0;
    // 是否处于沉默期 0 不是 1是
    private Integer slient =0;
    public MqJson(String tel, Integer operatorid, Integer zid, Double sumflow, Double useflow, Double per,Integer slient) {
        super();
        this.tel = tel;
        this.operatorid = operatorid;
        this.zid = zid;
        this.sumflow = sumflow;
        this.useflow = useflow;
        this.per = per;
        this.slient = slient;
    }

}
