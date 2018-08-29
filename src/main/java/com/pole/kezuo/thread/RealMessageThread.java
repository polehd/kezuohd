/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pole.kezuo.thread;

import com.pole.kezuo.common.CommSend;
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
                cal.add(Calendar.DAY_OF_MONTH, ConstsKezuo.DAYS_PRE);
                queryMap.put("beginTime", TimeUtil.convertDateToString(cal.getTime(), TimeUtil.YMDHMS));
                List<Device> list = deviceService.selectDeviceList(queryMap);
                Client client;
                for (Device device : list) {//定时发送实时数据
                    try {
                        client = ConstsKezuo.CLIENT_MAP.get(CommSend.getClientIdFromDevice(device));
                        if (CommUtils.notNull(client)) {
                            Message msg = client.sendMessage(CommSend.getRealMsgFromDevice(device), 6);
                            log.info("收到实时数据确认：" + msg.toHexString());
                        }
                    } catch (Exception e) {
                        log.error("发送实时数据异常：", e);
                    }
                }

            } catch (Exception e) {
                log.error(null, e);
            }

            try {
                Thread.sleep(15000);
            } catch (Exception e) {
                log.error(null, e);
            }
        }
    }

}
