package eap.web.demo.bizprocess.product.bs.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import eap.web.demo.bizprocess.product.bs.IProductBS;
import eap.web.demo.bizprocess.product.dao.IProductDAO;

@Service("productBS")
public class ProductBSImpl implements IProductBS {
	
	@Resource(name="productDAO")
	private IProductDAO productDAO;
	
	
}