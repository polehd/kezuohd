/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pole.kezuo.common;

import com.pole.kezuo.entity.Device;
import com.xx.Client;
import com.xx.core.dto.ObjectMessage;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author zlzuo
 */
public class ConstsKezuo {

    //INT DIRECT, INT DIV, INT FCB, INT FUNCTIONCODE, INT PRODUCTNO, INT PRODUCTPWD, INT MONTH, INT YEAR;
    public static int DIRECT_UP = 1;
    public static int DIRECT_DOWN = 0;
    public static int DIV_MORE = 1;
    public static int DIV_ONE = 0;
    public static int FUNCTIONCODE_DEVICE = 1;
    public static int FUNCTIONCODE_PLAT = 0;
    public static int PRODUCTNO = 120;
    public static int PRODUCTPWD = 6;
    public static int MONTH = 7;
    public static int YEAR = 18;

    //客户端集合
    public static final ConcurrentHashMap<String, Client> CLIENT_MAP = new ConcurrentHashMap<String, Client>();

    /**
     * 获取客户端ID
     *
     * @param device
     * @return
     */
    public static String getClientIdFromDevice(Device device) {
        return device.getDistrictCode() + device.getDeviceNo();
    }

    /**
     * 获取站址
     *
     * @param device
     * @return
     */
    public static String getStcdFromDevice(Device device) {
        String distCode = device.getDistrictCode();
        String distShortCode = distCode.substring(distCode.length() - 2, distCode.length());//截取村编码后2位
        return distShortCode + device.getDeviceNo();
    }

    /**
     * 设置默认常用信息
     * @param om 
     */
    public static void setDeviceUpMsgCommInfo(ObjectMessage om) {
        om.setDirect(ConstsKezuo.DIRECT_UP);
        om.setDiv(ConstsKezuo.DIV_ONE);
        om.setFcb(3);
        om.setFunctionCode(ConstsKezuo.FUNCTIONCODE_DEVICE);
        om.setProductNo(ConstsKezuo.PRODUCTNO);
        om.setProductPwd(ConstsKezuo.PRODUCTPWD);
        om.setMonth(7);
        om.setYear(18);
    }
}
