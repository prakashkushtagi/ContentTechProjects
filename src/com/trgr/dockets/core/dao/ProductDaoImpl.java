/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.trgr.dockets.core.entity.Product;

public class ProductDaoImpl implements ProductDao 
{
	private SessionFactory sessionFactory;

	public ProductDaoImpl() 
	{
	}

	@Override
	public Product findProductByProductCode(String productCode) 
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Product.class);
		criteria.add(Restrictions.eq("code", productCode));
		return (Product)criteria.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Product> fetchProductMap()
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Product.class);
		criteria.addOrder(Order.asc("code"));
		List<Product> productList = (List<Product>)criteria.list();
		
		HashMap<String,Product> productMap = new HashMap<String,Product>();
		
		for(Product product : productList)
		{
			productMap.put(product.getCode(),product);
		}
		
		return productMap;
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
