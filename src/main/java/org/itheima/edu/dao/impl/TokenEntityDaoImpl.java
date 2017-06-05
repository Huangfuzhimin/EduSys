package org.itheima.edu.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.itheima.edu.dao.TokenEntityDao;
import org.itheima.edu.entity.TokenEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class TokenEntityDaoImpl implements TokenEntityDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public TokenEntity findActive(Integer userId) {
		Session session = sessionFactory.openSession();

		Criteria criteria = session.createCriteria(TokenEntity.class);
		criteria.add(Restrictions.eq("userId", userId));
		criteria.add(Restrictions.eq("state", TokenEntity.STATE_ENABLE));

		@SuppressWarnings("unchecked")
		List<TokenEntity> list = criteria.list();
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public TokenEntity find(String token) {
		Session session = sessionFactory.openSession();

		Criteria criteria = session.createCriteria(TokenEntity.class);
		criteria.add(Restrictions.eq("token", token));

		@SuppressWarnings("unchecked")
		List<TokenEntity> list = criteria.list();
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public void add(TokenEntity entity) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.save(entity);
		transaction.commit();
	}

	@Override
	public void update(TokenEntity entity) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.update(entity);
		transaction.commit();
	}

}
