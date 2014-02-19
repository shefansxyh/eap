package _eap.comps.ruleengine;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import eap.comps.ruleengine.IRuleEngine;

public class Main2 {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("AC-test.xml");
		IRuleEngine ruleEngine = ctx.getBean("ruleEngine", IRuleEngine.class);
		
		ruleEngine.execute("p2", "1");
		
	}
}
