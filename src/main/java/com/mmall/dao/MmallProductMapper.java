package com.mmall.dao;

import com.mmall.pojo.MmallProduct;
import com.mmall.pojo.MmallProductWithBLOBs;

public interface MmallProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MmallProductWithBLOBs record);

    int insertSelective(MmallProductWithBLOBs record);

    MmallProductWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MmallProductWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(MmallProductWithBLOBs record);

    int updateByPrimaryKey(MmallProduct record);
}