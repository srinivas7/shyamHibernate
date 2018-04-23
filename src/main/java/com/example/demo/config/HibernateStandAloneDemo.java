package com.example.demo.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Student;
import com.example.demo.model.University;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

/**
 * Class used to perform CRUD operation on database with Hibernate API's
 * 
 */
@RestController
@RequestMapping("/api")
public class HibernateStandAloneDemo {

	@Autowired
	HibernateUtil HibernateUtil;

	@Autowired
	private SessionFactory sessionFactory;

	/*
	 * @SuppressWarnings("unused") public static void main(String[] args) {
	 * 
	 * HibernateStandAloneDemo application = new HibernateStandAloneDemo();
	 * 
	 * 
	 * Save few objects with hibernate
	 * 
	 * int studentId1 = application.saveStudent("Sam", "Disilva", "Maths"); int
	 * studentId2 = application.saveStudent("Joshua", "Brill", "Science"); int
	 * studentId3 = application.saveStudent("Peter", "Pan", "Physics"); int
	 * studentId4 = application.saveStudent("Bill", "Laurent", "Maths");
	 * 
	 * 
	 * Retrieve all saved objects
	 * 
	 * List<Student> students = application.getAllStudents();
	 * System.out.println("List of all persisted students >>>"); for (Student
	 * student : students) { System.out.println("Persisted Student :" +
	 * student); }
	 * 
	 * 
	 * Update an object
	 * 
	 * application.updateStudent(studentId4, "ARTS");
	 * 
	 * 
	 * Deletes an object
	 * 
	 * application.deleteStudent(studentId2);
	 * 
	 * 
	 * Retrieve all saved objects
	 * 
	 * List<Student> remaingStudents = application.getAllStudents();
	 * System.out.println("List of all remained persisted students >>>"); for
	 * (Student student : remaingStudents) { System.out.println(
	 * "Persisted Student :" + student); }
	 * 
	 * }
	 */

	/**
	 * This method saves a Student object in database
	 */
	public int saveStudent(String firstName, String lastName, String section, University unv) {
		Student student = new Student();

		student.setFirstName(firstName);
		student.setLastName(lastName);
		student.setSection(section);
		student.setUniversity(unv);
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		int id = (Integer) session.save(student);
		session.getTransaction().commit();
		session.close();
		return id;
	}

	public University saveUniv(University unv) {
		/*
		 * University unv = new University();
		 * 
		 * unv.setPlace(place); unv.setUnv_name(name);
		 */

		Session session = sessionFactory.openSession();
		session.beginTransaction();

		int id = (Integer) session.save(unv);
		unv.setUnv_id(id);
		session.getTransaction().commit();
		session.close();

		return unv;
	}

	/**
	 * This method returns list of all persisted Student objects/tuples from
	 * database
	 */
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public List<Student> getAllStudents() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		University unv2 = new University();
		/*
		 * University unv1 = this.saveUniv("RJY", "GIET"); University unv3 =
		 * this.saveUniv("RJY", "VIET"); University unv4 = this.saveUniv("RJY",
		 * "BIET");
		 */
		Student student1 = new Student("Sam", "Disilva", "Maths");
		Student student2 = new Student("Joshua", "Brill", "Science");
		Student student3 = new Student("Peter", "Pan", "Physics");
		/*
		 * int studentId1 = this.saveStudent("Sam", "Disilva", "Maths",unv2);
		 * int studentId2 = this.saveStudent("Joshua", "Brill", "Science",unv2);
		 * int studentId3 = this.saveStudent("Peter", "Pan", "Physics",unv2);
		 * int studentId4 = this.saveStudent("Bill", "Laurent", "Maths",unv2);
		 */
		unv2.setUnv_name("RIET");
		unv2.setPlace("RJY");
		List<Student> allStudents = new ArrayList<Student>();
		student1.setUniversity(unv2);
		student2.setUniversity(unv2);
		student3.setUniversity(unv2);

