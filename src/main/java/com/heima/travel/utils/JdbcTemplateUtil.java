package com.heima.travel.utils;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author buguniao
 * @date 2018-07-01:10:51
 * @copy 本项目版权归java31期所有，未经许可不得私自复制，否则，要承当法律责任
 */
public class JdbcTemplateUtil {

    private static ComboPooledDataSource dataSource = null;
    private static JdbcTemplate jdbcTemplate = null;


    static {
        dataSource = new ComboPooledDataSource();
    }


    /**
     * 获取jdbcTemplate
     *
     * @return
     */
    public static JdbcTemplate getJdbcTemplate() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }

    //获取jdbcTemplate使用的连接池
    public static DataSource getDataSource() {
        return dataSource;
    }

}
