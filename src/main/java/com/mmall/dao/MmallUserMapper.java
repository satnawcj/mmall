package com.mmall.dao;

import com.mmall.pojo.MmallUser;
import org.apache.ibatis.annotations.Param;

public interface MmallUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MmallUser record);

    int insertSelective(MmallUser record);

    MmallUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MmallUser record);

    int updateByPrimaryKey(MmallUser record);

    int checkUsername(String username);

    int checkEmail(@Param("email") String email);

    MmallUser selectLogin(@Param("username") String username, @Param("password") String password);

    String selectQuestionByUsername(@Param("username") String username);

    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);

    int checkPassword(@Param("password") String password, @Param("userId") Integer userId);

    int checkEmailByUserId(@Param("email") String email, @Param("userId") Integer userId);
}