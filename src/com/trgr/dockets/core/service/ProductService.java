package com.trgr.dockets.core.service;

import java.util.Map;

import com.trgr.dockets.core.entity.Product;


/**
 * A product service.
 */
public interface ProductService 
{
	public Product findProductByProductCode(String productCode);

	public Map<String, Product> fetchProductMap();
	
}
