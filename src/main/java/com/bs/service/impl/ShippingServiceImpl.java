package com.bs.service.impl;

import com.bs.common.ServerResponse;
import com.bs.dao.ShippingMapper;
import com.bs.pojo.Shipping;
import com.bs.service.IShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Auther: 杨博文
 * @Date: 2019/5/18 03:30
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);//防止横向越权
        int rowCount = shippingMapper.insert(shipping);
        if(rowCount > 0){
            Map result = Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess("地址添加成功",result);
        }
        return ServerResponse.createByErrorMessage("地址添加失败");
    }

    public ServerResponse<String> del(Integer userId,Integer shippingId){
        int resultCount = shippingMapper.deleteByShippingIdUserId(userId,shippingId);//防止横向越权
        if(resultCount > 0){
            return ServerResponse.createBySuccess("地址删除成功");
        }
        return ServerResponse.createByErrorMessage("地址删除失败");
    }

    public ServerResponse update(Integer userId, Shipping shipping){
        shipping.setUserId(userId);//防止横向越权
        int rowCount = shippingMapper.updateByShipping(shipping);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("地址更新成功");
        }
        return ServerResponse.createByErrorMessage("地址更新失败");
    }

    public ServerResponse<Shipping> select(Integer userId, Integer shippingId){
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId,shippingId);//防止横向越权
        if(shipping == null){
            return ServerResponse.createByErrorMessage("找不到该地址");
        }
        return ServerResponse.createBySuccess("地址查询成功",shipping);
    }

    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }

}
