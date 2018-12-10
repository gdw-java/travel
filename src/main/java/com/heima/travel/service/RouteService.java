package com.heima.travel.service;

public interface RouteService {
    String chooseRoute();

    String findRouteByPage(int cid, int curPage,String searchWord);

    String findRouteDetailByRid(int rid);
}
