package com.sky.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetemealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetemealDishMapper setemealDishMapper;

    /*
     * 新增菜品和口味
     * */
    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        //1.向菜品表插入一条数据
        dishMapper.insert(dish);
        //获取INSERT生成的主键值
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //2.向口味表插入N条数据
            dishFlavorMapper.insertBatch(flavors);

        }
    }

    /*
     * 分页查询
     * */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        //1.开始分页查询
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        //2.调用mapper执行分页查询
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /*
     * 菜品批量删除
     * */
    @Override
    public void deleteBatch(List<Long> ids) {
        //判断删除当前的菜品是否处于起售状态
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            // 新增判空逻辑
            if (dish != null && dish.getStatus() == StatusConstant.ENABLE) {
                //当前菜品处于起售状态，不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断当前的菜品是否能够删除--是否有关联套餐
        List<Long> setmealIds = setemealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && !setmealIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除菜品表中的菜品数据
        //sql:delete from dish where id in(?,?,?)
        dishMapper.deleteByIds(ids);
        //删除菜品关联的口味数据
        //sql: delete from dish_flavor where dish_id in(?,?,?)
        dishFlavorMapper.deleteByDishIds(ids);
    }
    /*
     * 根据ID查询菜品
     * */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        //根据菜品id查询菜品数据
        Dish dish = dishMapper.getById(id);
        //根据菜品id查询口味数据
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);
        //将查询到的数据封装VO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }
    /*
     * 修改菜品
     * */
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //修改菜品表基本信息
        dishMapper.update(dish);
        //删除原有的口味数据
        dishFlavorMapper.deleteByDishIds2(dishDTO.getId());
        //重新插入新的口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            //2.向口味表插入N条数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }
}
