package com.clientid.setting;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.clientid.crypto.Base58;
import com.clientid.crypto.SHA256;
import com.clientid.initial.JobMongoDB;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class UpdateClientSecret {
	private String oldName;
	private String oldSecret;
	private String newName;
	private String newSecret;
	
	public UpdateClientSecret(String oldName, String oldSecret, String newName, String newSecret) {
		this.oldName = oldName;
		this.oldSecret = oldSecret;
		this.newName = newName;
		this.newSecret = newSecret;
	}
	
	public void updateData() {
		try {
			String oldKey = Base58.encode(SHA256.digest(oldName + oldSecret).getBytes());
			MongoDatabase db = JobMongoDB.getDB();
			MongoCollection<Document> coll = db.getCollection(AccountInfo.DATABASE_NAME);
			Document document = new Document();
			document.put(AccountInfo.NAME, newName);
			document.put(AccountInfo.CLIENT_ID, newName);
			document.put(AccountInfo.SECRET, newSecret);
			Bson updateDocument = new Document("$set", document);
			coll.updateOne(Filters.eq("name", oldName), updateDocument);
			
			MongoCollection<Document> collHash = db.getCollection(HashKeyInfo.DATABASE_NAME);
			Document documentHash = new Document();
			documentHash.put(HashKeyInfo.HASH_KEY, Base58.encode(SHA256.digest(newName + newSecret).getBytes()));
			documentHash.put(HashKeyInfo.HASH_VALUE, SHA256.digest(newName + newSecret));
			documentHash.put(HashKeyInfo.CLIENT_ID_HASH, newName);
			documentHash.put(HashKeyInfo.SECRET_KEY, newSecret);
			Bson updateHashDocument = new Document("$set", documentHash);
			collHash.updateOne(Filters.eq("_id", oldKey), updateHashDocument);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
