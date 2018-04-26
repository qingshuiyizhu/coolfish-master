package org.com.coolfish.util;

import java.math.BigDecimal;

/**
 * 浮点数操作工具
 * 
 * @author qugf
 */
public class DecimalTools {
    /**
     * 默认保留2位小数
     */
    private static final int DEFAULT_SCALE = 2;
    
    /**
     * v1与v2进行比较，0: v1==v2 1: v1>v2 -1:v1<v2
     * @return
     */
    public static int compareTo(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        
        return b1.compareTo(b2);
    }

    /**
     * 精确的加法运算。
     * 
     * @param v1 加数
     * @param v2 被加数
     * @return 两个数的和
     */
    public static String add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).setScale(DEFAULT_SCALE).toPlainString();
    }

    public static String add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).setScale(DEFAULT_SCALE).toPlainString();
    }

    /**
     * 精确的减法运算
     * 
     * @param v1 减数
     * @param v2 被减数
     * @return 两个数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    public static double sub(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 精确的乘法运算
     * 
     * @param v1 乘数
     * @param v2 被乘数
     * @return 两个数的积
     */
    public static String mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).setScale(DEFAULT_SCALE).toPlainString();
    }

    public static String mul(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).setScale(DEFAULT_SCALE).toPlainString();
    }

    public static String mul(String v1, String v2, int scale) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).setScale(scale).toPlainString();
    }

    /**
     * （相对）精确的除法运算，当发生除不尽的情况时，默认保留2小数，以后的数字四舍五入。
     * 
     * @param v1 除数
     * @param v2 被除数
     * @return 两个数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEFAULT_SCALE);
    }

    /**
     * （相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     * 
     * @param v1 除数
     * @param v2 被除数
     * @param scale 小数点后保留几位
     * @return 两个数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double div(String v1, String v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 精确的小数位四舍五入处理。
     * 
     * @param v 浮点数
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v)).setScale(scale, BigDecimal.ROUND_HALF_UP);
        return b.doubleValue();
    }

    public static double round(String v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(v).setScale(scale, BigDecimal.ROUND_HALF_UP);
        return b.doubleValue();
    }

    /**
     * 精确的小数位四舍五入处理。
     * 
     * @param v 浮点数
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double floor(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v)).setScale(scale, BigDecimal.ROUND_FLOOR);
        return b.doubleValue();
    }

    public static double floor(String v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(v).setScale(scale, BigDecimal.ROUND_FLOOR);
        return b.doubleValue();
    }

    public static void main(String[] args) {
        System.out.println(compareTo(0.85d, 0.85d));
        System.out.println(compareTo(0.85d, 0.7512d));
        System.out.println(compareTo(0.85d, 0.9512d));

    }

}
