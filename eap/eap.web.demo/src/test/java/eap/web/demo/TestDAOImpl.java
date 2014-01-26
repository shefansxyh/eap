package eap.web.demo;

import org.springframework.stereotype.Repository;

import eap.comps.orm.jpa.BaseDAO;
import eap.web.demo.bo.TestBO;

//@Repository("testDAOImpl")
@Repository
public class TestDAOImpl extends BaseDAO<TestBO, Long> {
	
	public TestBO findByName(String name) {
//		return (TestBO) entityManager.createNamedQuery("findName").setParameter("name", name).getSingleResult();
		return null;
	}
}