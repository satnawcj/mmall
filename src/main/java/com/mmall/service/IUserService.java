package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.MmallUser;

public interface IUserService {
    /**
     * 登陆接口
     * @param username
     * @param password
     * @return
     */
    ServerResponse<MmallUser> login(String username, String password);
}
