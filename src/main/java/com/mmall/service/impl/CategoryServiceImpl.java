package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.MmallCategoryMapper;
import com.mmall.pojo.MmallCategory;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private MmallCategoryMapper categoryMapper;

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    /**
     * 添加分类
     *
     * @param categoryName
     * @param parentId
     * @return
     */
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类参数错误～～");
        }
        MmallCategory category = new MmallCategory();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//分类可用
        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("添加品类成功～～");
        }
        return ServerResponse.createByErrorMessage("添加品类失败～～");
    }

    /**
     * 更新分类名称
     *
     * @param categoryId
     * @param categoryName
     * @return
     */
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("更新品类参数错误～～");
        }
        MmallCategory category = new MmallCategory();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新品类名字成功～～");
        }
        return ServerResponse.createByErrorMessage("更新品类名字失败～～");
    }

    /**
     * 根据父节点查询子节点
     *
     * @param categoryId
     * @return
     */
    public ServerResponse<List<MmallCategory>> getChildParallelCategory(Integer categoryId) {
        List<MmallCategory> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到当前分类的子分类～～");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * 递归查询本节点的 id 及孩子节点的 id 。
     *
     * @param categoryId
     * @return
     */
    public ServerResponse selectCategoryAndChildrenById(Integer categoryId) {
        Set<MmallCategory> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null) {
            for (MmallCategory categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    // 递归算法，算出子节点。
    private Set<MmallCategory> findChildCategory(Set<MmallCategory> categorySet,
                                                 Integer categoryId) {
        MmallCategory category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        // 查找子节点，递归算法一定要有退出条件
        List<MmallCategory> categoryList = categoryMapper
                .selectCategoryChildrenByParentId(categoryId);
        for (MmallCategory categoryItem : categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
