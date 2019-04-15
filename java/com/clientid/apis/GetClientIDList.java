package com.clientid.apis;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.bson.Document;
import org.json.JSONObject;

import com.clientid.dto.ClientIDListDTO;
import com.clientid.initial.JobMongoDB;
import com.clientid.setting.HashKeyInfo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

@Path(GetClientIDList.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class GetClientIDList {
	static final String PATH = "/list";

	@GET
	public Response getClientIdList() {
		MongoDatabase db = JobMongoDB.getDB();
		MongoCollection<Document> coll = db.getCollection(HashKeyInfo.DATABASE_NAME);
		FindIterable<Document> hashKeyInDB = coll.find();
		MongoCursor<Document> cursor = hashKeyInDB.iterator();
		List<ClientIDListDTO> list = new ArrayList<>();
		while(cursor.hasNext()) {
			JSONObject object = new JSONObject(cursor.next().toJson());
			ClientIDListDTO cIdListDTO = new ClientIDListDTO(
					object.get("_id").toString(), 
					object.get(HashKeyInfo.HASH_VALUE).toString(), 
					object.get(HashKeyInfo.SECRET_KEY).toString(), 
					object.get(HashKeyInfo.ROLE).toString(), 
					object.get(HashKeyInfo.CLIENT_ID_HASH).toString());
			list.add(cIdListDTO);
		}
		
		return Response.status(Status.OK).entity(list).build();
	}
}
