package com.heima.travel.service.imp;

import com.alibaba.fastjson.JSON;
import com.heima.travel.dao.RouteDao;
import com.heima.travel.dao.imp.RouteDaoImp;
import com.heima.travel.model.PageBean;
import com.heima.travel.model.Route;
import com.heima.travel.model.RouteImg;
import com.heima.travel.service.RouteService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteServiceImp implements RouteService {
    private RouteDao routeDao=new RouteDaoImp();

    //获取旅游线路详情页面信息
     @Override
    public String findRouteDetailByRid(int rid) {
         Map<String,Object> detailMap=routeDao.queryRouteDetailByRid(rid);
        //获取旅游图片
         List<RouteImg> imgList =routeDao.queryRouteDetailImagesByRid(rid);
         detailMap.put("routeImage",imgList);
         String jsonString = JSON.toJSONString(detailMap);
         return jsonString;
    }

    //获取旅游线路分类列表并分页
    @Override
    public String findRouteByPage(int cid, int curPage,String searchWord) {
        PageBean pageBean=new PageBean();
        //获取该分类总记录数
       int totalCount= routeDao.queryRouteCountByCid(cid,searchWord);
       pageBean.setTotalCount(totalCount);
       //定义pageSize,每页显示的数量
        int pageSize=8;
        pageBean.setPageSize(pageSize);
        //计算总页数
        pageBean.setTotalPage((int)Math.ceil(totalCount*1.0/pageSize));
        //设置当前页
        pageBean.setCurrentPage(curPage);
        //计算limit开始查询的索引
        int startIndex= (curPage-1)*pageSize;
        //获取旅游分类信息的列表数据
        List<Route> routes=routeDao.queryRouteByPage(cid,startIndex,pageSize,searchWord);
        //把数据放到分页bean
        pageBean.setRouteList(routes);
        String jsonString = JSON.toJSONString(pageBean);
        return jsonString;
    }

    //获取首页人气,最新,主题旅游数据service
    @Override
    public String chooseRoute() {
        //1.人气旅游，是每个旅游线路收藏数量的降序获取前4条数据进行显示；
        List<Route> popularityRoute = routeDao.findPopularityRoute();
        //2.最新旅游，是每个旅游线路上架时间的降序获取前4条数据进行显示；
        List<Route> newRoute = routeDao.findNewRoute();
        //3.主题旅游，是每个旅游线路过滤为主题旅游的获取前4条数据进行显示；
        List<Route> themeRoute = routeDao.findTheamRoute();

        Map<String,List<Route>> routes = new HashMap<>();
        routes.put("popularityRoute",popularityRoute);
        routes.put("newRoute",newRoute);
        routes.put("themeRoute",themeRoute);
        String jsonString = JSON.toJSONString(routes);
        return jsonString;
    }
}
