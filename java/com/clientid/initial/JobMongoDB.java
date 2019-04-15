package com.clientid.initial;

import java.util.Properties;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;

public class JobMongoDB implements IInitialized {
	
	private static String Host 	= null;
	private static String User 	= null;
	private static String Pwd 		= null;
	private static String DBName 		= null;
	private static String connectString 		= null;
	private static MongoClientOptions.Builder options;
	private static MongoClient mongo = null;
	private static MongoDatabase mDB = null;
	private static Properties properties;
	public JobMongoDB(Properties properties) {
		JobMongoDB.properties = properties;
		JobMongoDB.init();
	}		

	public static MongoClient getMongo() throws RuntimeException {
		if (mongo == null) {
			MongoClientURI uri = new MongoClientURI(connectString, options);
			try {
				mongo = new MongoClient(uri);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return mongo;
	}
		
	public static MongoDatabase getDB() {
		if (mDB == null) {
			if (mongo == null) {					
				mongo = getMongo();
			}
			mDB = mongo.getDatabase(DBName);				
		}
		return mDB;
	}

	public static void init() {
		close();
		Host = JobMongoDB.properties.getProperty("MongoDB_HOST");
		User = JobMongoDB.properties.getProperty("MongoDB_USER");
		Pwd = JobMongoDB.properties.getProperty("MongoDB_PWD");
		DBName = JobMongoDB.properties.getProperty("MongoDB_DB");
		options = MongoClientOptions.builder()
				.writeConcern(WriteConcern.ACKNOWLEDGED)
                .connectionsPerHost(4)
                .maxConnectionIdleTime((60 * 1_000))
                .maxConnectionLifeTime((120 * 1_000));
		
		connectString = String.format("mongodb://%s:%s@%s:27017/%s", User, Pwd, Host, DBName);
	}
		
	public static void close() {
		try {
			if (mongo != null) {
				mongo.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mDB = null;
			mongo = null;
		}
	}

	@Override
	public String exec() {
		//System.out.println(connectString);
		return "MongoDB";
	}	
}
