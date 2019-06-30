package com.bs.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: Cookie
 * @Auther: 杨博文
 * @Date: 2019/6/26 01:35
 */

@Slf4j
public class CookieUtil {

    private final static String COOKIE_DOMAIN = "gala.com";
    private final static String COOKIE_NAME = "bs_login_token";


    /**
     * @Description: 写入cookie
     * @Auther: 杨博文
     * @Date: 2019/6/26 1:49
     */
    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie cookie = new Cookie(COOKIE_NAME,token);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");
        //无法通过脚本获取cookie信息
        cookie.setHttpOnly(true);
        //单位：s,-1为永久有效。如果不设置MaxAge，cookie就不会写入硬盘，而是写在内存。只在当前页面有效
        cookie.setMaxAge(60*60*24*365);
        log.info("write cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());
        response.addCookie(cookie);

    }
    /**
     * @Description: 读取cookie
     * @Auther: 杨博文
     * @Date: 2019/6/26 1:58
     */
    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                log.info("read cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());
                if(StringUtils.equals(cookie.getName(),COOKIE_NAME)){
                    log.info("return cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * @Description: 删除cookie
     * @Auther: 杨博文
     * @Date: 2019/6/26 2:06
     */
    public static void delLoginToken(HttpServletResponse response,HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(StringUtils.equals(cookie.getName(),COOKIE_NAME)){
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    //0等于删除次cookie
                    cookie.setMaxAge(0);
                    log.info("del cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());
                    response.addCookie(cookie);
                    return;
                }
            }
        }
    }
}

