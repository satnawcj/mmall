package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.MmallUser;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 后台管理原控制器
 */
@RequestMapping("/manage/user/")
@Controller
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    /**
     * 后台管理员的登陆
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @PostMapping("login.do")
    @ResponseBody
    public ServerResponse<MmallUser> login(String username, String password, HttpSession session) {
        ServerResponse<MmallUser> res = iUserService.login(username, password);
        if (res.isSuccess()) {
            MmallUser user = res.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                session.setAttribute(Const.CURRENT_USER, user);
                return res;
            } else {
                return ServerResponse.createByErrorMessage("无此权限～～");
            }
        }
        return res;
    }
}
