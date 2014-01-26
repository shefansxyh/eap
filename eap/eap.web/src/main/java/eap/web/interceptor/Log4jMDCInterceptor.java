package eap.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import eap.util.HttpUtil;

public class Log4jMDCInterceptor extends HandlerInterceptorAdapter {
	
	public static final String MDC_KEY_IP = "ip";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (MDC.get(MDC_KEY_IP) != null) { // current thread already set ip
			MDC.put(MDC_KEY_IP, HttpUtil.getRemoteAddr(request));
		}
		
//		MDC.put("serverIp", System.getProperty("app.ip", InetAddress.getLocalHost().getHostAddress()));
//		MDC.put("http_method", request.getMethod().toLowerCase());
//		MDC.put("http_request_uri", request.getRequestURI());
//		MDC.put("http_user_agent", StringUtils.defaultIfBlank(request.getHeader("user-agent"), ""));
//		MDC.put("http_referer", StringUtils.defaultIfBlank(request.getHeader("referer"), ""));
		
//		Logger.getLogger(WebFilter.class).info(request.getRequestURL() + " " + request.getQueryString() + " " + request.getHeader("user-agent") + " " + request.getHeader("referer"));
		
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		MDC.clear();
	}
}