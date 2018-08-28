package com.mmall.dao;

import com.mmall.pojo.MmallProduct;
import com.mmall.pojo.MmallProductWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MmallProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MmallProductWithBLOBs record);

    int insertSelective(MmallProductWithBLOBs record);

    MmallProductWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MmallProductWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(MmallProductWithBLOBs record);

    int updateByPrimaryKey(MmallProduct record);

    List<MmallProductWithBLOBs> selectList();

    List<MmallProductWithBLOBs> selectByNameAndProductId(@Param("productName") String productName, @Param("productId") Integer productId);

    List<MmallProductWithBLOBs> selectByNameAndCategoryIds(@Param("productName")String productName,@Param("categoryIdList")List<Integer> categoryIdList);

}