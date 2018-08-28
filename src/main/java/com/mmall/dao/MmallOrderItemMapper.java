package com.mmall.dao;

import com.mmall.pojo.MmallOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MmallOrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MmallOrderItem record);

    int insertSelective(MmallOrderItem record);

    MmallOrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MmallOrderItem record);

    int updateByPrimaryKey(MmallOrderItem record);

    List<MmallOrderItem> getByOrderNoUserId(@Param("orderNo") Long orderNo, @Param("userId") Integer userId);

    void batchInsert(@Param("orderItemList") List<MmallOrderItem> orderItemList);

    List<MmallOrderItem> getByOrderNo(@Param("orderNo")Long orderNo);
}