package eap.sparrow.pagestats;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CollectServlet extends HttpServlet {
	
	private static final long serialVersionUID = 3361146195637693041L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		response.setHeader("Cache-Control", "private, max-age=0, no-cache");
		response.setHeader("Connection", "close");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("application/javascript");
		
		StringBuilder logMsg = new StringBuilder();
		logMsg.append(getRemoteAddr(request)).append("\t");
		logMsg.append(request.getQueryString()).append("\t");
		String referer = request.getHeader("referer");
		if (referer != null && referer.length() > 0) {
			logMsg.append(referer);
		}
		logMsg.append("\t");
		String cookie = request.getHeader("cookie");
		if (cookie != null && cookie.length() > 0) {
			logMsg.append(cookie);
		}
		logMsg.append("\t");
		String userAgent = request.getHeader("user-agent");
		if (userAgent != null && userAgent.length() > 0) {
			logMsg.append(userAgent);
		}
		logMsg.append("\t");
		
		System.out.println(logMsg.toString());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private String getParam(HttpServletRequest request, String name) {
		return request.getParameter(name);
	}
	
	private String getRemoteAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");  
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip != null && ip.length() > 0) {
			String[] ipArr = ip.split(" |,");
			if (ipArr != null && ipArr.length > 0) {
				return ipArr[ipArr.length - 1];
			}
		}
		
		return ip;
	}
}