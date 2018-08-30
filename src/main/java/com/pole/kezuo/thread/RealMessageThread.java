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
import com.pole.kezuo.service.IAsyncService;
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

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private IDeviceService deviceService;
    private IAsyncService asyncService;

    public RealMessageThread() {
        deviceService = (IDeviceService) SpringContextUtil.getBean("deviceService");
        asyncService = (IAsyncService) SpringContextUtil.getBean("asyncService");
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
                Device device;
                for (int i = 0, size = ConstsKezuo.calcClentNum(list.size()); i < size; i++) {
                    device = list.get(i);
                    //for (Device device : list) {//定时发送实时数据
                    try {
                        client = ConstsKezuo.CLIENT_MAP.get(CommSend.getClientIdFromDevice(device));
                        if (CommUtils.notNull(client) && client.isRunning()) {
                            Message msg = client.sendMessage(CommSend.getRealMsgFromDevice(device), 6);
                            //log.info("收到实时数据确认：" + msg.toHexString());
                            log.info(String.format("client[%s]i[%s]收到实时数据确认：%s", i, CommSend.getClientIdFromDevice(device), msg.toHexString()));
                        }else  {
                            try {
                                ConstsKezuo.CLIENT_MAP.remove(CommSend.getClientIdFromDevice(device));
                                asyncService.executeAsync(device, i);
                            } catch (Exception e) {
                                log.error(null, e);
                            }
                        }
                    } catch (Exception e) {
                        log.error(CommSend.getClientIdFromDevice(device) + "=发送实时数据异常：", e);
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
