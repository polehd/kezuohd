/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pole.kezuo.thread;

import com.pole.kezuo.common.ConstsKezuo;
import com.pole.kezuo.common.CommSend;
import com.pole.kezuo.common.SpringContextUtil;
import com.pole.kezuo.entity.Device;
import com.pole.kezuo.init.SystemInit;
import com.pole.kezuo.service.IAsyncService;
import com.pole.kezuo.service.IDeviceService;
import com.pole.kezuo.util.mytool.CommUtils;
import com.pole.kezuo.util.mytool.TimeUtil;
import com.xx.Client;
import com.xx.core.dto.LinkCheckMessage;
import com.xx.core.dto.Message;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xx.exception.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zlzuo
 */
public class ClientLinkThread implements Runnable {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private IDeviceService deviceService;
    private IAsyncService asyncService;

    public ClientLinkThread() {

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
                Boolean isNewClient = false;
                Device device;
                for (int i = 0, size = ConstsKezuo.calcClentNum(list.size()); i < size; i++) {
                    device = list.get(i);
                    //for (Device device : list) {//定时维护链路连接,还要定时清除Map,链路检测改为异步。
                    try {
                        client = ConstsKezuo.CLIENT_MAP.get(CommSend.getClientIdFromDevice(device));
                        if (CommUtils.notNull(client)) {
                            Message msg = client.sendMessage(CommSend.getLinkCheckMsgFromDevice(device), 6);
                            //log.info("收到链路检测确认 :" + msg.toHexString());
                        } else {
                            isNewClient = true;
                        }
                    } catch (ClientException ce) {
                        isNewClient = true;
                        log.error(CommSend.getClientIdFromDevice(device) + "-ClientException重新创建客户端", ce);
                    } catch (Exception e) {
                        log.error("非ClientException异常：", e);
                    }

                    //创建新client
                    if (isNewClient) {
                        try {
                            asyncService.executeAsync(device, i);
                        } catch (Exception e) {
                            log.error(null, e);
                        }
                    }
                }

            } catch (Exception e) {
                log.error(null, e);
            }

            try {
                Thread.sleep(30000);
            } catch (Exception e) {
                log.error(null, e);
            }
        }
    }

}
