package com.clientid.apis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.bson.Document;
import com.clientid.crypto.DESSecretKey;
import com.clientid.dto.ErrorDTO;
import com.clientid.initial.JobMongoDB;
import com.clientid.setting.AccountInfo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

@Path(CreateClientID.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class CreateClientID {
	static final String PATH = "/v1";
	private String clientId;
	private String secret;

	@POST
	@Path("/{name}")
	public Response create(@PathParam("name") String name) {
		try {
			MongoDatabase db = JobMongoDB.getDB();
			MongoCollection<Document> coll = db.getCollection(AccountInfo.DATABASE_NAME);
			FindIterable<Document> myDoc = coll.find(Filters.and(Filters.eq(AccountInfo.NAME, name)));
			if (!myDoc.iterator().hasNext()) {// 瘝����ame撠張nsert
				// ���lientID Secret
				clientId = name;
				SecretKey key = DESSecretKey.generateKey();
				secret = DESSecretKey.getKeyString(key);

				// 撠��靘�lientID Secret摮UserAccount銵�
				Document document = new Document();
				document.put(AccountInfo.NAME, name);
				document.put(AccountInfo.CLIENT_ID, clientId);
				document.put(AccountInfo.SECRET, secret);
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
				document.put(AccountInfo.CREATE_DATE, dateFormat.format(new Date()));

				coll.insertOne(document);
			} else {
				String data = myDoc.iterator().next().toString();
				System.out.println(data);
				ErrorDTO errorDTO = new ErrorDTO();
				errorDTO.setCode(ErrorDTO.ERROR_NOT_FOUND_KEY);
				errorDTO.setField(AccountInfo.NAME);
				errorDTO.setMessage("already exist name:" + name);
				return Response.status(Status.OK).entity(errorDTO).build();
			}

			return Response.status(Status.CREATED).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
}
