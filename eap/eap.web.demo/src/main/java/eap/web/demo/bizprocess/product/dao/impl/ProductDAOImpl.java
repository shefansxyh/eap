package eap.web.demo.bizprocess.product.dao.impl;

import org.springframework.stereotype.Repository;

import eap.comps.orm.mybatis.BaseDAO;
import eap.web.demo.bizprocess.product.dao.IProductDAO;
import eap.web.demo.common.model.bo.ProductBO;

@Repository("productDAO")
public class ProductDAOImpl extends BaseDAO implements IProductDAO {

	@Override
	public int save(ProductBO productBO) {
		return sqlExecutor.insert("bizprocess.product.save", productBO);
	}
}