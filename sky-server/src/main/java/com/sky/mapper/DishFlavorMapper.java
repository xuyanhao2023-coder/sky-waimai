package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
//    批量插入口味数据
    void insertBatch(List<DishFlavor> flavors);
// 根据菜品批量删除关联的口味数据
    void deleteByDishIds(List<Long> dishIds);
//根据ID查询菜品口味数据
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long id);

    @Select("select * from dish_flavor where dish_id = #{dishId}")
    void deleteByDishIds2(Long id);
}
