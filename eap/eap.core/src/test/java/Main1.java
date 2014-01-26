import java.util.HashMap;
import java.util.Map;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;


public class Main1 {
	public static void main(String[] args) {
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression("'Hello World'");
		String message = (String) exp.getValue();
		System.out.println(message);
		
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("a", 1);
		m.put("b", 10);
		StandardEvaluationContext ctx = new StandardEvaluationContext(m);
		String r = parser.parseExpression("eap.util.DateUtil.currDate()").getValue(String.class);
		System.out.println(r);
			
	}
}
