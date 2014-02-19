package eap;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.RetryNTimes;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.springframework.util.Assert;

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
public class UM {
	private static final Logger logger = Logger.getLogger(UM.class);
	
	public static final byte[] EMPTY_DATE = new byte[0];
	
	private static CuratorFramework client;
	private static boolean started = false;
//	private static MultiValueMap<String, NodeListener> nodeListenersMap = CollectionUtils.toMultiValueMap(new ConcurrentHashMap<String, List<NodeListener>>());
	private static Map<String, Map<NodeListener, List<Closeable>>> cache = new ConcurrentHashMap<String, Map<NodeListener, List<Closeable>>>();
	
	public static String appName;
	public static Long appId;
	public static String appVersion;
	public static String umConfigNS;
	public static String envPath;
	public static String serverPath;
	public static String logPath;
	public static String lockPath;
	public static String cliPath;
	
	public static String serverIp;
	public static String serverPort;
	public static String serverId;
	
	private static LeaderLatch leaderLatch;
	
	public static synchronized void start() throws Exception {
		String appUMServer = System.getProperty("app.UMServer"); // TODO , "localhost:2181"
		if (StringUtils.isNotBlank(appUMServer)) {
			Integer retryNum = Integer.getInteger("app.UMServer.retryNum", Integer.MAX_VALUE);
			Integer retryTimes = Integer.getInteger("app.UMServer.retryTimes", 5000);
			Integer connectionTimeoutMs = Integer.getInteger("app.UMServer.connectionTimeoutMs", 10000);
			
			final CountDownLatch connectedLatch = new CountDownLatch(1);
			
			client = CuratorFrameworkFactory.builder()
				.connectString(appUMServer)
				.retryPolicy(new RetryNTimes(retryNum, retryTimes))
				.connectionTimeoutMs(connectionTimeoutMs).build();
			client.getCuratorListenable().addListener(new CuratorListener() {
				public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
					if (connectedLatch.getCount() > 0 && event.getWatchedEvent() != null && event.getWatchedEvent().getState() == KeeperState.SyncConnected) {
						connectedLatch.countDown();
					}
				}
			});
			client.getUnhandledErrorListenable().addListener(new UnhandledErrorListener() {
				public void unhandledError(String message, Throwable e) {
					logger.error(message, e);
				}
			});
			client.start();
			connectedLatch.await();
			
			init();
			
			started = true;
		}
	}
	
	private static void init() throws Exception {
		appName = System.getProperty("app.name");
		Assert.hasText(appName, "system property 'app.name' must not be empty");
		String appIdStr = System.getProperty("app.id");
		Assert.hasText(appIdStr, "system property 'app.id' must not be empty");
		try {
			appId = new Long(appIdStr);
		} catch (Exception e) {
			throw new IllegalArgumentException("system property 'app.id' must be of type long");
		}
		appVersion = System.getProperty("app.version", "0");
		serverIp = System.getProperty("app.ip", InetAddress.getLocalHost().getHostAddress());
		serverPort = System.getProperty("app.port", "80");
		serverId = serverIp + ":" + serverPort;
		
		umConfigNS = String.format("/UM_CONFIG/APP/%s/%s", appName, appVersion);
		envPath = umConfigNS + "/env";
		serverPath = umConfigNS + "/server";
		logPath = umConfigNS + "/log";
		lockPath = umConfigNS + "/lock";
		cliPath = umConfigNS + "/cli";
		
		for (String p : new String[] {umConfigNS, envPath, serverPath, logPath, lockPath, cliPath}) {
			if (client.checkExists().forPath(p) == null) {
				client.create()
					.creatingParentsIfNeeded()
					.withMode(CreateMode.PERSISTENT)
					.withACL(Ids.OPEN_ACL_UNSAFE)
					.forPath(p, EMPTY_DATE);
			}
		}
		client.create()
			.creatingParentsIfNeeded()
			.withMode(CreateMode.EPHEMERAL)
			.withACL(Ids.OPEN_ACL_UNSAFE)
			.forPath(serverPath + "/" + serverId, appIdStr.getBytes());
		
		leaderLatch = new LeaderLatch(client, serverPath + "/leader", serverId);
		leaderLatch.start();
	}
	
	public static void addListener(final String path, final NodeListener listener) throws Exception {
		if (isStarted()) {
			if (cache.get(path) != null && cache.get(path).get(listener) != null) { // listener exists 
				return;
			}
			
			final NodeCache nodeCache = new NodeCache(client, path);
			nodeCache.getListenable().addListener(new NodeCacheListener() {
				@Override
				public void nodeChanged() throws Exception {
					listener.nodeChanged(client, nodeCache.getCurrentData());
				}
			});
			nodeCache.start();
			
			final PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path, false);
			pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
				@Override
				public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
					listener.childEvent(client, event);
				}
			});
			pathChildrenCache.start();
			
			putToCache(path, listener, nodeCache, pathChildrenCache);
		}
	}
	private static void putToCache(String path, NodeListener listener, Closeable... closeables) {
		Map<NodeListener, List<Closeable>> m = cache.get(path);
		if (m == null) {
			m = new ConcurrentHashMap<NodeListener, List<Closeable>>();
			cache.put(path, m);
		}
		m.put(listener, Arrays.asList(closeables));
	}
	
	public static void removeListener(String path, NodeListener listener) throws Exception {
		Map<NodeListener, List<Closeable>> m = cache.get(path);
		if (m != null && m.size() > 0 && m.containsKey(listener)) {
			for (Closeable closeable : m.get(listener)) {
				try {
					closeable.close();
				} catch (IOException e) {}
			}
		}
	}
	public static void removeListener(String path) throws Exception {
		Map<NodeListener, List<Closeable>> m = cache.get(path);
		if (m != null && m.size() > 0) {
			for (NodeListener listener : m.keySet()) {
				removeListener(path, listener);
			}
		}
	}
	public static void removeAllListener() throws Exception {
		for (String path : cache.keySet()) {
			removeListener(path);
		}
	}
	
	public static boolean isLeader() {
		if (isStarted() && leaderLatch != null) {
			try {
				return serverId.equals(leaderLatch.getLeader().getId());
			} catch (Exception e) {
				logger.debug(e.getMessage(), e);
				return false;
			}
		} else {
			return true;
		}
	}
	
	public static synchronized void stop() {
		if (isStarted() && client != null && client.getState() == CuratorFrameworkState.STARTED) {
			if (leaderLatch != null) {
				try {
					leaderLatch.close();
				} catch (IOException e) {}
			}
			
			client.close();
			
			started = false;
		}
	}
	
	
	public static boolean isStarted() {
		return started;
	}
	
	public static boolean isEnabled() {
		String appUMServer = System.getProperty("app.UMServer"); // TODO , "localhost:2181"
		return StringUtils.isNotBlank(appUMServer);
	}
	
	public static class NodeListener {
		public void nodeChanged(CuratorFramework client, ChildData childData) throws Exception {
		}
		public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		System.out.println();
		UM.start();
//		for (int i = 1; i < 100; i++) {
//			System.out.println(UM.isLeader());
			
			NodeListener l1 = new NodeListener() {
				@Override
				public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
					System.out.println("childEvent : " + event.toString());
				}
				@Override
				public void nodeChanged(CuratorFramework client, ChildData childData) throws Exception {
					System.out.println("nodeChanged : " + childData.toString());
				}
			};
			UM.addListener(UM.envPath, l1);
//			UM.addListener(UM.envPath, l1);
			
			Thread.sleep(5000000);
			
//			UM.removeListener(UM.envPath, l1);
//			
//			Thread.sleep(5000);
//			
//			UM.addListener(UM.envPath, l1);
			
			UM.removeListener(UM.envPath);
			
			Thread.sleep(5000);
//		}
		
		
		UM.stop();
	}
}