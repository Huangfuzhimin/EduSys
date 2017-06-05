package org.itheima.edu.dao;

import org.itheima.edu.entity.ClassEntity;

import java.util.List;


public interface ClassEntityDao {

	List<ClassEntity> findByCampusWithoutComplete(Integer campusId);
	
	List<ClassEntity> findByCampus(Integer campusId);

	ClassEntity find(Integer id);

	ClassEntity add(ClassEntity entity);

	void update(ClassEntity entity);

}
