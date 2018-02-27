package com.boot.mybatis;


import com.boot.mybatis.domian.DominantFuture;
import com.boot.mybatis.mapper.DominantFutureMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by SY on 2017/3/31.
 */
public class MybatisTest {

	public static void main(String[] args) throws IOException, SQLException {
		InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory sqlSession = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session = sqlSession.openSession(true);
		DominantFutureMapper mapper = session.getMapper(DominantFutureMapper.class);
		List<DominantFuture> all = mapper.findAll();
		System.out.println(all.size());
		session.close();
		SqlSession sessionNew = sqlSession.openSession(true);
		mapper=sessionNew.getMapper(DominantFutureMapper.class);
		System.out.println(mapper.findAll().size());
		sessionNew.close();


	}
}
