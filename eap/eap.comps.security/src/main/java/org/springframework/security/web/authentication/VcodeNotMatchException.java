package org.springframework.security.web.authentication;

import org.springframework.security.core.AuthenticationException;

/**
 * <p> Title: </p>
 * <p> Description: </p>
 * @作者 chiknin@gmail.com
 * @创建时间 
 * @版本 1.00
 * @修改记录
 * <pre>
 * 版本       修改人         修改时间         修改内容描述
 * ----------------------------------------
 * 
 * ----------------------------------------
 * </pre>
 */
public class VcodeNotMatchException extends AuthenticationException {
	public VcodeNotMatchException(String msg, Object extraInformation) {
		super(msg, extraInformation);
	}
}