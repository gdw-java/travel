package com.heima.travel.dao.imp;

import com.heima.travel.dao.CategoryDao;
import com.heima.travel.model.Category;
import com.heima.travel.utils.JdbcTemplateUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CategoryDaoImp implements CategoryDao {
    private JdbcTemplate jdbcTemplate= JdbcTemplateUtil.getJdbcTemplate();
    //获取分类信息dao
    @Override
    public List<Category> findCategorys() {
        String sql="select * from tab_category";
        List<Category> categoryList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));
        return categoryList;
    }
}
