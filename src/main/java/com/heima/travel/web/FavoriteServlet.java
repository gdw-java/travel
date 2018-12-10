package com.heima.travel.web;

import com.alibaba.fastjson.JSON;
import com.heima.travel.model.User;
import com.heima.travel.service.FavoriteService;
import com.heima.travel.service.imp.FavoriteServletImp;
import org.apache.commons.lang.StringUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/favorite")
public class FavoriteServlet extends BaseServlet {
    private FavoriteService favoriteService = new FavoriteServletImp();


    //添加收藏
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("isLogin", "noLogin");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }
        String json = favoriteService.addFavorite(rid, user.getUid());
        response.getWriter().write(json);

    }


    //我的收藏
    public void findMyFavoriteRoute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取session中用户信息并判断是否登录
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            HashMap<String, String> map = new HashMap<>();
            map.put("isLogin", "noLogin");
            String jsonString = JSON.toJSONString(map);
            response.getWriter().write(jsonString);
            return;
        }
        int curPage = 1;
        String currentPageStr = request.getParameter("curPage");
        if (StringUtils.isNotEmpty(currentPageStr)) {
            curPage = Integer.parseInt(currentPageStr);
        }
        String json = favoriteService.findMyFavoriteRoute(user.getUid(), curPage);
        response.getWriter().write(json);

    }

    //收藏排行榜分页
    public void favoriterankByPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int curPage = 1;
        String currentPageStr = request.getParameter("curPage");
        if (StringUtils.isNotEmpty(currentPageStr)) {
            curPage = Integer.parseInt(currentPageStr);
        }
        String searchName = request.getParameter("searchName");

        int startPrice = 0;
        String startPriceStr = request.getParameter("startPrice");
        if (StringUtils.isNotEmpty(startPriceStr)) {
            startPrice = Integer.parseInt(startPriceStr);
        }
        int endPrice = 0;
        String endPriceStr = request.getParameter("endPrice");
        if (StringUtils.isNotEmpty(endPriceStr)) {
            endPrice = Integer.parseInt(endPriceStr);
        }
        String json = favoriteService.favoriteRankByPage(curPage, searchName, startPrice, endPrice);
        response.getWriter().print(json);
    }


    //旅游详情页,点击收藏按钮是否显示判断
    public void findRouteIsFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            //没有登录
            Map<String, String> jsonmap = new HashMap<>();
            jsonmap.put("message", "noLogin");
            String jsonString = JSON.toJSONString(jsonmap);
            response.getWriter().print(jsonString);
            return;
        }
        int rid = Integer.parseInt(request.getParameter("rid"));

        String json = favoriteService.findRouteIsFavorite(rid, user.getUid());
        response.getWriter().print(json);
    }
}
