package com.heima.travel.dao;

import com.heima.travel.model.Route;
import com.heima.travel.model.RouteImg;

import java.util.List;
import java.util.Map;

public interface RouteDao {
    List<Route> findPopularityRoute();

    List<Route> findNewRoute();

    List<Route> findTheamRoute();

    int queryRouteCountByCid(int cid,String searchWord);

    List<Route> queryRouteByPage(int cid, int startIndex, int pageSize,String searchWord);

    Map<String,Object> queryRouteDetailByRid(int rid);

    List<RouteImg> queryRouteDetailImagesByRid(int rid);
}
