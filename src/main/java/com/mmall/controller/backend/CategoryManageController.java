package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.MmallUser;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 添加类目
     *
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @PostMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName,
                                      @RequestParam(value = "parentId", defaultValue = "0")
                                              int parentId) {
        MmallUser user = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆~~");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员，增加处理分类的逻辑。
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需管理员权限~~");
        }
    }

    /**
     * 更新类目名称
     *
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    @PostMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        MmallUser user = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆~~");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 更新类目
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需管理员权限~~");
        }
    }

    /**
     * 根据父节点，查询所有子节点
     *
     * @param session
     * @param categoryId
     * @return
     */
    @ResponseBody
    @GetMapping("get_category.do")
    public ServerResponse getChildParallelCategory(HttpSession session,
                                                   @RequestParam(value = "categoryId", defaultValue = "0")
                                                           Integer categoryId) {
        MmallUser user = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆~~");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 查询子节点信息，不第归，保持平级
            return iCategoryService.getChildParallelCategory(categoryId);

        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需管理员权限~~");
        }
    }

    /**
     * 深度查询
     * @param session
     * @param categoryId
     * @return
     */
    @ResponseBody
    @GetMapping("get_deep_category.do")
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,
                                                             @RequestParam(value = "categoryId", defaultValue = "0")
                                                                     Integer categoryId) {
        MmallUser user = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆~~");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 查询子节点的 id 和递归子节点的 id 。 0->100->1000 传 100 ，返回 1000，传 0 返回 100 和 1000
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需管理员权限~~");
        }
    }
}
