package com.yulchon.dao;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.yulchon.domain.Users;

@Repository
public class UserDaoImpl implements UserDao{
	
	@Resource(name="session")
	private SqlSession sqlSession;
	
	@Override
	public Users getLoginUser(Users users) {
		return sqlSession.selectOne("users.getLoginUser", users);
	}
}
