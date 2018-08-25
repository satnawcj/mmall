package com.mmall.dao;

import com.mmall.pojo.MmallUser;

public interface MmallUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MmallUser record);

    int insertSelective(MmallUser record);

    MmallUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MmallUser record);

    int updateByPrimaryKey(MmallUser record);
}