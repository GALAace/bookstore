package com.bs.service.impl;

import com.bs.common.ServerResponse;
import com.bs.dao.CategoryMapper;
import com.bs.pojo.Category;
import com.bs.service.ICategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Auther: 杨博文
 * @Date: 2019/5/13 23:37
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse addCategory(String categoryName,Integer parentId){
        if(parentId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("类别参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true); //分类可用

        int rowCount = categoryMapper.insert(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("类别添加成功");
        }
        return ServerResponse.createByErrorMessage("类别添加失败");
    }

    public ServerResponse updateCategoryName(Integer categoryId,String categoryName){
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("类别参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("类别名称修改成功");
        }
        return ServerResponse.createByErrorMessage("类别名称修改失败");
    }

    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前类别的子类别");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * @Description: 递归查询本节点id及子节点id
     * @Auther: 杨博文
     * @Date: 2019/5/14 17:36
     */
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
        Set<Category> categorySte = Sets.newHashSet();
        findChildCategory(categorySte,categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null){
            for(Category categoryItme : categorySte){
                categoryIdList.add(categoryItme.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    //递归算法,算出子节点
    private Set<Category> findChildCategory(Set<Category> categorySte,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            categorySte.add(category);
        }
        //查找子节点
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItme : categoryList){
            findChildCategory(categorySte,categoryItme.getId());
        }
        return categorySte;
    }
}
