package org.itheima.edu.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.itheima.edu.dao.TokenEntityDao;
import org.itheima.edu.dao.UserEntityDao;
import org.itheima.edu.entity.TokenEntity;
import org.itheima.edu.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class UserEntityDaoImpl implements UserEntityDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	TokenEntityDao tokenDao;

	@Override
	public List<UserEntity> findAll() {
		Session session = sessionFactory.openSession();
		String hql = "FROM UserEntity";
		Query query = session.createQuery(hql);
		return query.list();
	}

	@Override
	public void add(UserEntity user) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.save(user);
		transaction.commit();
	}

	@Override
	public UserEntity findById(Integer id) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(UserEntity.class);
		criteria.add(Restrictions.eq("id", id));
		List list = criteria.list();
		if (list != null && list.size() > 0) {
			return (UserEntity) list.get(0);
		}
		return null;
	}

	@Override
	public UserEntity findByAccount(String account) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(UserEntity.class);
		criteria.add(Restrictions.eq("account", account));
		List list = criteria.list();
		if (list != null && list.size() > 0) {
			return (UserEntity) list.get(0);
		}
		return null;
	}

	@Override
	public UserEntity findByPassword(String account, String password) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(UserEntity.class);
		criteria.add(Restrictions.eq("account", account));
		criteria.add(Restrictions.eq("password", password));
		List list = criteria.list();
		if (list != null && list.size() > 0) {
			return (UserEntity) list.get(0);
		}
		return null;
	}

	@Override
	public void update(UserEntity user) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(user);
		transaction.commit();
	}

	@Override
	public UserEntity findByToken(String token) {

		TokenEntity tokenEntity = tokenDao.find(token);
		if(tokenEntity == null){
			return null;
		}
		return findById(tokenEntity.getUserId());
	}

}
