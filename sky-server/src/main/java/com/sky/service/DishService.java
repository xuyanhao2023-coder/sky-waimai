package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

public interface DishService {
    /*
    * 新增菜品和口味
    * */
    public void saveWithFlavor(DishDTO dishDTO);
    /*
    * 分页查询
    * */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);
    /*
    * 菜品批量删除
    * */
    void deleteBatch(List<Long> ids);
}
