/**
 *
 */
package com.pole.kezuo.tcpip;

import com.xx.Client;
import com.xx.core.dto.RealtimeMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author lee
 *
 */
public class App {

    /**
     * @param args
     */
    public static void main(String[] args) {

        RealtimeMessage msg = new RealtimeMessage();
        msg.setDirect(1);
        msg.setDiv(0);
        msg.setFcb(3);
        msg.setFunctionCode(1);
        msg.setProductNo(120);
        msg.setProductPwd(6);
        msg.setMonth(7);
        msg.setYear(18);
        msg.setStation(1);
        msg.setChannelNum(2);
        msg.setData(new float[]{0, 324});
        Client client = new Client("tripnet.unilogger.cn", 10260);
        // 等待启动
        while (true) {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                client.sendMessage(msg, 6);
            } catch (Exception ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
