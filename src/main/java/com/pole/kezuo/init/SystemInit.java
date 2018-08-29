/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pole.kezuo.init;

import com.pole.kezuo.common.ConstsKezuo;
import com.pole.kezuo.entity.Device;
import com.pole.kezuo.service.IDeviceService;
import com.pole.kezuo.thread.ClientLinkThread;
import com.pole.kezuo.thread.RealMessageThread;
import com.pole.kezuo.util.mytool.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.pole.kezuo.service.IAsyncService;

/**
 * @author zlzuo
 */
@Component
public class SystemInit implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "deviceService")
    private IDeviceService deviceService;

    @Resource(name = "asyncService")
    private IAsyncService asyncService;

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
                asyncService.executeAsync(device);
            } catch (Exception e) {
                log.error(null, e);
            }
        }
    }
}
