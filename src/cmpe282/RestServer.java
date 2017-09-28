package cmpe282;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("cmpe282ashok639/rest")
public class RestServer {
 	
	MongoConnect mongo = new MongoConnect();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("project")
	public Response fetchProjects(){
		String resp = mongo.getProjects();		
		if(resp.equals("[]"))
		{
			String err = "{\"error\": {\"code\": 404,\"message\": \"No objects found\"}}";
	        return Response.status(Response.Status.NOT_FOUND).entity(err).build();
		}else
			return Response.ok(mongo.getProjects(), MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("employee")
	public Response fetchEmployees(){
		String resp = mongo.getEmployees();		
		if(resp.equals("[]"))
		{
			String err = "{\"error\": {\"code\": 404,\"message\": \"No objects found\"}}";
	        return Response.status(Response.Status.NOT_FOUND).entity(err).build();
		}else
			return Response.ok(mongo.getEmployees(), MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("employee")
	public Response createEmployee(String employee){
		if(mongo.insertEmployee(employee)){
			String success = "{\"success\": {\"code\": 201,\"message\": \"Object successfully created\"}}";
			return Response.status(Response.Status.CREATED).entity(success).build();
		}else{
			String err = "{\"error\": {\"code\": 409,\"message\": \"Unable to create object\"}}";
			return Response.status(Response.Status.CONFLICT).entity(err).build();
		}
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("project")
	public Response createProject(String project){
		if(mongo.insertProject(project)){
			String success = "{\"success\": {\"code\": 201,\"message\": \"Object successfully created\"}}";
			return Response.status(Response.Status.CREATED).entity(success).build();
		}else{
			String err = "{\"error\": {\"code\": 409,\"message\": \"Unable to create object\"}}";
			return Response.status(Response.Status.CONFLICT).entity(err).build();
		}
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("employee/{id}")
	public Response updateEmployee(String employee, @PathParam("id") int id){
		if(mongo.updateEmployee(employee, id)){
			String success = "{\"success\": {\"code\": 200,\"message\": \"Object updated successfully\"}}";
			return Response.ok(success, MediaType.APPLICATION_JSON).build();
		}else{
			String err = "{\"error\": {\"code\": 404,\"message\": \"Unable to find the object\"}}";
			return Response.status(Response.Status.NOT_FOUND).entity(err).build();
		}
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("project/{id}")
	public Response updateProject(String project, @PathParam("id") int id){
		if(mongo.updateProject(project, id)){
			String success = "{\"success\": {\"code\": 200,\"message\": \"Object updated successfully\"}}";
			return Response.ok(success, MediaType.APPLICATION_JSON).build();
		}else{
			String err = "{\"error\": {\"code\": 404,\"message\": \"Unable to find the object\"}}";
			return Response.status(Response.Status.NOT_FOUND).entity(err).build();
		}
	}
	

	@DELETE
	@Produces(MediaType.TEXT_HTML)
	@Path("employee/{id}")
	public Response deleteEmployee(@PathParam("id") int id){
		if(mongo.deleteEmployee(id)){
			String success = "{\"success\": {\"code\": 200,\"message\": \"Object deleted successfully\"}}";
			return Response.ok(success, MediaType.APPLICATION_JSON).build();
		}else{
			String err = "{\"error\": {\"code\": 404,\"message\": \"Unable to find the object\"}}";
			return Response.status(Response.Status.NOT_FOUND).entity(err).build();
		}
		
	}
	
	@DELETE
	@Produces(MediaType.TEXT_HTML)
	@Path("project/{id}")
	public Response deleteProject(@PathParam("id") int id){
		if(mongo.deleteProject(id)){
			String success = "{\"success\": {\"code\": 200,\"message\": \"Object deleted successfully\"}}";
			return Response.ok(success, MediaType.APPLICATION_JSON).build();
		}else{
			String err = "{\"error\": {\"code\": 404,\"message\": \"Unable to find the object\"}}";
			return Response.status(Response.Status.NOT_FOUND).entity(err).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("employee/{id}")
	public Response fetchEmployee(@PathParam("id") int id){
		String resp=mongo.getEmployee(id);
		if(resp.equals(""))
		{
			String err = "{\"error\": {\"code\": 404,\"message\": \"ID not found\"}}";
	        return Response.status(Response.Status.NOT_FOUND).entity(err).build();
		}else
			return Response.ok(mongo.getEmployee(id), MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("project/{id}")
	public Response fetchProject(@PathParam("id") int id){
		String resp=mongo.getProject(id);
		if(resp.equals(""))
		{
			String err = "{\"error\": {\"code\": 404,\"message\": \"ID not found\"}}";
	        return Response.status(Response.Status.NOT_FOUND).entity(err).build();
		}else
			return Response.ok(mongo.getProject(id), MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("test")
	public Response sample(){
		return Response.ok(mongo.getEmployeeCount(), MediaType.APPLICATION_JSON).build();
	}
	
}
