package com.mmall.dao;

import com.mmall.pojo.MmallCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MmallCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MmallCategory record);

    int insertSelective(MmallCategory record);

    MmallCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MmallCategory record);

    int updateByPrimaryKey(MmallCategory record);

    List<MmallCategory> selectCategoryChildrenByParentId(@Param("parentId") Integer parentId);
}