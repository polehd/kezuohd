package com.pole.kezuo.mapper;

import com.pole.kezuo.entity.Device;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 机井设备 Mapper 接口
 * </p>
 *
 * @author zlzuo
 * @since 2018-08-27
 */
public interface DeviceMapper extends BaseMapper<Device> {
    

    public List<Device> selectDeviceList(Map<String, Object> queryMap);

}
