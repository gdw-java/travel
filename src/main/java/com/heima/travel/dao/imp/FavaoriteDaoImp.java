package com.heima.travel.dao.imp;

import com.heima.travel.dao.FavoriteDao;
import com.heima.travel.model.Favorite;
import com.heima.travel.model.Route;
import com.heima.travel.utils.JdbcTemplateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class FavaoriteDaoImp implements FavoriteDao {
    private JdbcTemplate jdbcTemplate = JdbcTemplateUtil.getJdbcTemplate();

    //根据rid更新收藏次数到tab_route表


    @Override
    public void updateRouteCountByRid(String rid, JdbcTemplate jdbcTemplate) {
        String sql = "update tab_route set count=count+1 where rid=?";
        jdbcTemplate.update(sql, rid);

    }

    //添加收藏
    @Override
    public void addFavoriteByRidAndUid(String rid, int uid, String date, JdbcTemplate jdbcTemplate) {
        String sql = "insert into tab_favorite values(?,?,?)";
        jdbcTemplate.update(sql, rid, date, uid);
    }

    //我的收藏总条数
    @Override
    public int queryMyFavoriteCount(int uid) {
        String sql = "select count(*) from tab_favorite where uid=?";
        Integer count = 0;
        try {
            count = jdbcTemplate.queryForObject(sql, Integer.class, uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return count;
    }

    //我的收藏分页数据
    @Override
    public List<Route> queryMyFavoriteList(int uid, int beginIndex, int pageSize) {
        String sql = "select r.* from tab_route as r,tab_favorite as f where r.rid=f.rid and f.uid=? limit ?,?";
        List<Route> routeList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class), uid, beginIndex, pageSize);
        return routeList;
    }

    //获取旅游总记录,收藏排行榜页面的
    @Override
    public int queryRouteCount(String searchName, int startPrice, int endPrice) {
        List<Object> params = new ArrayList<>();
        String sql = "select count(*) from tab_route where 1=1";
        if (StringUtils.isNotEmpty(searchName)) {
            sql += " and rname like ?";
            params.add("%" + searchName + "%");
        }
        if (startPrice > 0) {
            sql += " and price>?";
            params.add(startPrice);
        }
        if (endPrice > 0 && endPrice > startPrice) {
            sql += " and price<?";
            params.add(endPrice);
        }

        Integer count = 0;
        try {
            count = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return count;
    }

    //旅游线路收藏排行降序+条件搜索查询
    @Override
    public List<Route> queryRouteByFavoriteDesc(int startIndex, int pageSize, String searchName, int startPrice, int endPrice) {
        String sql = "select * from tab_route where 1=1";
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotEmpty(searchName)) {
            sql += " and rname like ?";
            params.add("%" + searchName + "%");
        }
        if (startPrice > 0) {
            sql += " and price>?";
            params.add(startPrice);
        }
        if (endPrice > 0 && endPrice > startPrice) {
            sql += " and price<?";
            params.add(endPrice);
        }
        sql += " order by count desc limit ?,?";
        params.add(startIndex);
        params.add(pageSize);


        List<Route> routeList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class), params.toArray());
        return routeList;
    }

    //查询指定用户是否收藏了指定旅游线路
    @Override
    public Favorite queryRouteIsFavoriteByRid_Uid(int rid, int uid) {
        String sql = "select * from tab_favorite where rid=? and uid=?";
        Favorite favorite = null;
        try {
            favorite = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Favorite.class), rid, uid);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }

        return favorite;
    }
}
