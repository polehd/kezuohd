package com.pole.kezuo.service;

import com.pole.kezuo.entity.Device;
import com.baomidou.mybatisplus.service.IService;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 机井设备 服务类
 * </p>
 *
 * @author zlzuo
 * @since 2018-08-27
 */
public interface IDeviceService extends IService<Device> {

    public List<Device> selectDeviceList(Map<String, Object> queryMap);
}
