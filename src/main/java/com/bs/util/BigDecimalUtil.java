package com.bs.util;

import java.math.BigDecimal;

/**
 * @Description:
 * @Auther: 杨博文
 * @Date: 2019/5/17 10:32
 */
public class BigDecimalUtil {

    private BigDecimalUtil(){

    }
    //加
    public static BigDecimal add(double v1,double v2){
        //商业运算中要使用BigDecimal的String构造器！！
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }
    //减
    public static BigDecimal sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }
    //乘
    public static BigDecimal mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }
    //除以
    public static BigDecimal div(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);//保留两位，四舍五入
    }
}
