package com.clientid.apis;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.bson.Document;

import com.clientid.crypto.SHA256;
import com.clientid.dao.ApiKeyDAO;
import com.clientid.dto.ClientIdDTO;
import com.clientid.dto.ErrorDTO;
import com.clientid.initial.JobMongoDB;
import com.clientid.setting.AccountInfo;
import com.clientid.setting.HashKeyInfo;
import com.clientid.token.Token;
import com.clientid.token.TokenRole;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

@Path(GetClientID.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class GetClientID {
	static final String PATH = "/client";
	private String clientId;
	private String secret;

	@GET
	@Path("/{name}")
	public Response getClientID(@PathParam("name") String name) {
		try {
			// ���lientID secret
			MongoDatabase db = JobMongoDB.getDB();
			MongoCollection<Document> coll = db.getCollection(AccountInfo.DATABASE_NAME);
			FindIterable<Document> myDoc = coll.find(Filters.eq(AccountInfo.NAME, name));
			if (myDoc.iterator().hasNext()) {
				ClientIdDTO dto = new ClientIdDTO();
				for (Document doc : myDoc) {
					dto.setClientId(doc.get(AccountInfo.CLIENT_ID).toString());
					clientId = doc.get(AccountInfo.CLIENT_ID).toString();
					dto.setSecret(doc.get(AccountInfo.SECRET).toString());
					secret = doc.get(AccountInfo.SECRET).toString();
				}
				// ������lientID secret���ash
				ApiKeyDAO dao = new ApiKeyDAO();
				String hashValue = SHA256.digest(clientId + secret);
				String hashKey = Token.digest(hashValue);
				dao.setHashKey(hashKey);
				dao.setHashValue(hashValue);
				dao.setSecretKey(secret);
				dao.setRole(TokenRole.TR_API);
				dao.setClientId(clientId);
				
				//撠ashKey雿_id,甈�hashkey hashvalue
				MongoCollection<Document> collHash = db.getCollection(HashKeyInfo.DATABASE_NAME);
				Document documentHash = new Document();
				documentHash.put(HashKeyInfo.ID, dao.getHashKey());
				documentHash.put(HashKeyInfo.HASH_VALUE, dao.getHashValue());
				documentHash.put(HashKeyInfo.SECRET_KEY, dao.getSecretKey());
				documentHash.put(HashKeyInfo.ROLE, dao.getRole());
				documentHash.put(HashKeyInfo.CLIENT_ID_HASH, dao.getClientId());
				FindIterable<Document> myDocHash = collHash.find(Filters.eq(HashKeyInfo.ID, hashKey));
				if(!myDocHash.iterator().hasNext()) {
					collHash.insertOne(documentHash);
					int time = 60 * 60 * 24 * 365;
					if (!dao.save(time)) {
						return Response.status(Status.INTERNAL_SERVER_ERROR).entity("insert redis error").build();
					}
				}
				return Response.status(Status.OK).entity(dto).build();
			} else {// 200 OK ��error message name瘝�mongo銝�
				ErrorDTO errorDTO = new ErrorDTO();
				errorDTO.setCode(ErrorDTO.ERROR_NOT_FOUND_KEY);
				errorDTO.setField("name");
				errorDTO.setMessage("does not exist in databasess");
				return Response.status(Status.OK).entity(errorDTO).build();
			}
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
}
