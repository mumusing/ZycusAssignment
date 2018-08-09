package com.zycus.common.api;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class BaseTest
{
  static	RequestSpecification reqSpec;
  static	ResponseSpecification resSpec;
	public static void initialize(String testName,String testDescription) throws Exception
	{
		try
		{
			Report.startExtentReport(testName, testDescription);
			reqSpec=RestUtilities.getRequestSpecification();
			resSpec=RestUtilities.getResponseSpecification();
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
