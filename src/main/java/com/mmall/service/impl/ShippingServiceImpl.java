package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.MmallShippingMapper;
import com.mmall.pojo.MmallShipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private MmallShippingMapper shippingMapper;

    /**
     * 新建地址
     *
     * @param userId
     * @param shipping
     * @return
     */
    public ServerResponse add(Integer userId, MmallShipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount > 0) {
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功~~", result);
        }
        return ServerResponse.createByErrorMessage("新建地址失败~~");
    }

    /**
     * 删除
     *
     * @param userId
     * @param shippingId
     * @return
     */
    public ServerResponse<String> del(Integer userId, Integer shippingId) {
        int resultCount = shippingMapper.deleteByShippingIdUserId(userId, shippingId);
        if (resultCount > 0) {
            return ServerResponse.createBySuccess("删除地址成功~~");
        }
        return ServerResponse.createByErrorMessage("删除地址失败~~");
    }

    /**
     * 更新
     *
     * @param userId
     * @param shipping
     * @return
     */
    public ServerResponse update(Integer userId, MmallShipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新地址成功~~");
        }
        return ServerResponse.createByErrorMessage("更新地址失败~~");
    }

    /**
     * 查询单条
     *
     * @param userId
     * @param shippingId
     * @return
     */
    public ServerResponse<MmallShipping> select(Integer userId, Integer shippingId) {
        MmallShipping shipping = shippingMapper.selectByShippingIdUserId(userId, shippingId);
        if (shipping == null) {
            return ServerResponse.createByErrorMessage("无法查询到该地址~~");
        }
        return ServerResponse.createBySuccess("查询地址成功~~", shipping);
    }

    /**
     * 分页接口
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<MmallShipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
