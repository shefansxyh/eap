package eap.web.demo.bizprocess.product.ucc.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import eap.web.demo.bizprocess.product.bs.IProductBS;
import eap.web.demo.bizprocess.product.ucc.IProductUCC;

@Service("productUCC")
public class ProductUCCImpl implements IProductUCC {
	
	@Resource(name="productBS")
	private IProductBS productBS;

	@Override
	public void saveabc() {
		// TODO Auto-generated method stub
		
	}
	
	
}