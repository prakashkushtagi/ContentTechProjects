package com.trgr.dockets.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.VendorCounty;
@Transactional
public class VendorCountyDaoImpl implements VendorCountyDao {
	
	private SessionFactory sessionFactory;
	
	public VendorCountyDaoImpl() {
		super();
	}
	
	public VendorCountyDaoImpl (SessionFactory hibernateSessionFactory) {
		this.sessionFactory = hibernateSessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<VendorCounty> getActiveVendorCounties(Long vendorId) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(VendorCounty.class);
		criteria.add(Restrictions.eq("primaryKey.vendorId", vendorId));
		criteria.add(Restrictions.eq("active", 'Y'));
		return criteria.list();
	}
}
