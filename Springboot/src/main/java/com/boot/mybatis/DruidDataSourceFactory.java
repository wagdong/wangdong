package com.boot.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;


/**
 * Created by SY on 2017/3/31.
 */
public class DruidDataSourceFactory extends UnpooledDataSourceFactory {

    public DruidDataSourceFactory(){
        this.dataSource = new DruidDataSource();
    }
}
