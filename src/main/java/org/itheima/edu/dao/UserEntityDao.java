package org.itheima.edu.dao;

import org.itheima.edu.entity.UserEntity;

import java.util.List;


public interface UserEntityDao {

	public List<UserEntity> findAll();

	public void add(UserEntity user);

	public UserEntity findById(Integer id);

	public UserEntity findByAccount(String account);

	public UserEntity findByPassword(String account, String password);

	public UserEntity findByToken(String token);

	public void update(UserEntity user);
}
