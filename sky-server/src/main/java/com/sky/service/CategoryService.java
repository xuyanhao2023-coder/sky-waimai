package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;

public interface CategoryService {
    /*
    * 新增分类
    * */
    void save(CategoryDTO categoryDTO);
    /*
    * 分页查询
    * */
    PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);
    /*
    * 启用禁用员工
    * */
    void starOrStop(Integer status, Long id);
    /*
     * 修改分类
     * */
    void updateCategory(CategoryDTO categoryDTO);
    /*
    * 删除分类
    * */
    void deleteById(Long id);
    /*
    * 根据类型查询分类
    * */
    Result<?> listByType(Integer type);
}
