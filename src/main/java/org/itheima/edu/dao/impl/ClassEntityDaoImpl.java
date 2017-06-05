package org.itheima.edu.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.itheima.edu.dao.ClassEntityDao;
import org.itheima.edu.entity.ClassEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassEntityDaoImpl implements ClassEntityDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<ClassEntity> findByCampusWithoutComplete(Integer campusId) {
		Session session = sessionFactory.openSession();

		Criteria criteria = session.createCriteria(ClassEntity.class);
		criteria.add(Restrictions.eq("campus.id", campusId));
		criteria.add(Restrictions.eq("complete", false));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ClassEntity> findByCampus(Integer campusId) {
		Session session = sessionFactory.openSession();

		Criteria criteria = session.createCriteria(ClassEntity.class);
		criteria.add(Restrictions.eq("campus.id", campusId));
		return criteria.list();
	}

	@Override
	public ClassEntity find(Integer id) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(ClassEntity.class);
		criteria.add(Restrictions.eq("id", id));

		@SuppressWarnings("unchecked")
		List<ClassEntity> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}

		return null;
	}

	@Override
	public ClassEntity add(ClassEntity entity) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.save(entity);
		transaction.commit();

		return null;
	}

	@Override
	public void update(ClassEntity entity) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.update(entity);
		transaction.commit();
	}

}
