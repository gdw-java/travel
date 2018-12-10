package com.heima.travel.dao.imp;

import com.heima.travel.dao.UserDao;
import com.heima.travel.model.User;
import com.heima.travel.utils.JdbcTemplateUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;


import java.util.List;

public class UserDaoImp implements UserDao {

     private JdbcTemplate jdbcTemplate = JdbcTemplateUtil.getJdbcTemplate();


     //用户登录dao
    @Override
    public User lqueryUserLogin(String email, String pwd) {
       String sql="select * from tab_user where (email=? or username=?) and password=?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), email, email, pwd);
            return user;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }

    }

    //用户激活
    @Override
    public User userActive(String code) {
      String sql ="select * from tab_user where code=?";
        User user=null;
      try {
           user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), code);
           sql="UPDATE tab_user set status=1 where uid=?";
           jdbcTemplate.update(sql,user.getUid());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }
    //修改密码
    @Override
    public void changePwdByEmai(String email, String newPwd) {
        String sql="update tab_user set password=? where email=?";
        jdbcTemplate.update(sql,newPwd,email);
    }

    //用户注册
    @Override
    public int addUser(User user) {
       String sql ="insert into tab_user values(null,?,?,?,?,?,?,?,?,?)";
        int update = 0;
        try {
            update = jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getName(), user.getBirthday(),
                    user.getSex(), user.getTelephone(), user.getEmail(), 0, user.getCode());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return 0;
        }
        return update;
    }

    /**
     * 查询邮箱是否存在
     * @param email
     * @return
     */

    @Override
    public List<User> queryEmail(String email) {
        String sql="select * from tab_user where email=?";
        List<User> query = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email);
        System.out.println(query.size());
        return query;
    }

    /**
     * 查询用户名是否存在
     */
    @Override
    public List<User> queryUserName(String username) {
        String sql = "select * from tab_user where username=?";
        List<User> query = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class),username);
        return query;
    }
}
