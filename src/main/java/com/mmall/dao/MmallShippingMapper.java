package com.mmall.dao;

import com.mmall.pojo.MmallShipping;

public interface MmallShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MmallShipping record);

    int insertSelective(MmallShipping record);

    MmallShipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MmallShipping record);

    int updateByPrimaryKey(MmallShipping record);
}