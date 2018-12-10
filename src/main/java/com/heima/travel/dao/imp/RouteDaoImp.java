package com.heima.travel.dao.imp;

import com.heima.travel.dao.RouteDao;
import com.heima.travel.model.Route;
import com.heima.travel.model.RouteImg;
import com.heima.travel.utils.JdbcTemplateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteDaoImp implements RouteDao {
    private JdbcTemplate jdbcTemplate = JdbcTemplateUtil.getJdbcTemplate();

    //获取旅游详情图片
    @Override
    public List<RouteImg> queryRouteDetailImagesByRid(int rid) {
        String sql = "select * from tab_route_img where rid=?";
        List<RouteImg> routeImgs = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RouteImg.class), rid);
        return routeImgs;
    }

    //获取旅游线路详情页面信息
    @Override
    public Map<String, Object> queryRouteDetailByRid(int rid) {
        //String sql ="select * from tab_route tr JOIN tab_category tc on tr.cid=tc.cid join tab_seller ts on tr.sid=ts.sid where tr.cid=5"
        String sql = "select * from tab_route tr,tab_category tc,tab_seller ts where tr.cid=tc.cid and tr.sid=ts.sid and tr.rid=?";
        Map<String, Object> map = null;
        try {
            map = jdbcTemplate.queryForMap(sql, rid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return map;
    }

    //获取指定分类的旅游线路列表,分页查询 加搜索
    @Override
    public List<Route> queryRouteByPage(int cid, int startIndex, int pageSize, String searchWord) {
        ArrayList<Object> params = new ArrayList<>();
        String sql = "select * from tab_route where cid=? ";
        params.add(cid);
        if (StringUtils.isNotEmpty(searchWord)) {
            sql += " and rname like ?";
            params.add("%" + searchWord + "%");
        }
        sql += "limit ?,?";
//        System.out.println("sql = " + sql);
        params.add(startIndex);
        params.add(pageSize);

        List<Route> routeList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class), params.toArray());
        return routeList;
    }

    //获取指定分类下旅游线路总数         加了搜索功能
    @Override
    public int queryRouteCountByCid(int cid, String searchWord) {

        ArrayList<Object> params = new ArrayList<>();
        String sql = "select count(*) from tab_route where cid=? ";
        params.add(cid);
        if (StringUtils.isNotEmpty(searchWord)) {
            sql += "and rname like ? ";
            params.add("%" + searchWord + "%");
        }
        Integer count = 0;
        try {
            count = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return count;
    }

    //1.人气旅游，是每个旅游线路收藏数量的降序获取前4条数据进行显示；
    @Override
    public List<Route> findPopularityRoute() {
        String sql = "select * from tab_route order by count desc limit 0,4";
        List<Route> routeList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class));
        return routeList;
    }

    //2.最新旅游，是每个旅游线路上架时间的降序获取前4条数据进行显示；
    @Override
    public List<Route> findNewRoute() {
        String sql = "select * from tab_route order by rdate desc limit 0,4";
        List<Route> routeList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class));
        return routeList;
    }

    //3.主题旅游，是每个旅游线路过滤为主题旅游的获取前4条数据进行显示；
    @Override
    public List<Route> findTheamRoute() {
        String sql = "select * from tab_route WHERE isThemeTour=1 limit 0,4";
        List<Route> routeList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class));
        System.out.println(routeList);
        return routeList;
    }
}
