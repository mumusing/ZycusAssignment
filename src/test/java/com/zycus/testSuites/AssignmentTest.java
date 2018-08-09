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

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.given;

//This important to mention here. As Before start of test case Listener will get fired which will trigger Initialize method in Base Test.
@Listeners(Listener.class)

public class AssignmentTest extends BaseTest
{
	String customerId="";
	
  @Test(description="This method send post request and create customer and validate status code 200")
  public void VeirfyCustomerAccountCreation() 
  {
	  try
	  {
	  //Here I am using POJO classes for storing body.These are model classes. Here I am assuming customer have below detail
	  CustomerModel custmoerInfo=new CustomerModel();
	  custmoerInfo.setName("Mukesh");
	  custmoerInfo.setPhone_number("1234567");
	  custmoerInfo.setAddress("pearl village hyd-tn");
	  custmoerInfo.setWebsite("https://www.mysite.com");
	  custmoerInfo.setLanguage("English");
	  custmoerInfo.setEmail("abc.text");
	  
	  //prepare requestSpecification for this test
	  requestSpecification.basePath(Path.BASE_PATH_CREATE_CUSTOMER);//Here adding Base Path in request
	
	  //Send Request and validate Status Code, Content Type And Status
	  Response response=given()
	  .spec(requestSpecification)
	  .body(custmoerInfo)
	  .when()
	  .post(EndPoints.CREATE_CUSTOMER)
	  .then()
	  .statusCode(200)
	  .contentType(ContentType.JSON).and()
	  .body("status", equalTo("OK")).extract()
	  .response();
	  
	  //Get Json Path Object to read response body
	  JsonPath jsonPath=RestUtilities.getJsonPath(response);
	  customerId=jsonPath.getString("customerId");
	  Report.log(Status.PASS, "Customer Created Successfully with ID: "+customerId);
	  
	  }
	  catch (Exception e) 
	  {
		// TODO: handle exception
		  Report.log(Status.FAIL, "Test Case Failed due to: "+e);
	 }	  	  
  }
  
  @Test(description="This method get the data about the customer using customer Id")
  public void getCustomerDetail()
  {
	  try 
	  {
		Response response =given()
		.spec(RestUtilities.createPathParam(requestSpecification, customerId, "id"))
		.when()
		.post(EndPoints.GET_CUSTOMER)
		.then()
		.statusCode(200)
		.contentType(ContentType.JSON).and()
		.body("status", equalTo("OK")).extract()
		.response();
		
		
	  } 
	  catch (Exception e) {
		// TODO: handle exception
	}
  }
  
  
  
  
}