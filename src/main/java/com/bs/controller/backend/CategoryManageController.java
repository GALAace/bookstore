package com.bs.controller.backend;

import com.bs.common.Const;
import com.bs.common.ResponseCode;
import com.bs.common.ServerResponse;
import com.bs.pojo.User;
import com.bs.service.ICategoryService;
import com.bs.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Description: 类别管理
 * @Auther: 杨博文
 * @Date: 2019/5/13 23:13
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * @Description: 添加类别
     * @Auther: 杨博文
     * @Date: 2019/5/13 23:48
     */
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请先登录");
        }
        //校验是否为管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员
            return iCategoryService.addCategory(categoryName,parentId);
        }else {
            return ServerResponse.createByErrorMessage("权限不足,请使用管理员身份操作");
        }
    }

    /**
     * @Description: 修改类别名字
     * @Auther: 杨博文
     * @Date: 2019/5/14 1:51
     */
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请先登录");
        }
        //校验是否为管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        }else {
            return ServerResponse.createByErrorMessage("权限不足,请使用管理员身份操作");
        }
    }

    /**
     * @Description: 获取类别子节点(平级)
     * @Auther: 杨博文
     * @Date: 2019/5/14 3:06
     */
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0")Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请先登录");
        }
        //校验是否为管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //查询子节点的类别信息，并且不递归，保持平级
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else {
            return ServerResponse.createByErrorMessage("权限不足,请使用管理员身份操作");
        }
    }
    
    /**
     * @Description: 获取当前类别id及递归子节点categoryId
     * @Auther: 杨博文
     * @Date: 2019/5/14 15:56
     */
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0")Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请先登录");
        }
        //校验是否为管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //查询当前节点的类别id和递归子节点id
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }else {
            return ServerResponse.createByErrorMessage("权限不足,请使用管理员身份操作");
        }
    }


}
