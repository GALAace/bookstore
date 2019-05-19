package com.bs.service;

import com.bs.common.ServerResponse;
import com.bs.pojo.Shipping;
import com.github.pagehelper.PageInfo;

/**
 * @Description: 收货地址
 * @Auther: 杨博文
 * @Date: 2019/5/18 03:29
 */
public interface IShippingService {

    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse<String> del(Integer userId,Integer shippingId);

    ServerResponse update(Integer userId, Shipping shipping);

    ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
