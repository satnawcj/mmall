package com.mmall.dao;

import com.mmall.pojo.MmallCart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MmallCartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MmallCart record);

    int insertSelective(MmallCart record);

    MmallCart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MmallCart record);

    int updateByPrimaryKey(MmallCart record);

    MmallCart selectCartByUserIdProductId(@Param("userId") Integer userId,
                                          @Param("productId") Integer productId);

    List<MmallCart> selectCartByUserId(@Param("userId") Integer userId);

    int selectCartProductCheckedStatusByUserId(@Param("userId") Integer userId);

    int deleteByUserIdProductIds(@Param("userId") Integer userId,
                                 @Param("productList") List<String> productList);

    int checkedOrUncheckedProduct(@Param("useId") Integer userId,
                                  @Param("productId") Integer productId,
                                  @Param("checked") Integer checked);

    int selectCartProductCount(@Param("userId") Integer userId);

    List<MmallCart> selectCheckedCartByUserId(@Param("userId") Integer userId);
}