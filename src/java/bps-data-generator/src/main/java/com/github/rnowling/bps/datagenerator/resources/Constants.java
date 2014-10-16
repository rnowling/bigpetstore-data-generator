package com.github.rnowling.bps.datagenerator.resources;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.google.common.collect.ImmutableList;

public class Constants
{
	public static File
		COORDINATES_FILE,INCOMES_FILE,POPULATION_FILE,ACS_ann,NAMEDB_FILE,PRODUCTS_FILE ;
	static{
		try{
			NAMEDB_FILE=
			COORDINATES_FILE = Paths.get(
					Constants.class.getResource("namedb/data/data.dat").toURI()).toFile();
			INCOMES_FILE = Paths.get(
					Constants.class.getResource("ACS_12_5YR_S1903/ACS_12_5YR_S1903_with_ann.csv").toURI()).toFile();
			POPULATION_FILE = Paths.get(
					Constants.class.getResource("population_data.csv").toURI()).toFile();
			PRODUCTS_FILE = Paths.get(
					Constants.class.getResource("product_categories.json").toURI()).toFile();
			Constants.class.getResource("namedb/data/data.dat");
		}
		catch(Throwable r){
			throw(new RuntimeException(r));
		}
	}
	
	public static final double INCOME_SCALING_FACTOR = 100.0;
	
	public static final int MIN_PETS = 1;
	public static final int MAX_PETS = 10;
	
	public static final List<Pair<Double, Double>> TRANSACTION_TRIGGER_TIME_GAUSSIANS = ImmutableList.of(Pair.create(5.0, 2.0));
	public static final List<Pair<Double, Double>> PURCHASE_TRIGGER_TIME_GAUSSIANS = ImmutableList.of(Pair.create(10.0, 4.0));
	
	public static final double TRANSACTION_TRIGGER_TIME_MAX = 10.0;
	public static final double TRANSACTION_TRIGGER_TIME_MIN = 1.0;
	
	public static final double PURCHASE_TRIGGER_TIME_MAX = 20.0;
	public static final double PURCHASE_TRIGGER_TIME_MIN = 1.0;
	
	public static final double AVERAGE_CUSTOMER_STORE_DISTANCE = 5.0; // miles
	
	public static final List<Pair<Double, Double>> PRODUCT_MSM_FIELD_WEIGHT_GAUSSIANS = ImmutableList.of(Pair.create(0.15, 0.1), Pair.create(0.85, 0.1));
	public static final double PRODUCT_MSM_FIELD_WEIGHT_LOWERBOUND = 0.05;
	public static final double PRODUCT_MSM_FIELD_WEIGHT_UPPERBOUND = 0.95;
	
	
	public static final List<Pair<Double, Double>> PRODUCT_MSM_FIELD_SIMILARITY_WEIGHT_GAUSSIANS = ImmutableList.of(Pair.create(0.15, 0.1), Pair.create(0.85, 0.1));
	public static final double PRODUCT_MSM_FIELD_SIMILARITY_WEIGHT_LOWERBOUND = 0.05;
	public static final double PRODUCT_MSM_FIELD_SIMILARITY_WEIGHT_UPPERBOUND = 0.95;
	
	public static final List<Pair<Double, Double>> PRODUCT_MSM_LOOPBACK_WEIGHT_GAUSSIANS = ImmutableList.of(Pair.create(0.25, 0.1), Pair.create(0.75, 0.1));
	public static final double PRODUCT_MSM_LOOPBACK_WEIGHT_LOWERBOUND = 0.05;
	public static final double PRODUCT_MSM_LOOPBACK_WEIGHT_UPPERBOUND = 0.95;
	
	public static final String PRODUCT_QUANTITY = "size";
	public static final String PRODUCT_CATEGORY = "category";
	
	public static final double STOP_CATEGORY_WEIGHT = 0.01;

}
