package com.sky.mapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetemealDishMapper {
    /*
    * 根据菜品ID查询对应的套餐ID
    * */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
