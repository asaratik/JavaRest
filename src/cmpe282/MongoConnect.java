package cmpe282;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBCursor;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;

import sun.applet.Main;

import com.mongodb.client.model.Projections;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;

public class MongoConnect {
	private final String HOST = "localhost";
	private final String DATABASE = "cmpe282ashok639";
	private final int PORT = 27017;
	private MongoClient mongoClient;
	private MongoDatabase database;
	private MongoCollection<Document> empCollection;
	private MongoCollection<Document> projCollection;

	private void connect() {
		mongoClient = new MongoClient(HOST, PORT);
		database = mongoClient.getDatabase(DATABASE);
		empCollection = database.getCollection("employees").withWriteConcern(new WriteConcern(1));
		projCollection = database.getCollection("projects").withWriteConcern(new WriteConcern(1));
	}

	public String getEmployees() {
		connect();
		FindIterable<Document> it = empCollection.find().projection(Projections.excludeId());
		ArrayList<String> employees = new ArrayList<>();

		for (Document doc : it) {
			employees.add(doc.toJson());
		}
		return employees.toString();
	}
	
	public String getProjects() {
		connect();
		FindIterable<Document> it = projCollection.find().projection(Projections.excludeId());
		ArrayList<String> projects = new ArrayList<>();

		for (Document doc : it) {
			projects.add(doc.toJson());
		}
		return projects.toString();

	}

	public boolean insertEmployee(String employee) {
		try {
			connect();
			JsonObject object = (new JsonParser()).parse(employee).getAsJsonObject();
			long id = getEmployeeCount() + 1;
			Document empDoc = new Document("id", id)
					.append("firstName", object.get("firstName").toString().replaceAll("^.|.$", ""))
					.append("lastName", object.get("lastName").toString().replaceAll("^.|.$", ""));
			empCollection.insertOne(empDoc);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public boolean insertProject(String project) {
		try {
			connect();
			JsonObject object = (new JsonParser()).parse(project).getAsJsonObject();
			long id = getProjectCount() + 1;
			Document projDoc = new Document("id", id)
					.append("name", object.get("name").toString().replaceAll("^.|.$", ""))
					.append("budget", object.get("budget").toString().replaceAll("^.|.$", ""));
			projCollection.insertOne(projDoc);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public String getEmployee(int id) {
		try {
			connect();
			return empCollection.find(eq("id", id)).projection(Projections.excludeId()).first().toJson();
		} catch (NullPointerException e) {
			return "";
		}
	}

	public String getProject(int id) {
		try {
			connect();
			return projCollection.find(eq("id", id)).projection(Projections.excludeId()).first().toJson();
		} catch (NullPointerException e) {
			return "";
		}
	}

	public boolean deleteEmployee(int id) {
		connect();
		if (empCollection.deleteOne(eq("id", id)).getDeletedCount() == 0) {
			return false;
		}
		return true;
	}

	public boolean deleteProject(int id) {
		connect();
		if (projCollection.deleteOne(eq("id", id)).getDeletedCount() == 0) {
			return false;
		}
		return true;

	}

	public boolean updateEmployee(String employee, int id) {
		try {
			connect();
			JsonObject object = (new JsonParser()).parse(employee).getAsJsonObject();
			try {
				empCollection.find(eq("id", id)).first().toJson();
				if (object.has("firstName")) {
					empCollection.updateOne(eq("id", id), new Document("$set",
							new Document("firstName", object.get("firstName").toString().replaceAll("^.|.$", ""))));
				}
				if (object.has("lastName")) {
					empCollection.updateOne(eq("id", id), new Document("$set",
							new Document("lastName", object.get("lastName").toString().replaceAll("^.|.$", ""))));
				}
			} catch (NullPointerException ex) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean updateProject(String project, int id) {
		try {
			connect();
			JsonObject object = (new JsonParser()).parse(project).getAsJsonObject();
			try {
				projCollection.find(eq("id", id)).first().toJson();
				if (object.has("name")) {
					projCollection.updateOne(eq("id", id), new Document("$set",
							new Document("name", object.get("name").toString().replaceAll("^.|.$", ""))));
				}
				if (object.has("budget")) {
					projCollection.updateOne(eq("id", id), new Document("$set",
							new Document("budget", object.get("budget").toString().replaceAll("^.|.$", ""))));
				}
			} catch (NullPointerException ex) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public long getEmployeeCount() {
		connect();
		JsonObject object1 = null;
		try{
			JsonObject object = (new JsonParser()).parse(empCollection.find().sort(new BasicDBObject("id", -1)).first().toJson()).getAsJsonObject();
			if(object.has("id")){
				object1 = (new JsonParser()).parse(object.get("id").toString()).getAsJsonObject();	
			}
		}
		catch(NullPointerException e){
			return 1;
		}
		return object1.get("$numberLong").getAsLong()+1;
	}

	private long getProjectCount() {
		connect();
		JsonObject object1 = null;
		try{
			JsonObject object = (new JsonParser()).parse(projCollection.find().sort(new BasicDBObject("id", -1)).first().toJson()).getAsJsonObject();
			if(object.has("id")){
				object1 = (new JsonParser()).parse(object.get("id").toString()).getAsJsonObject();	
			}
		}
		catch(NullPointerException e){
			return 1;
		}
		return object1.get("$numberLong").getAsLong()+1;
	}

}