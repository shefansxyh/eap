package eap.web.demo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.bson.types.ObjectId;
import org.springframework.util.StopWatch;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

public class MongodbMain {
	public static void main(String[] args) throws IOException, InterruptedException {
		MongoClient client = new MongoClient(Arrays.asList(
			new ServerAddress("127.0.0.1", 10001),
			new ServerAddress("127.0.0.1", 10002)
//			new ServerAddress("192.168.1.51", 10003))
		));
		DB db = client.getDB("eDoc");
		db.dropDatabase();
//		DBCollection col = db.getCollection("order");
//		
//		BasicDBObject doc = new BasicDBObject();
//		doc.put("id", 1);
//		doc.put("msg", "hello world mongoDB in Java");
//		col.insert(doc);
//		
//		BasicDBObject query = new BasicDBObject();
//		query.put("id", 1);
//		
//		DBCursor cursor = col.find(query);
//		while (cursor.hasNext()) {
//			System.out.println(cursor.next());
//		}
		
//		final GridFS fs = new GridFS(db, "policyPdf");
//		
//		StopWatch sw = new StopWatch();
//		sw.start("save");
//		final CountDownLatch cdl = new CountDownLatch(1000);
//		for (int t = 0; t < 10; t++) {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					for (int i = 0; i < 100; i++) {
//						try {
//							ObjectId id = new ObjectId();
//							InputStream input = new FileInputStream("src/test/resources/Drools 规则引擎的研究与应用.pdf");
//							GridFSInputFile fsFile = fs.createFile(input);
//							fsFile.setId(id);
//							fsFile.setFilename("/eDoc/policy/" + id + ".pdf");
//							fsFile.save();
//							input.close();
//							
//							cdl.countDown();
////							System.out.println(i);
//						} catch (Exception e) {
//						}
//					}
//				}
//			}).start();
//		}
//		cdl.await();
//		sw.stop();
		
//		sw.start("fileList");
//		DBCursor cursor = fs.getFileList();
//		while (cursor.hasNext()) {
////			cursor.next();
//			System.out.println(cursor.next());
//		}
//		cursor.close();
//		sw.stop();
		
//		fs.remove(id);
//		fs.remove(new ObjectId("530ddcd30364ed7119bae531"));
		
//		GridFSDBFile dbFile = fs.find(new ObjectId("530ddcd30364ed7119bae531"));
//		dbFile.writeTo("src/test/resources/Drools.pdf");
		
//		System.out.println(sw.prettyPrint());
	}
}
