package eap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
public class TopicManager {
	
	private static Map<String, List<TopicListener>> listeneresMap = new ConcurrentHashMap<String, List<TopicListener>>();
	
	private static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
	public static void publish(String topic, Object data) {
		List<TopicListener> listeners = getListeners(topic);
		for (TopicListener listener : listeners) {
			dispatch(listener, data);
		}
	}
	
	public static void subscribe(String topic, TopicListener listener) {
		getListeners(topic).add(listener);
	}
	public static void unsubscribe(String topic) {
		getListeners(topic).clear();
	}
	public static void unsubscribe(String topic, TopicListener listener) {
		List<TopicListener> listeners = new ArrayList<TopicListener>(getListeners(topic));
		boolean changed = false;
		for (int i = 0; i < listeners.size(); i++) {
//			if (listeners.get(i).hashCode() == listener.hashCode()) {
			if (listeners.get(i) == listener) {
				listeners.remove(i);
				changed = true;
				break;
			}
		}
		if (changed) {
			listeneresMap.put(topic, listeners);
		}
	}
	
	private static List<TopicListener> getListeners(String topic) {
		List<TopicListener> listeners = listeneresMap.get(topic);
		if (listeners == null) {
			listeners = new CopyOnWriteArrayList<TopicListener>();
			listeneresMap.put(topic, listeners);
		} 
		
		return listeners;
	}
	
	public void setListeneresMap(Map<String, List<TopicListener>> listeneresMap) {
		TopicManager.listeneresMap = listeneresMap;
	}
	
	private static void dispatch(final TopicListener listener, final Object data) {
		executor.execute(
			new Runnable() {
				@Override
				public void run() {
					listener.onPublish(data);
				}
			}
		);
	}
	
	public static void main(String[] args) {
		TopicListener l1 = new L1();
		
		TopicManager.subscribe("a", l1);
		
		for (int j = 0; j < 100; j++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 10000; i++) {
						if (i == 999) {
						TopicManager.publish("a", i);
						}
					}
				}
			}).start();
		}
		
//		TopicManager.unsubscribe("a", l1);
		TopicManager.unsubscribe("a");
		
//		TopicManager.publish("a", 1);
		
	}
	
	static class L1 implements TopicListener {
		@Override
		public void onPublish(Object data) {
			System.out.println(data);
		}
	}
}