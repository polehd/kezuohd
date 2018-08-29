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
 * @author zlzuo
 */
public class ConstsKezuo {

    //客户端信息
    public static String HOST = "tripnet.unilogger.cn";//主机IP
    public static int PORT = 10260;//主机端口

    public static int DAYS_PRE = -10;//向前推的天数


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

}
