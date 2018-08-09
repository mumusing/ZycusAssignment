package com.zycus.custom.exceptions;

@SuppressWarnings("serial")
public class ActionNotSupportedException extends RuntimeException
{

	public ActionNotSupportedException(String exception)
	{
		super(exception);
	}
}
