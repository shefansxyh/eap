package eap.web.demo.module.sample.form.clr;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import eap.base.BaseController;
import eap.web.demo.bizprocess.product.ucc.IProductUCC;

@Controller
public class ProductCLR extends BaseController {
	
	@Resource(name="productUCC")
	private IProductUCC productUCC;
	
	
	public static void main(String[] args) {
		Logger.getLogger(ProductCLR.class).info("111111111");
	}
	
}