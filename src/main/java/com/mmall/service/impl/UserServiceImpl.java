package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.MmallUserMapper;
import com.mmall.pojo.MmallUser;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private MmallUserMapper userMapper;

    /**
     * 登陆
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse<MmallUser> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("无此用户~~");
        }
        //todo md5 password
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        MmallUser user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误~~");
        }
        // 设置密码为空
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功~~", user);
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    public ServerResponse<String> register(MmallUser user) {
        ServerResponse res = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!res.isSuccess()) {
            return res;
        }
        res = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!res.isSuccess()) {
            return res;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        // user 密码 md5 加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败~~");
        }
        return ServerResponse.createBySuccess("注册成功~~");
    }

    /**
     * 校验参数
     *
     * @param str
     * @param type
     * @return
     */
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(str)) {
            int resultCount = 0;
            //开始校验
            if (Const.USERNAME.equals(str)) {
                resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户已经存在~~");
                }
            }
            if (Const.EMAIL.equals(str)) {
                resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("电子邮箱已经存在~~");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误~~");
        }
        return ServerResponse.createBySuccessMessage("校验成功~~");
    }

    /**
     * 寻找密码答案
     *
     * @param username
     * @return
     */
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在~~");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(username)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题为空~~");
    }

    /**
     * 检验问题答案
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            // 添加本地缓存。
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题答案错误~~");
    }

    /**
     * 重置密码
     *
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误, token 需要传递~~");
        }
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在~~");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);

        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token 无效或者过期~~");
        }
        if (StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功~~");
            }
        } else {
            return ServerResponse.createByErrorMessage("token 错误，请重新设置 token~~");
        }
        return ServerResponse.createByErrorMessage("修改密码失败~~");
    }

    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, MmallUser user) {
        //防止横向越权。一定要指定用户的 id 。
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户密码错误~~");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount == 0) {
            return ServerResponse.createByErrorMessage("用户密码更新失败~~");
        }
        return ServerResponse.createBySuccessMessage("用户密码更新成功~~");
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    public ServerResponse<MmallUser> updateInformation(MmallUser user) {
        // 用户的 username 是不能被更新的。
        // email 也要被校验，看看是否已经被占用
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("邮箱已经被占用~~");
        }
        MmallUser updateUser = new MmallUser();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功~~", updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败~~");
    }

    public ServerResponse<MmallUser> getInformation(Integer userId) {
        MmallUser user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找不到当前登陆用户~~");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    public ServerResponse checkAdminRole(MmallUser user) {
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

}
