package com.bs.service;

import com.bs.common.ServerResponse;
import com.bs.pojo.Category;

import java.util.List;

/**
 * @Description: 类别service
 * @Auther: 杨博文
 * @Date: 2019/5/13 23:35
 */
public interface ICategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId,String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer CategoryId);

    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer CategoryId);

}
