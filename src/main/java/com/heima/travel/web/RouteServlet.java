package com.heima.travel.web;

import com.heima.travel.model.Route;
import com.heima.travel.service.RouteService;
import com.heima.travel.service.imp.RouteServiceImp;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route")
public class RouteServlet extends BaseServlet {
    private RouteService routeService =new RouteServiceImp();

    //旅游线路详情页
    public void findRouteDetailByRid(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String ridmeter =request.getParameter("rid");
        int rid=0;
        if (StringUtils.isNotEmpty(ridmeter)) {
            rid=Integer.parseInt(ridmeter);
            String json=routeService.findRouteDetailByRid(rid);
            response.getWriter().print(json);
        }

    }


    //获取首页人气,最新,主题旅游数据
    public void chooseRoute(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String jsonString=routeService.chooseRoute();
        response.getWriter().print(jsonString);
    }

    //获取分类列表并分页展示
    public void findRouteByPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int cid=Integer.parseInt(request.getParameter("cid"));
        int curPage=Integer.parseInt(request.getParameter("currentPage"));
        String searchWord=request.getParameter("searchWord");
        System.out.println("searchWord = " + searchWord);
        String json=routeService.findRouteByPage(cid,curPage,searchWord);
        
        response.getWriter().print(json);
    }

}
