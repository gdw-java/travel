package com.heima.travel.dao;

import com.heima.travel.model.Favorite;
import com.heima.travel.model.Route;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public interface FavoriteDao {
    Favorite queryRouteIsFavoriteByRid_Uid(int rid, int uid);

    int queryRouteCount(String searchName, int startPrice, int endPrice);

    List<Route> queryRouteByFavoriteDesc(int startIndex, int pageSize, String searchName, int startPrice, int endPrice);

    int queryMyFavoriteCount(int uid);

    List<Route> queryMyFavoriteList(int uid, int beginIndex, int pageSize);

    void addFavoriteByRidAndUid(String rid, int uid, String date, JdbcTemplate jdbcTemplate);

    void updateRouteCountByRid(String rid, JdbcTemplate jdbcTemplate);
}
