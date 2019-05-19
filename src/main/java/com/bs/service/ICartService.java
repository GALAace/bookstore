package com.bs.service;

import com.bs.common.ServerResponse;
import com.bs.vo.CartVo;

/**
 * @Description: 购物车
 * @Auther: 杨博文
 * @Date: 2019/5/16 21:20
 */
public interface ICartService{

    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count);

    ServerResponse<CartVo> deleteProduct(Integer userId,String productIds);

    ServerResponse<CartVo> list(Integer userId);

    ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer checked,Integer productId);

    ServerResponse<Integer> getCartProductCount(Integer userId);

    
}
