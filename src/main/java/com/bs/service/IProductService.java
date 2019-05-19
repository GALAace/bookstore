package com.bs.service;

import com.bs.common.ServerResponse;
import com.bs.pojo.Product;
import com.bs.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

/**
 * @Description: 产品service
 * @Auther: 杨博文
 * @Date: 2019/5/15 18:22
 */
public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId,Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);

}
