package com.mmall.dao;

import com.mmall.pojo.MmallShipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MmallShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MmallShipping record);

    int insertSelective(MmallShipping record);

    MmallShipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MmallShipping record);

    int updateByPrimaryKey(MmallShipping record);

    int deleteByShippingIdUserId(@Param("userId") Integer userId,
                                 @Param("shippingId") Integer shippingId);

    int updateByShipping(MmallShipping shipping);

    MmallShipping selectByShippingIdUserId(@Param("shippingId") Integer shippingId,
                                           @Param("userId") Integer userId);

    List<MmallShipping> selectByUserId(@Param("userId") Integer userId);
}