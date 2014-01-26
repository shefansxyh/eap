package eap.web.demo;

import junit.framework.TestCase;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import eap.web.demo.bizprocess.product.dao.IProductDAO;
import eap.web.demo.bo.TestBO;
import eap.web.demo.common.model.bo.ProductBO;

public class JpaTest extends TestCase {
	public void test1() {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("AC-test1.xml");
		ITestDAO testDAO = ctx.getBean(ITestDAO.class);
//		System.out.println("testDAO  " + testDAO);
//		
		TestBO bo = new TestBO();
//		bo.setId(new IdWorker(1).nextId());
		bo.setName("name");
		testDAO.save(bo);
//		
//		System.out.println(testDAO.findByName("name"));
		
//		IProductDAO pd = ctx.getBean("productDAO", IProductDAO.class);
//		
//		ProductBO productBO = new ProductBO();
//		productBO.setProductName("PD1");
//		pd.save(productBO);
	}
}
