///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.pole.kezuo.bak;
//
//import com.hongdian.saas.util.time.TimeUtil;
//import com.pole.kezuo.common.ConstsKezuo;
//import com.pole.kezuo.entity.Device;
//import com.pole.kezuo.service.IDeviceService;
//import com.xx.Client;
//import com.xx.core.dto.LinkCheckMessage;
//import com.xx.core.dto.RealtimeMessage;
//import com.xx.core.dto.RegisterMessage;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import javax.annotation.Resource;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//
///**
// *
// * @author zlzuo
// */
////@Component
//public class SystemInitBak implements CommandLineRunner {
//
//    private final Logger log = LoggerFactory.getLogger(SystemInitBak.class);
//
//    @Resource(name = "deviceService")
//    private IDeviceService deviceService;
//
//    @Override
//    public void run(String... strings) throws Exception {
//        log.info("systerm init success(系統初始化成功！)");
//
////        Wrapper<Device> ew = new EntityWrapper<Device>();
////        List<Device> list = deviceService.selectList(ew);
//        Map<String, Object> queryMap = new HashMap<String, Object>();
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DAY_OF_MONTH, -3);
//        queryMap.put("beginTime", TimeUtil.convertDateToString(cal.getTime(), TimeUtil.YMDHMS));
//        List<Device> list = deviceService.selectDeviceList(queryMap);
//        for (Device device : list) {
//            Client client = new Client("tripnet.unilogger.cn", 10260);
//            String clentId = device.getDistrictCode() + device.getDeviceNo();
//            ConstsKezuo.CLIENT_MAP.put(clentId, client);
//
//            //拼装站址1-60000
//            String distCode = device.getDistrictCode();
//            String distShortCode = distCode.substring(distCode.length() - 2, distCode.length());//截取村编码后2位
//            String stcdstr = distShortCode + device.getDeviceNo();
//
//            //链路信息
//            LinkCheckMessage link = new LinkCheckMessage();
//            link.setDirect(ConstsKezuo.DIRECT_UP);
//            link.setDiv(ConstsKezuo.DIV_ONE);
//            link.setFcb(3);
//            link.setFunctionCode(ConstsKezuo.FUNCTIONCODE_DEVICE);
//            link.setProductNo(ConstsKezuo.PRODUCTNO);
//            link.setProductPwd(ConstsKezuo.PRODUCTPWD);
//            link.setMonth(7);
//            link.setYear(18);
//            link.setStation(Integer.valueOf(stcdstr));
//
//            //注册信息
//            RegisterMessage reg = new RegisterMessage();
//            reg.setDirect(ConstsKezuo.DIRECT_UP);
//            reg.setDiv(ConstsKezuo.DIV_ONE);
//            reg.setFcb(3);
//            reg.setFunctionCode(ConstsKezuo.FUNCTIONCODE_DEVICE);
//            reg.setProductNo(ConstsKezuo.PRODUCTNO);
//            reg.setProductPwd(ConstsKezuo.PRODUCTPWD);
//            reg.setMonth(7);
//            reg.setYear(18);
//            reg.setStation(Integer.valueOf(stcdstr));
//            String serial = "55102018204" + device.getDistrictCode() + device.getDeviceNo();
//            if (serial.length() < 32) {
//                for (int i = 0, len = 32 - serial.length(); i < len; i++) {
//                    serial = "0" + serial;
//                }
//            }
//            reg.setSerial(serial);
//
//            //实时信息
//            RealtimeMessage msg = new RealtimeMessage();
//            msg.setDirect(ConstsKezuo.DIRECT_UP);
//            msg.setDiv(ConstsKezuo.DIV_ONE);
//            msg.setFcb(3);
//            msg.setFunctionCode(ConstsKezuo.FUNCTIONCODE_DEVICE);
//            msg.setProductNo(ConstsKezuo.PRODUCTNO);
//            msg.setProductPwd(ConstsKezuo.PRODUCTPWD);
//            msg.setMonth(7);
//            msg.setYear(18);
//            msg.setStation(Integer.valueOf(stcdstr));
//            msg.setChannelNum(2);
//            msg.setData(new float[]{0, device.getWaterUsed().floatValue()});
//
//            client.sendMessage(link);
//            client.sendMessage(reg);
//            client.sendMessage(msg);
//            break;
//        }
//
//    }
//
//    public static void main(String[] args) {
//        String serial = "55102018204" + "202" + "115";
//        if (serial.length() < 32) {
//            for (int i = 0, len = 32 - serial.length(); i < len; i++) {
//                serial = "0" + serial;
//            }
//        }
//        System.out.println(serial);
//
//        String distCode = "202";
//        distCode = distCode.substring(distCode.length() - 2, distCode.length());//截取村编码后2位
//        String stcdstr = distCode + "115";
//        System.out.println("stcdstr" + stcdstr);
//    }
//}
