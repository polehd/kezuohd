package com.pole.kezuo.service.impl;

import com.pole.kezuo.entity.Device;
import com.pole.kezuo.mapper.DeviceMapper;
import com.pole.kezuo.service.IDeviceService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 机井设备 服务实现类
 * </p>
 *
 * @author zlzuo
 * @since 2018-08-27
 */
@Service("deviceService")
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    @Resource
    DeviceMapper deviceMapper;

    @Override
    public List<Device> selectDeviceList(Map<String, Object> queryMap) {
        return deviceMapper.selectDeviceList(queryMap);
    }

}
