/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pole.kezuo.thread;

import com.hongdian.saas.util.CommUtils;
import com.hongdian.saas.util.time.TimeUtil;
import com.pole.kezuo.common.ConstsKezuo;
import com.pole.kezuo.common.SpringContextUtil;
import com.pole.kezuo.entity.Device;
import com.pole.kezuo.init.SystemInit;
import com.pole.kezuo.service.IDeviceService;
import com.xx.Client;
import com.xx.core.dto.LinkCheckMessage;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zlzuo
 */
public class ClientLinkThread implements Runnable {

    private final Logger log = LoggerFactory.getLogger(SystemInit.class);

    private IDeviceService deviceService;

    public ClientLinkThread() {
        deviceService = (IDeviceService) SpringContextUtil.getBean("deviceService");
    }

    @Override
    public void run() {
        while (true) {
            try {
                Map<String, Object> queryMap = new HashMap<String, Object>();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, -3);
                queryMap.put("beginTime", TimeUtil.convertDateToString(cal.getTime(), TimeUtil.YMDHMS));
                List<Device> list = deviceService.selectDeviceList(queryMap);
                Client client;
                for (Device device : list) {//定时维护链路连接
                    client = ConstsKezuo.CLIENT_MAP.get(ConstsKezuo.getClientIdFromDevice(device));
                    if (CommUtils.notNull(client)) {
                        LinkCheckMessage link = new LinkCheckMessage();
                        ConstsKezuo.setDeviceUpMsgCommInfo(link);
                        link.setStation(Integer.valueOf(ConstsKezuo.getStcdFromDevice(device)));
                        client.sendMessage(link);
                    }else{//没有则添加
                        
                    }
                }

            } catch (Exception e) {
                log.error(null, e);
            }

            try {
                Thread.sleep(60000);
            } catch (Exception e) {
                log.error(null, e);
            }
        }
    }

}
