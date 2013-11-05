package com.mapsdirections.lib.api;

public interface DirectionsError {

	public static int NOT_FOUND = 1;

	public static int CONNECTIVITY_PROBLEM = 2;

	public static int PROVIDER_ERROR = 3;

	public static int HTTP_ERROR = 4;

	public static int PARSING_ERROR = 5;

	public static int FATAL_ERROR = 6;
}
