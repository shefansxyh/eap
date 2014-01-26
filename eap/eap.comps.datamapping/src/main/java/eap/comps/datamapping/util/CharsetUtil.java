package eap.comps.datamapping.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;

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
public class CharsetUtil {
	public static String getString(byte[] bytes, String charset) {
		if (bytes == null) 
			return null;
		
		if (StringUtils.isNotBlank(charset)) {
			try {
				return new String(bytes, charset);
			} catch (UnsupportedEncodingException e) {
				return new String(bytes);
			}
		} else {
			return new String(bytes);
		}
	}
	
	public static String getString(String str, String srcCharest, String descCharset) {
		byte[] bytes = getBytes(str, srcCharest);
		if (bytes != null) {
			return getString(bytes, descCharset);
		}
		
		return str;
	}
	
	public static byte[] getBytes(String str, String charset) {
		if (str == null) 
			return null;
		
		if (StringUtils.isNotBlank(charset)) {
			try {
				return str.getBytes(charset);
			} catch (UnsupportedEncodingException e) {
				return str.getBytes();
			}
		} else {
			return str.getBytes();
		}
	}
}
