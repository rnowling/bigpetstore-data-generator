package com.github.rnowling.bps.datagenerator;

public class Constants
{
	public static final String COORDINATES_FILE = "resources/zips.csv";
	public static final String INCOMES_FILE = "resources/ACS_12_5YR_S1903/ACS_12_5YR_S1903_with_ann.csv";
	public static final String POPULATION_FILE = "resources/population_data.csv";
	
	public static final String NAMEDB_FILE = "resources/namedb/data/data.dat";
	
	public static final double INCOME_SCALING_FACTOR = 100.0d;
	
	public static final int MIN_PETS = 1;
	public static final int MAX_PETS = 10;
	
	public static final double TRANSACTION_TRIGGER_TIME_AVERAGE = 5.0; // days
	public static final double TRANSACTION_TRIGGER_TIME_VARIANCE = 2.0;
	public static final double TRANSACTION_TRIGGER_TIME_MAX = 10.0;
	public static final double TRANSACTION_TRIGGER_TIME_MIN = 1.0;
	
	public static final double PURCHASE_TRIGGER_TIME_AVERAGE = 10.0; // days
	public static final double PURCHASE_TRIGGER_TIME_VARIANCE = 4.0;
	public static final double PURCHASE_TRIGGER_TIME_MAX = 20.0;
	public static final double PURCHASE_TRIGGER_TIME_MIN = 1.0;
	
	public static final double AVERAGE_CUSTOMER_STORE_DISTANCE = 5.0; // miles
	
}
