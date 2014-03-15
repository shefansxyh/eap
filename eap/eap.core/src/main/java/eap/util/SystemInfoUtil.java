package eap.util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

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
public class SystemInfoUtil {
	
	private static MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
	private static RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
	
	private static List<String> PATH_RELATED_KEYS = new ArrayList<String>();
	private static List<String> IGNORE_THESE_KEYS = new ArrayList<String>();
	static {
		PATH_RELATED_KEYS.add("sun.boot.class.path");
		PATH_RELATED_KEYS.add("com.ibm.oti.vm.bootstrap.library.path");
		PATH_RELATED_KEYS.add("java.library.path");
		PATH_RELATED_KEYS.add("java.endorsed.dirs");
		PATH_RELATED_KEYS.add("java.ext.dirs");
		PATH_RELATED_KEYS.add("java.class.path");
		
		IGNORE_THESE_KEYS.addAll(PATH_RELATED_KEYS);
		IGNORE_THESE_KEYS.addAll(PATH_RELATED_KEYS);
		IGNORE_THESE_KEYS.add("line.separator");
		IGNORE_THESE_KEYS.add("path.separator");
		IGNORE_THESE_KEYS.add("file.separator");
	}
	
	
	public static float getJvmVersion()  {
		String property = System.getProperty("java.specification.version");
		try {
			return Float.valueOf(property).floatValue();
		} catch (Exception e) {
			throw new IllegalStateException("Invalid JVM version: '" + property + "'. " + e.getMessage());
		}
	}

	public static long getTotalHeapMemoryInit() {
		return memoryMXBean.getHeapMemoryUsage().getInit();
	}
	public static long getTotalHeapMemoryMax() {
		return memoryMXBean.getHeapMemoryUsage().getMax();
	}
	public static long getTotalHeapMemoryCommitted() {
		return memoryMXBean.getHeapMemoryUsage().getCommitted();
	}
	public static long getTotalHeapMemoryUsed() {
		return memoryMXBean.getHeapMemoryUsage().getUsed();
	}
	public static long getTotalHeapMemoryFree() {
		return getTotalHeapMemoryCommitted() - getTotalHeapMemoryUsed();
	}
	
	public static List<String[]> getMemoryPools() {
		List<String[]> pools = new ArrayList<String[]>();
		List<MemoryPoolMXBean> mxBeans = ManagementFactory.getMemoryPoolMXBeans();
		for (MemoryPoolMXBean mxBean : mxBeans) {
			pools.add(new String[] {mxBean.getName(), mxBean.getUsage().toString()});
		}
		return pools;
	}
	
	public static Map<String, String> getSystemProperties() {
		Properties props = System.getProperties();
		Map<String, String> properties = new TreeMap(props);
		properties.keySet().removeAll(IGNORE_THESE_KEYS);
		
		for (String key : properties.keySet()) {
			if (key.indexOf("password") != -1) {
				properties.put(key, "********");
			}
		}
		
		return properties;
	}
	
	public static void main(String[] args) {
		System.out.println(getSystemProperties());
	}
}