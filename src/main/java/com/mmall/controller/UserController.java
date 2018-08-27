package com.mmall.controller;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.MmallUser;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 用户控制器
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 登陆接口
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @PostMapping(value = "login.do")
    @ResponseBody
    public ServerResponse<MmallUser> login(String username, String password, HttpSession session) {
        ServerResponse<MmallUser> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * 登出接口
     *
     * @param session
     * @return
     */
    @GetMapping(value = "logout.do")
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess("登出成功～～");
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @PostMapping(value = "register.do")
    @ResponseBody
    public ServerResponse<String> register(MmallUser user) {
        return iUserService.register(user);
    }

    /**
     * 校验参数
     *
     * @param str
     * @param type
     * @return
     */
    @GetMapping(value = "check_valid.do")
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /**
     * 获取登陆用户信息
     *
     * @param session
     * @return
     */
    @GetMapping(value = "get_user_info.do")
    @ResponseBody
    public ServerResponse<MmallUser> getUserInfo(HttpSession session) {
        MmallUser user = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登陆～～");
    }

    /**
     * 寻找密码
     *
     * @param username
     * @return
     */
    @GetMapping(value = "forget_get_question.do")
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    /**
     * 校验问题答案
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @GetMapping(value = "forget_check_answer.do")
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 重置密码
     * 忘记密码功能中
     *
     * @param username
     * @param passwordNew
     * @param token
     * @return
     */
    @PostMapping(value = "forget_reset_password.do")
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String token) {
        return iUserService.forgetResetPassword(username, passwordNew, token);
    }

    /**
     * 重置密码
     * 用户重置密码
     *
     * @param session
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @PostMapping(value = "reset_password.do")
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew) {
        MmallUser user = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆～～");
        }
        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }

    /**
     * 更新用户信息
     *
     * @param session
     * @param user
     * @return
     */
    @PostMapping(value = "update_information.do")
    @ResponseBody
    public ServerResponse<MmallUser> updateInformation(HttpSession session, MmallUser user) {
        MmallUser currUser = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (currUser == null) {
            return ServerResponse.createByErrorMessage("用户未登陆～～");
        }
        user.setId(currUser.getId());
        user.setUsername(currUser.getUsername());
        ServerResponse<MmallUser> res = iUserService.updateInformation(user);
        if (res.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, res.getData());
        }
        return res;
    }

    /**
     * 得到登陆用户信息
     *
     * @param session
     * @return
     */
    @GetMapping(value = "get_information.do")
    @ResponseBody
    public ServerResponse<MmallUser> getInformation(HttpSession session) {
        MmallUser currUser = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (currUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登陆，要强制登陆，status = 10～～");
        }
        return iUserService.getInformation(currUser.getId());
    }
}
