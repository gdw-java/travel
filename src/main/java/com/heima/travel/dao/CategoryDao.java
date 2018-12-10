package com.heima.travel.dao;

import com.heima.travel.model.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> findCategorys();
}
