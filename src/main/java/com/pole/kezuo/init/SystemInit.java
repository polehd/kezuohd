/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pole.kezuo.init;

import com.hongdian.saas.util.time.TimeUtil;
import com.pole.kezuo.common.ConstsKezuo;
import com.pole.kezuo.entity.Device;
import com.pole.kezuo.service.IDeviceService;
import com.pole.kezuo.thread.ClientLinkThread;
import com.pole.kezuo.thread.RealMessageThread;
import com.xx.Client;
import com.xx.core.dto.LinkCheckMessage;
import com.xx.core.dto.RegisterMessage;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
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
    private void initLinkAndRegister() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -3);
        queryMap.put("beginTime", TimeUtil.convertDateToString(cal.getTime(), TimeUtil.YMDHMS));
        List<Device> list = deviceService.selectDeviceList(queryMap);
        for (Device device : list) {
            Client client = new Client("tripnet.unilogger.cn", 10260);
            String clentId = ConstsKezuo.getClientIdFromDevice(device);
            ConstsKezuo.CLIENT_MAP.put(clentId, client);

            //拼装站址1-60000
            String stcd = ConstsKezuo.getClientIdFromDevice(device);

            //链路信息
            LinkCheckMessage link = new LinkCheckMessage();
            ConstsKezuo.setDeviceUpMsgCommInfo(link);
            link.setStation(Integer.valueOf(stcd));
            client.sendMessage(link);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                log.error(null, ex);
            }

            //注册信息
            RegisterMessage reg = new RegisterMessage();
            ConstsKezuo.setDeviceUpMsgCommInfo(reg);
            reg.setStation(Integer.valueOf(stcd));
            String serial = "55102018204" + device.getDistrictCode() + device.getDeviceNo();
            if (serial.length() < 32) {
                for (int i = 0, len = 32 - serial.length(); i < len; i++) {
                    serial = "0" + serial;
                }
            }
            reg.setSerial(serial);
            client.sendMessage(reg);
        }
    }
}
