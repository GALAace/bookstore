package com.bs.controller.portal;

import com.bs.common.Const;
import com.bs.common.ResponseCode;
import com.bs.common.ServerResponse;
import com.bs.pojo.User;
import com.bs.service.IUserService;
import com.bs.util.CookieUtil;
import com.bs.util.JsonUtil;
import com.bs.util.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Description: 用户
 * @Auther: 杨博文
 * @Date: 2019/5/11 22:06
 */

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;


    /**
     * @Description: 用户登录
     * @Auther: 杨博文
     * @Date: 2019/5/12 0:47
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse resp, HttpServletRequest request){
        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
            //session.setAttribute(Const.CURRENT_USER,response.getData());
            CookieUtil.writeLoginToken(resp,session.getId());
            CookieUtil.readLoginToken(request);
            CookieUtil.delLoginToken(resp,request);
            RedisPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return response;
    }

    /**
     * @Description: 用户登出
     * @Auther: 杨博文
     * @Date: 2019/5/12 15:46
     */
    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> login(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * @Description: 用户注册
     * @Auther: 杨博文
     * @Date: 2019/5/12 15:59
     */
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    /**
     * @Description: 校验信息
     * @Auther: 杨博文
     * @Date: 2019/5/12 23:31
     */
    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
        return iUserService.checkValid(str,type);
    }

    /**
     * @Description: 获取登录用户信息
     * @Auther: 杨博文
     * @Date: 2019/5/12 23:34
     */
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user!=null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("未登录，无法获取用户信息");
    }
    /**
     * @Description: 忘记密码(获取问题)
     * @Auther: 杨博文
     * @Date: 2019/5/13 2:00
     */
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    /**
     * @Description: 提交问题答案
     * @Auther: 杨博文
     * @Date: 2019/5/13 2:40
     */
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question,String answer){
        return iUserService.checkAnswer(username, question,answer);
    }

    /**
     * @Description: 忘记密码的重设密码
     * @Auther: 杨博文
     * @Date: 2019/5/13 3:18
     */
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    /**
     * @Description: 登录中状态重置密码
     * @Auther: 杨博文
     * @Date: 2019/5/13 16:36
     */
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }
    /**
     * @Description: 登录状态更新个人信息
     * @Auther: 杨博文
     * @Date: 2019/5/13 17:16
     */
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session,User user){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if(response.isSuccess()){
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }
    /**
     * @Description: 获取当前登录用户的详细信息，并强制去登录
     * @Auther: 杨博文
     * @Date: 2019/5/13 17:44
     */
    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录,需强制登陆status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }

}
