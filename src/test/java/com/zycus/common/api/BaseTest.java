package com.zycus.common.api;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class BaseTest
{
//jhjjhgu
 protected static RequestSpecification requestSpecification;
 protected static ResponseSpecification responseSpecification;
	public static void initialize(String testName,String testDescription) throws Exception
	{
		try
		{
			Report.startExtentReport(testName, testDescription);
			requestSpecification=RestUtilities.getRequestSpecification();
			responseSpecification=RestUtilities.getResponseSpecification();
		}
		catch (Exception e) 
		{
			// TODO: handle exception
			Report.log(Status.FAIL, "Failed while initialization..........");
			throw e;
		}
	}
	public static void endReport() {
		// TODO Auto-generated method stub
		Report.endReport();
	}
	
}
