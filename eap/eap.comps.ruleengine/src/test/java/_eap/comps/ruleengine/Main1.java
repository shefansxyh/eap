package _eap.comps.ruleengine;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.Activation;
import org.drools.runtime.rule.AgendaFilter;

public class Main1 {
	public static void main(String[] args) {
		KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		builder.add(ResourceFactory.newClassPathResource("1.drl"), ResourceType.DRL);
		builder.add(ResourceFactory.newClassPathResource("2.drl"), ResourceType.DRL);
		KnowledgeBuilderErrors errors = builder.getErrors();
		if (errors.size() > 0) {
			   for (KnowledgeBuilderError error: errors) {
			    System.err.println(error.getMessage());
			   }
			   throw new IllegalArgumentException("Could not parse knowledge.");
			  }
		
		KnowledgeBase base = builder.newKnowledgeBase();
		base.addKnowledgePackages(builder.getKnowledgePackages());
		StatefulKnowledgeSession session = base.newStatefulKnowledgeSession();
		KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(session, "test");
		
		session.fireAllRules(new AgendaFilter() {
			public boolean accept(Activation activation) {
				System.out.println(activation.getRule().getName());
				return false;
			}
		});
		
//		session.insert(null);
		session.fireAllRules();
		
		session.dispose();
		logger.close();
		
	}
}
