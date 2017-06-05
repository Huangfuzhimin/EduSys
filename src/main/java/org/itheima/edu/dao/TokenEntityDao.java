package org.itheima.edu.dao;


import org.itheima.edu.entity.TokenEntity;

public interface TokenEntityDao {

	TokenEntity findActive(Integer userId);

	TokenEntity find(String token);
	
	void add(TokenEntity entity);

	void update(TokenEntity entity);
	
	
}