		allStudents.add(student1);
		allStudents.add(student2);
		allStudents.add(student3);

		unv2.setStudents(allStudents);
		this.saveUniv(unv2);
		@SuppressWarnings("unchecked")
		List<Student> employees = (List<Student>) session.createQuery("FROM Student s ORDER BY s.firstName DESC")
				.list();

		session.getTransaction().commit();
		session.close();
		return employees;
	}

	/**
	 * This method updates a specific Student object
	 */
	public void updateStudent(int id, String section) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Student student = (Student) session.get(Student.class, id);
		student.setSection(section);
		// session.update(student);//No need to update manually as it will be
		// updated automatically on transaction close.
		session.getTransaction().commit();
		session.close();
	}

	/**
	 * This method deletes a specific Student object
	 */
	public void deleteStudent(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Student student = (Student) session.get(Student.class, id);
		session.delete(student);
		session.getTransaction().commit();
		session.close();
	}

	@RequestMapping(value = "/saveRec", method = RequestMethod.POST)
	public void m1(HttpServletRequest req, @RequestParam("query") String query)
			throws ClassNotFoundException, SQLException, IOException {

		String ob = req.getParameter("data");
		String ob1 = req.getParameter("params");
		JsonParser parser = new JsonParser();
		JsonObject o = parser.parse(ob).getAsJsonObject();
		JsonArray arr = o.get("data").getAsJsonArray();
		JsonObject o1 = parser.parse(ob1).getAsJsonObject();
		System.out.println(query);
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
		String insertEmployeeSQL = query;
		//queryBuilder(query, o1);
		PreparedStatement employeeStmt = conn.prepareStatement(insertEmployeeSQL);

		for (JsonElement ojb : arr) {
			JsonObject object = ojb.getAsJsonObject();

			for (Entry<String, JsonElement> valueEntry : object.entrySet()) {
				JsonElement element = valueEntry.getValue();
				if (element.isJsonPrimitive()) {
					JsonPrimitive value = element.getAsJsonPrimitive();
					if (value.isString()) {
						employeeStmt.setString(o1.get(valueEntry.getKey()).getAsInt(),
								valueEntry.getValue().getAsString());
					} else if (value.isNumber()) {
						if (value.getAsString().contains(".")) {
							employeeStmt.setDouble(o1.get(valueEntry.getKey()).getAsInt(),
									valueEntry.getValue().getAsDouble());
						} else {
							employeeStmt.setLong(o1.get(valueEntry.getKey()).getAsInt(),
									valueEntry.getValue().getAsLong());
						}
					} else if (value.isBoolean()) {
						employeeStmt.setBoolean(o1.get(valueEntry.getKey()).getAsInt(),
								valueEntry.getValue().getAsBoolean());
					} else {
						throw new JsonSyntaxException("Can't parse value: " + value);
					}
				}
			}

			employeeStmt.addBatch();
		}

		employeeStmt.executeBatch();
		conn.close();
	}

	@RequestMapping(value = "/createTable", method = RequestMethod.POST)
	public boolean m2(@RequestParam("query") String query) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
		String insertEmployeeSQL = query;

		PreparedStatement employeeStmt = conn.prepareStatement(insertEmployeeSQL);
		boolean ret = employeeStmt.execute();
		conn.close();
		return ret;
	}

	private  String queryBuilder(String query, JsonObject params) {
		
		String q=null;
		
		switch(query){
				
		case "INSERT":
			q = queryConstruction(params);
			return q;
		case "CREATE":
			q = queryConstruction(params);
			return q;	
		
		}
		return q;
		
		
	}
	
	private String queryConstruction(JsonObject params){
		
		JsonObject obj = (JsonObject) params.get("data");
		String val = "values(";
		int lenOfKeys = obj.keySet().size();
		for(int k=0;k<lenOfKeys;k++){
			val=val+"?,";	
		}
		val = val.substring(0, val.lastIndexOf(','));
		val = val+')';
				
		return val;
	}

}