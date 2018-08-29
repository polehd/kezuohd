/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pole.kezuo.thread;

import com.pole.kezuo.common.ConstsKezuo;
import com.pole.kezuo.common.SpringContextUtil;
import com.pole.kezuo.entity.Device;
import com.pole.kezuo.init.SystemInit;
import com.pole.kezuo.service.IDeviceService;
import com.pole.kezuo.util.mytool.CommUtils;
import com.pole.kezuo.util.mytool.TimeUtil;
import com.xx.Client;
import com.xx.core.dto.Message;
import com.xx.core.dto.RealtimeMessage;
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
public class RealMessageThread implements Runnable {

    private final Logger log = LoggerFactory.getLogger(SystemInit.class);

    private IDeviceService deviceService;

    public RealMessageThread() {
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
                for (Device device : list) {//定时发送实时数据
                    client = ConstsKezuo.CLIENT_MAP.get(ConstsKezuo.getClientIdFromDevice(device));
                    if (CommUtils.notNull(client)) {
                        //实时信息
                        RealtimeMessage rm = new RealtimeMessage();
                        ConstsKezuo.setDeviceUpMsgCommInfo(rm);
                        rm.setStation(Integer.valueOf(ConstsKezuo.getStcdFromDevice(device)));
                        rm.setChannelNum(2);
                        rm.setData(new float[]{0, device.getWaterUsed().floatValue()});
                        Message msg = client.sendMessage(rm, 6);
                        log.info("RealMessage resp msg.toHexString() :" + msg.toHexString());
                    }
                }

            } catch (Exception e) {
                log.error(null, e);
            }

            try {
                Thread.sleep(70000);
            } catch (Exception e) {
                log.error(null, e);
            }
        }
    }

}
