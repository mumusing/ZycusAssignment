package com.zycus.testSuites;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.zycus.common.api.BaseTest;
import com.zycus.common.api.Listener;
import com.zycus.common.api.Report;
import com.zycus.common.api.RequestType;
import com.zycus.common.api.RestUtilities;
import com.zycus.common.api.Status;
import com.zycus.constants.EndPoints;
import com.zycus.constants.Path;
import com.zycus.model.classes.CustomerModel;

import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

//This important to mention here. As Before start of test case Listener will get fired which will trigger Initialize method in Base Test.
@Listeners(Listener.class)

public class AssignmentTestSuite extends BaseTest {
	String customerId = "";

	@Test(description = "This method send post request and create customer and validate status code 200")
	public void VeirfyCustomerAccountCreation() {
		try {
			// Here I am using POJO classes for storing body.These are model classes. Here I
			// am assuming customer have below detail
			CustomerModel custmoerInfo = new CustomerModel();
			custmoerInfo.setName("Mukesh");
			custmoerInfo.setPhone_number("1234567");
			custmoerInfo.setAddress("pearl village hyd-tn");
			custmoerInfo.setWebsite("https://www.mysite.com");
			custmoerInfo.setLanguage("English");
			custmoerInfo.setEmail("test@test.com");

			// prepare requestSpecification for this test
			requestSpecification.basePath(Path.BASE_PATH_CREATE_CUSTOMER);// Here adding Base Path in request

			// Send Request and validate Status Code, Content Type And Status
			Response response = given().spec(requestSpecification).body(custmoerInfo).when()
					.post(EndPoints.CREATE_CUSTOMER).then().statusCode(200).contentType(ContentType.JSON).and()
					.body("status", equalTo("OK")).extract().response();

			// Get Json Path Object to read response body
			JsonPath jsonPath = RestUtilities.getJsonPath(response);
			customerId = jsonPath.getString("customerId");
			Report.log(Status.PASS, "Customer Created Successfully with ID: " + customerId);

		} catch (Exception e) {
			// TODO: handle exception
			Report.log(Status.FAIL, "Test Case Failed due to: " + e);
		}
	}

	@Test(description = "This method get the data about the customer using customer Id")
	public void getCustomerDetail() {
		try {
			        Response response = given()
					 .spec(RestUtilities.createPathParam(requestSpecification, customerId, "id"))
					.when().get(EndPoints.GET_CUSTOMER)
					.then()
					.statusCode(200)
					.contentType(ContentType.JSON)
					.and()
					.body("status", equalTo("OK"))
					.body("name", equalTo("mukesh"))
					.body("email", equalTo("test@test.com"))
					.body("phone_number", equalTo("1234567"))
					.extract()
					.response();

			JsonPath jPath = RestUtilities.getJsonPath(response);
			customerId = jPath.getString("ID");
			Report.log(Status.PASS, "Customer details access successfully with ID: " + customerId);

		} catch (Exception e) {
			// TODO: handle exception
			Report.log(Status.FAIL, "Test Failed because of exception: " + e.getMessage());
		}
	}

	@Test(description = "This Test send bad request and validate status code 400")
	public void Validate_For_400_Status_Code() {
		try {
			// suppose name field is mandatory but in body we are not adding So that we will
			// get Status code as 400
			CustomerModel custmoerInfo = new CustomerModel();
//	  custmoerInfo.setName("Mukesh");
//	  custmoerInfo.setPhone_number("1234567");
//	  custmoerInfo.setAddress("pearl village hyd-tn");
			custmoerInfo.setWebsite("https://www.mysite.com");
			custmoerInfo.setLanguage("English");
			custmoerInfo.setEmail("test@test.com");

			requestSpecification.basePath(Path.BASE_PATH_CREATE_CUSTOMER);

			        given()
					.spec(requestSpecification)
					.body(custmoerInfo)
					.when()
					.post(EndPoints.CREATE_CUSTOMER)
					.then()
					.log()
					.ifError()
					.statusCode(400);

		} catch (Exception e) {
			// TODO: handle exception
			Report.log(Status.FAIL, "Failed Because of exception: " + e.getMessage());
		}
	}

	@Test(description = "This Test send request with InValid Auth and validate status code 401")
	public void Validate_401_Unauthorized() {
		try {
			// Set Invalid Authentication token
			AuthenticationScheme wrongAuth = RestAssured.oauth("wrong auth", "", "", "");

			// Prepare Request
			RequestSpecBuilder request_Builder = new RequestSpecBuilder();
			request_Builder.setBaseUri(Path.BASE_URI);
			request_Builder.setAuth(wrongAuth);
			requestSpecification = request_Builder.build();

			CustomerModel custmoerInfo = new CustomerModel();
			custmoerInfo.setName("Mukesh");
			custmoerInfo.setPhone_number("1234567");
			custmoerInfo.setAddress("pearl village hyd-tn");
			custmoerInfo.setWebsite("https://www.mysite.com");
			custmoerInfo.setLanguage("English");
			custmoerInfo.setEmail("test@test.com");

			        given().spec(requestSpecification).body(custmoerInfo).when()
					.post(EndPoints.CREATE_CUSTOMER)
					.then()
					.log()
					.ifError()
					.statusCode(401);
		} catch (Exception e) {
			// TODO: handle exception
			Report.log(Status.FAIL, "Failed Because of exception: " + e.getMessage());
		}

	}

	@Test(description="This method send wrong url and Validate 404 Not found")
	public void	Validate_404_Not_found()
	{
		
		try 
		{
			//Add wrong url 
			requestSpecification.basePath("http://wrongurl.com");

			          given()
					 .spec(RestUtilities.createPathParam(requestSpecification, customerId, "id"))
					.when().get(EndPoints.GET_CUSTOMER)
					.then()
					.log()
					.ifError()
					.statusCode(404);

		
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
			Report.log(Status.FAIL, "Failed Because of exception: " + e.getMessage());

		}
	}
	
	
	@Test(description="Validate response time")
	public void ValidateresponseTime()
	{
		try
		{
		    //Validate response time for API should be less than 1 second
	         given()
			.spec(RestUtilities.createPathParam(requestSpecification, customerId, "id"))
			.when().get(EndPoints.GET_CUSTOMER)
			.then()
			.statusCode(200)
			.time(lessThan(1L), TimeUnit.SECONDS)
			.contentType(ContentType.JSON);
		}
		catch(Exception e)   
		{
			Report.log(Status.FAIL, "Failed Because of exception: " + e.getMessage());
		}
				
	}
	
	
	
}
