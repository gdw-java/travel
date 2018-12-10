package com.heima.travel.service.imp;

import com.alibaba.fastjson.JSON;
import com.heima.travel.dao.CategoryDao;
import com.heima.travel.dao.imp.CategoryDaoImp;
import com.heima.travel.model.Category;
import com.heima.travel.service.CategoryService;
import com.heima.travel.utils.JedisUtils;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

public class CategoryServicImp implements CategoryService {
   private CategoryDao categoryDao=new CategoryDaoImp();

    //获取头部导航分类信息
    @Override
    public String findCategorys() {
        Jedis jedis = JedisUtils.getJedis();
        String jsonString = jedis.get("category_json");
        if (StringUtils.isEmpty(jsonString)) {
            List<Category> categorys= categoryDao.findCategorys();
            jsonString= JSON.toJSONString(categorys);
            jedis.set("category_json",jsonString);
        }
        jedis.close();
        return jsonString;
    }
}
