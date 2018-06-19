/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.ProductDao;
import com.trgr.dockets.core.entity.Product;

public class ProductServiceImpl implements ProductService
{
	private ProductDao productDao;
	

	public ProductServiceImpl() 
	{

	}
	@Override
	@Transactional(readOnly=true)
	public Product findProductByProductCode(String productCode) 
	{
		return productDao.findProductByProductCode(productCode);
	}

	@Override
	@Transactional(readOnly=true)
	public Map<String, Product> fetchProductMap()
	{
		return productDao.fetchProductMap();
	}
	public ProductDao getProductDao() {
		return productDao;
	}
	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}
}
