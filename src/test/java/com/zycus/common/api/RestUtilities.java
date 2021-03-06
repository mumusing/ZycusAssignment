package com.zycus.common.api;

import java.util.concurrent.TimeUnit;

import org.apache.http.client.methods.RequestBuilder;

import com.zycus.constants.AuthTokens;
import com.zycus.constants.Path;
import com.zycus.custom.exceptions.ActionNotSupportedException;

import groovyjarjarantlr.LexerSharedInputState;
import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static org.hamcrest.Matchers.lessThan;
import static io.restassured.RestAssured.given;


public class RestUtilities {
	public static String endPoint;
	public static RequestSpecBuilder request_Builder;
	public static ResponseSpecBuilder response_Builder;
	public static RequestSpecification requestSpecification;
	public static ResponseSpecification response_specification;

	public static void setEndPoints(String endPoint)
	{
		RestUtilities.endPoint=endPoint;
	}

	/*
	 * This method is use for getting request specification object. This Method will
	 * set base uri and auth schema
	 */
	public static RequestSpecification getRequestSpecification() {
		AuthenticationScheme authSchema = RestAssured.oauth(AuthTokens.CONSUMER_KEY, AuthTokens.CONSUMER_SECRET,
				AuthTokens.ACCESS_TOKEN, AuthTokens.ACCESS_SECRET);
		request_Builder = new RequestSpecBuilder();
		request_Builder.setBaseUri(Path.BASE_URI);
		request_Builder.setAuth(authSchema);
		requestSpecification = request_Builder.build();
		return requestSpecification;
	}

	/*
	 * This method is use for getting response specification It will validate for
	 * Response Code 2sec, and status code 200
	 */
	public static ResponseSpecification getResponseSpecification() {
		response_Builder = new ResponseSpecBuilder();

		response_Builder.expectStatusCode(200);
		response_Builder.expectResponseTime(lessThan(2L), TimeUnit.SECONDS);
		response_specification = response_Builder.build();
		return response_specification;
	}

	/*
	 * This Method creates query parameter
	 * 
	 */
	public static RequestSpecification createQueryParameter(RequestSpecification rspec, String param, String value) {
		return requestSpecification.queryParam(param, value);
	}

	/*
	 * This Method can be use too create path parameter
	 * 
	 */
	public static RequestSpecification createPathParam(RequestSpecification requesSpec, String path, String value) {
		return requesSpec.pathParam(path, value);
	}
    /*
     * This Method return the the response object for given response
     */
	public static Response getResponse()
	{
		if (endPoint==null) 
		{
			return null;
		}
		return given().get(endPoint);
	}
	
	/*
	 * This method return the the response object based on request type
	 * 
	 */
	public static Response getResponse(RequestSpecification request,RequestType type)
	{
		requestSpecification=request.spec(request);
		Response response=null;
		switch (type) {
		case GET:
			response=given().spec(requestSpecification).get(endPoint);
			break;
			
        case POST:
			response=given().spec(requestSpecification).post(endPoint);
			break;	
			
        case PUT:
			response=given().spec(requestSpecification).put(endPoint);
			break;	
		
        case DELETE:
        	response=given().spec(requestSpecification).delete(endPoint);
			break;	

		default:
			throw new ActionNotSupportedException("Not valid Exception");
		}
		//Log response
		response.then().log().all();
		//Verify for common validation
		response.then().specification(response_specification);
		return response;
	}
	/*
	 * This method gets JSON path OBJECT for given response
	 */
	public static JsonPath getJsonPath(Response response)
	{
		if (response==null) 
		{
			return null;
		}
		String jsonPath=response.asString();
		return new JsonPath(jsonPath);
	}
	
	public static void setContentType(ContentType type)
	{
		given().contentType(type);
	}
	
}
