package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

    /*
     * 新增分类
     * */
    @Insert("insert into category(type,name,sort,status,create_time,update_time,create_user,update_user)" +
            "values (#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Category category);

    /*
     * 分页查询
     * */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /*
     * 启用禁用菜品
     * */
    void update(Category category);

    /*
     * 修改分类
     * */
    void updateCategory(Category category);
    /*
    * 删除分类
    * */
    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);
}
