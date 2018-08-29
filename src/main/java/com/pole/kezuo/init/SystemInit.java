/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pole.kezuo.init;

import com.pole.kezuo.common.ConstsKezuo;
import com.pole.kezuo.common.CommSend;
import com.pole.kezuo.entity.Device;
import com.pole.kezuo.service.IDeviceService;
import com.pole.kezuo.thread.ClientLinkThread;
import com.pole.kezuo.thread.RealMessageThread;
import com.pole.kezuo.util.mytool.TimeUtil;
import com.xx.Client;
import com.xx.core.dto.LinkCheckMessage;
import com.xx.core.dto.Message;
import com.xx.core.dto.RegisterMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zlzuo
 */
@Component
public class SystemInit implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(SystemInit.class);

    @Resource(name = "deviceService")
    private IDeviceService deviceService;

    @Override
    public void run(String... strings) throws Exception {
        try {

            this.initLinkAndRegister();

            Thread clientLinkThread = new Thread(new ClientLinkThread());
            clientLinkThread.start();

            Thread realMessageThread = new Thread(new RealMessageThread());
            realMessageThread.start();

            log.info("systerm init success(系統初始化成功！)");
        } catch (Exception e) {
            log.error(null, e);
        }

    }

    /**
     * 初始化登录和注册
     */
    private void initLinkAndRegister() throws Exception {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, ConstsKezuo.DAYS_PRE);
        queryMap.put("beginTime", TimeUtil.convertDateToString(cal.getTime(), TimeUtil.YMDHMS));
        List<Device> list = deviceService.selectDeviceList(queryMap);
        for (Device device : list) {
            try {
                Client client = new Client(ConstsKezuo.HOST, ConstsKezuo.PORT);
                //等待客户端启动
                Thread.sleep(5000);
                String clentId = CommSend.getClientIdFromDevice(device);
                ConstsKezuo.CLIENT_MAP.put(clentId, client);

                log.info("device.toString() :" + device.toString());

                //链路信息
                Message linkResp = client.sendMessage(CommSend.getLinkCheckMsgFromDevice(device), 6);

                //注册信息
                Message regResp = client.sendMessage(CommSend.getRegMsgFromDevice(device), 6);
                log.info("收到注册信息确认:" + regResp.toHexString());
            } catch (Exception e) {
                log.error(null, e);
            }
        }
    }
}
