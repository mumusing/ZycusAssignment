package com.zycus.common.api;

import java.io.File;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Report
{

	private static String resultPath=getResultPath();
	private static String file_name="/ExtentReports.html";
	private static  ExtentReports extentReport;
	private static ExtentTest looger;
	private static String testName;
	private static String getResultPath()
	{
		resultPath=System.getProperty("user.dir")+"/report/"+"Run"+DateHelper.getCurrentDateTime("yyyyDDmm")+DateHelper.getCurrentDateTime("HHmmss");
		if (!new File(resultPath).isDirectory())
		{
			new File(resultPath).mkdirs();
		}
		
		return resultPath;
	}
	
	public static void startExtentReport(String testCaseName,String description)
	{
		testName=testCaseName;
		extentReport=new ExtentReports(resultPath+file_name,false);
		extentReport.addSystemInfo("Host", "Local").addSystemInfo("Env", "Cloud");
		looger=extentReport.startTest(testCaseName, description);
		
	}
	public static void endReport()
	{
		extentReport.endTest(looger);
		extentReport.flush();
	}
	public static void log(Status status,String message)
	{
		switch (status) 
		{
		
			
		case PASS:
			      looger.log(LogStatus.PASS, message);
			break;
			
		
			
		case FAIL:
			      looger.log(LogStatus.FAIL, message);
			break;
			
		case INFO:
		      looger.log(LogStatus.INFO, message);
		break;
			
		default:
			break;
		}
	}
	

	
}
