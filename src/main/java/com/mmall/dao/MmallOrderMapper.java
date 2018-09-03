package com.mmall.dao;

import com.mmall.pojo.MmallOrder;
import com.mmall.pojo.MmallOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MmallOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MmallOrder record);

    int insertSelective(MmallOrder record);

    MmallOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MmallOrder record);

    int updateByPrimaryKey(MmallOrder record);

    MmallOrder selectByUserIdAndOrderNo(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    MmallOrder selectByOrderNo(Long orderNo);

    List<MmallOrder> selectByUserId(@Param("userId") Integer userId);

    List<MmallOrder> selectAllOrder();

    //二期新增定时关单
    List<MmallOrder> selectOrderStatusByCreateTime(@Param("status") Integer status, @Param("date") String date);

    int closeOrderByOrderId(Integer id);

}