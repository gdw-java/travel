package com.heima.travel.service.imp;

import com.alibaba.fastjson.JSON;
import com.heima.travel.dao.FavoriteDao;
import com.heima.travel.dao.imp.FavaoriteDaoImp;
import com.heima.travel.model.Favorite;
import com.heima.travel.model.PageBean;
import com.heima.travel.model.Route;
import com.heima.travel.service.FavoriteService;
import com.heima.travel.utils.JdbcTemplateUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteServletImp implements FavoriteService {

    private FavoriteDao favoriteDao = new FavaoriteDaoImp();

    //添加收藏
    @Override
    public String addFavorite(String rid, int uid) {
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());

        /**
         *  因为事务的管理是通过connection对象来控制的，所以在service层
         *  我们需要获取连接Connection对象，dao层的JdbcTemplate的底层也需要涉及到Connection
         *  对象的使用，dao层的connection和service层的connection是否有必要保持同一个？
         *  是必须要保证是同一个！！！
         */
        //1.创建jdbcTemplate对象
        JdbcTemplate jdbcTemplate = JdbcTemplateUtil.getJdbcTemplate();
        //2.开启jdbcTemplate的事务管理器，将数据库连接池中的对象和当前线程实现绑定。
        TransactionSynchronizationManager.initSynchronization();
        //3.获取当前jdbcTemplate使用的Connection连接对象
        Connection connection = DataSourceUtils.getConnection(JdbcTemplateUtil.getDataSource());
        try {
            //设置mysql事务为手动提交
            connection.setAutoCommit(false);
            //添加收藏
            favoriteDao.addFavoriteByRidAndUid(rid, uid, date, jdbcTemplate);
            //更新tab_route表的收藏次数
            favoriteDao.updateRouteCountByRid(rid, jdbcTemplate);
            connection.commit();
            map.put("message", "success");
        } catch (Exception e) {
            //出现异常收藏失败
            map.put("message", "fail");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            ////释放连接对象与当前线程的绑定
            TransactionSynchronizationManager.clearSynchronization();

            //设置连接对象为自动提交
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return JSON.toJSONString(map);
    }

    //获取我的收藏分页信息 service层
    @Override
    public String findMyFavoriteRoute(int uid, int curPage) {
        //创建pageBean
        PageBean pageBean = new PageBean();
        //获取我的收藏总数
        int totalCount = favoriteDao.queryMyFavoriteCount(uid);
        pageBean.setTotalCount(totalCount);
        //设置pageSize
        int pageSize = 12;
        pageBean.setPageSize(pageSize);
        //计算总页数
        pageBean.setTotalPage((int) Math.ceil(totalCount * 1.0 / pageSize));
        pageBean.setCurrentPage(curPage);//设置当前页和上一页下一页和开始结束页码点进去这个方法便知详情
        //计算分页查询开始索引
        int beginIndex = (curPage - 1) * pageSize;
        //获取分页route数据
        List<Route> routes = favoriteDao.queryMyFavoriteList(uid, beginIndex, pageSize);
        pageBean.setRouteList(routes);
        String jsonString = JSON.toJSONString(pageBean);
        return jsonString;
    }

    //收藏排行榜分页
    @Override
    public String favoriteRankByPage(int curPage, String searchName, int startPrice, int endPrice) {
        PageBean pageBean = new PageBean();
        //获取总记录
        int totalCount = favoriteDao.queryRouteCount(searchName, startPrice, endPrice);
        //设置每页显示条数
        int pageSize = 8;
        pageBean.setPageSize(pageSize);
        pageBean.setTotalCount(totalCount);
        //计算总页数
        pageBean.setTotalPage((int) Math.ceil(totalCount * 1.0 / pageSize)); //总页数
        pageBean.setCurrentPage(curPage);
        int startIndex = (curPage - 1) * pageSize; //设置分页查询的开始索引
        List<Route> routes = favoriteDao.queryRouteByFavoriteDesc(startIndex, pageSize, searchName, startPrice, endPrice);//分页查询
        pageBean.setRouteList(routes);
        return JSON.toJSONString(pageBean);
    }

    //判断当前登录的用户是否收藏了该rid
    @Override
    public String findRouteIsFavorite(int rid, int uid) {
        Favorite favorite = favoriteDao.queryRouteIsFavoriteByRid_Uid(rid, uid);
        Map<String, String> jsonMap = new HashMap<>();
        if (favorite == null) {
            jsonMap.put("message", "noFavorite");
        } else {
            jsonMap.put("message", "yesFavorite");
        }
        return JSON.toJSONString(jsonMap);
    }
}
