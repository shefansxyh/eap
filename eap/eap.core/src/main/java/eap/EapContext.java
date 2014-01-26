package eap;

import java.util.Locale;

import eap.base.UserDetailsVO;

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
public class EapContext {
	
	private static IEapContextHolder holder = new IEapContextHolder() {
		public UserDetailsVO getUserDetailsVO() {
			return null;
		}
		public Env getEnv() {
			Env env = new Env();
			env.init();
			return env;
		}
		public Locale getLocale() {
			return Locale.getDefault();
		}
		public String getIp() {
			return "127.0.0.1";
		}
		@Override
		public <T> T get(String key, Class<T> requireType) {
			return null;
		}
	};
	
	public static void init(IEapContextHolder holder) {
		EapContext.holder = holder;
	}
	public static IEapContextHolder getHolder() {
		return holder;
	}
	
	public static Env getEnv() {
		checkInit();
		return holder.getEnv();
	}
	
	public static UserDetailsVO getUserDetailsVO() {
		checkInit();
		return holder.getUserDetailsVO();
	}
	
	public static Locale getLocale() {
		checkInit();
		return holder.getLocale();
	}
	
	public static String getIp() {
		checkInit();
		return holder.getIp();
	}
	
	public static <T> T get(String key, Class<T> requireType) {
		checkInit();
		return holder.get(key, requireType);
	}
	
	private static void checkInit() {
		if (holder == null) {
			throw new IllegalStateException("holder not initialized");
		}
	}
}