package com.clientid.initial;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.bson.Document;
import com.clientid.dao.ApiKeyDAO;
import com.clientid.setting.HashKeyInfo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class WebAPI implements ServletContextListener {

	private JobManager jobManager = new JobManager();
	private JobRedis jobRedis;
	private JobProperties jobProperties;
	private JobMongoDB jobMongoDB;
	public static Properties properties;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		System.out.println("Initializing..." + dateFormat.format(new Date()));

		jobProperties = new JobProperties();
		jobProperties.exec();
		properties = jobProperties.getProperties();
		// -
		jobMongoDB = new JobMongoDB(properties);
		jobRedis = new JobRedis(properties);
		//
		jobManager.add(jobMongoDB);
		jobManager.add(jobRedis);
		jobManager.exec();
		//

		try {
			String clientId = null;
			String secret = null;
			MongoDatabase db = JobMongoDB.getDB();
			MongoCollection<Document> coll = db.getCollection(HashKeyInfo.DATABASE_NAME);
			FindIterable<Document> myDoc = coll.find();
			for (Document doc : myDoc) {
				ApiKeyDAO dao = new ApiKeyDAO();
				dao.setHashKey(doc.get(HashKeyInfo.ID).toString());
				dao.setHashValue(doc.get(HashKeyInfo.HASH_VALUE).toString());
				dao.setSecretKey(doc.get(HashKeyInfo.SECRET_KEY).toString());
				dao.setRole(doc.get(HashKeyInfo.ROLE).toString());
				dao.setClientId(doc.get(HashKeyInfo.CLIENT_ID_HASH).toString());
				clientId = dao.getClientId();
				
				int time = 60 * 60 * 24 * 365;
				dao.save(time);
			}
		} catch (Exception e) {
			System.out.println("insert to redis problem " + e.getMessage());
		}

		System.out.println("Initialized..." + dateFormat.format(new Date()));
		//
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		JobMongoDB.close();
	}
}
