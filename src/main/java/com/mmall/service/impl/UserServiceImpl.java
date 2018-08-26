package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.MmallUserMapper;
import com.mmall.pojo.MmallUser;
import com.mmall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private MmallUserMapper userMapper;


    @Override
    public ServerResponse<MmallUser> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("无此用户～～");
        }
        //todo md5 password
        MmallUser user = userMapper.selectLogin(username, password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误～～");
        }
        // 设置密码为空
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功～～", user);
    }
}
