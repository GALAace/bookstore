package com.bs.controller.backend;

import com.bs.common.Const;
import com.bs.common.ResponseCode;
import com.bs.common.ServerResponse;
import com.bs.pojo.Product;
import com.bs.pojo.User;
import com.bs.service.IFileService;
import com.bs.service.IProductService;
import com.bs.service.IUserService;
import com.bs.util.PropertiesUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Description: 产品管理
 * @Auther: 杨博文
 * @Date: 2019/5/15 18:09
 */

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    /**
     * @Description: 新增or更新产品
     * @Auther: 杨博文
     * @Date: 2019/5/15 18:37
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //增加产品
            return iProductService.saveOrUpdateProduct(product);
        }else {
            return ServerResponse.createByErrorMessage("权限不足,请使用管理员身份操作");
        }
    }

    /**
     * @Description: 产品上下架
     * @Auther: 杨博文
     * @Date: 2019/5/15 18:45
     */
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session,Integer productId,Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //修改状态
            return iProductService.setSaleStatus(productId,status);
        }else {
            return ServerResponse.createByErrorMessage("权限不足,请使用管理员身份操作");
        }
    }

    /**
     * @Description: 产品详情
     * @Auther: 杨博文
     * @Date: 2019/5/15 19:32
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //获取详情
            return iProductService.manageProductDetail(productId);
        }else {
            return ServerResponse.createByErrorMessage("权限不足,请使用管理员身份操作");
        }
    }

    /**
     * @Description: 产品list
     * @Auther: 杨博文
     * @Date: 2019/5/15 20:11
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //获取产品list并分页
            return iProductService.getProductList(pageNum,pageSize);
        }else {
            return ServerResponse.createByErrorMessage("权限不足,请使用管理员身份操作");
        }
    }

    /**
     * @Description: 产品搜索
     * @Auther: 杨博文
     * @Date: 2019/5/15 20:49
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session, String productName,Integer productId, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);
        }else {
            return ServerResponse.createByErrorMessage("权限不足,请使用管理员身份操作");
        }
    }

    /**
     * @Description: 上传文件
     * @Auther: 杨博文
     * @Date: 2019/5/16 0:43
     */
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录");
        }

        if(iUserService.checkAdminRole(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServerResponse.createBySuccess(fileMap);
        }else {
            return ServerResponse.createByErrorMessage("权限不足,请使用管理员身份操作");
        }
    }

    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            resultMap.put("success",false);
            resultMap.put("msg","管理员未登录");
            return resultMap;
        }
        //使用simditor富文本的要求返回
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }

        if(iUserService.checkAdminRole(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);

            response.addHeader("Access-Control-Allow-Headers","X-File-Name");

            return resultMap;
        }else {
            resultMap.put("success",false);
            resultMap.put("msg","权限不足,请使用管理员身份操作");
            return resultMap;
        }
    }
}
