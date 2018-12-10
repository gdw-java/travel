package com.heima.travel.service;

public interface FavoriteService {
    String findRouteIsFavorite(int rid, int uid);

    String favoriteRankByPage(int curPage, String searchName, int startPrice, int endPrice);

    String findMyFavoriteRoute(int uid, int curPage);

    String addFavorite(String rid, int uid);
}
