package com.clientid.apis;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.bson.Document;
import com.clientid.crypto.Base58;
import com.clientid.crypto.SHA256;
import com.clientid.dao.ApiKeyDAO;
import com.clientid.dto.ErrorDTO;
import com.clientid.initial.JobMongoDB;
import com.clientid.setting.AccountInfo;
import com.clientid.setting.HashKeyInfo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

@Path(DelClientID.PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DelClientID {
	static final String PATH = "/remove";

	@POST
	@Path("/{name}")
	public Response removeClientID(@PathParam("name") String name) {
		try {
			MongoDatabase db = JobMongoDB.getDB();
			MongoCollection<Document> collUserAccount = db.getCollection(AccountInfo.DATABASE_NAME);
			FindIterable<Document> accountDoc = collUserAccount.find(Filters.eq(AccountInfo.NAME, name));
			if (accountDoc.iterator().hasNext()) {
				String clientSecret = accountDoc.first().getString(AccountInfo.SECRET); //���lientId 
				String redisKey=Base58.encode(SHA256.digest(name + clientSecret).getBytes());
				collUserAccount.deleteOne(Filters.eq(AccountInfo.NAME, name));
			
				MongoCollection<Document> collHash = db.getCollection(HashKeyInfo.DATABASE_NAME);
				collHash.deleteOne(Filters.eq(HashKeyInfo.ID, redisKey));
				ApiKeyDAO dao = new ApiKeyDAO();
				dao.deleteByHashKey(redisKey);			
				return Response.status(Status.OK).build();
			} else {
				ErrorDTO errorDTO = new ErrorDTO();
				errorDTO.setCode(ErrorDTO.ERROR_NOT_FOUND_KEY);
				errorDTO.setField(AccountInfo.NAME);
				errorDTO.setMessage("does not exist in databasess");
				return Response.status(Status.OK).entity(errorDTO).build();
			}
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
}
