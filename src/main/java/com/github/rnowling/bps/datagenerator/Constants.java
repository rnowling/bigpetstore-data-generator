package com.github.rnowling.bps.datagenerator;

import java.io.File;
import java.util.List;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.google.common.collect.ImmutableList;

public class Constants
{
	public static enum PurchasingModelType
	{
		STATIC,
		DYNAMIC;
	}
	
	public static enum DistributionType
	{
		BOUNDED_MULTIMODAL_GAUSSIAN,
		EXPONENTIAL;
	}
	
	public static final File COORDINATES_FILE = new File("zips.csv");
	public static final File INCOMES_FILE = new File("ACS_12_5YR_S1903/ACS_12_5YR_S1903_with_ann.csv");
	public static final File POPULATION_FILE = new File("population_data.csv");
	
	public static final File NAMEDB_FILE = new File("namedb/data/data.dat");
	
	public static final File PRODUCTS_FILE = new File("product_categories.json");
	
	public static final File WEATHER_PARAMETERS_FILE = new File("weather_parameters.csv");
	
	public static final double INCOME_SCALING_FACTOR = 100.0;
	
	public static final int MIN_PETS = 1;
	public static final int MAX_PETS = 10;
	
	public static final double TRANSACTION_START_TIME = 0.0;
	
	public static final List<Pair<Double, Double>> TRANSACTION_TRIGGER_TIME_GAUSSIANS = ImmutableList.of(Pair.create(5.0, 2.0));
	public static final List<Pair<Double, Double>> PURCHASE_TRIGGER_TIME_GAUSSIANS = ImmutableList.of(Pair.create(10.0, 4.0));
	
	public static final double TRANSACTION_TRIGGER_TIME_MAX = 10.0;
	public static final double TRANSACTION_TRIGGER_TIME_MIN = 1.0;
	
	public static final double PURCHASE_TRIGGER_TIME_MAX = 20.0;
	public static final double PURCHASE_TRIGGER_TIME_MIN = 1.0;
	
	public static final double AVERAGE_CUSTOMER_STORE_DISTANCE = 5.0; // miles
	
	public static final PurchasingModelType PURCHASING_MODEL_TYPE = PurchasingModelType.DYNAMIC;
	
	public static final List<Pair<Double, Double>> PRODUCT_MSM_FIELD_WEIGHT_GAUSSIANS = ImmutableList.of(Pair.create(0.15, 0.1), Pair.create(0.85, 0.1));
	public static final double PRODUCT_MSM_FIELD_WEIGHT_LOWERBOUND = 0.05;
	public static final double PRODUCT_MSM_FIELD_WEIGHT_UPPERBOUND = 0.95;
	
	public static final List<Pair<Double, Double>> PRODUCT_MSM_FIELD_SIMILARITY_WEIGHT_GAUSSIANS = ImmutableList.of(Pair.create(0.15, 0.1), Pair.create(0.85, 0.1));
	public static final double PRODUCT_MSM_FIELD_SIMILARITY_WEIGHT_LOWERBOUND = 0.05;
	public static final double PRODUCT_MSM_FIELD_SIMILARITY_WEIGHT_UPPERBOUND = 0.95;
	
	public static final List<Pair<Double, Double>> PRODUCT_MSM_LOOPBACK_WEIGHT_GAUSSIANS = ImmutableList.of(Pair.create(0.25, 0.1), Pair.create(0.75, 0.1));
	public static final double PRODUCT_MSM_LOOPBACK_WEIGHT_LOWERBOUND = 0.05;
	public static final double PRODUCT_MSM_LOOPBACK_WEIGHT_UPPERBOUND = 0.95;
	
	public static final DistributionType STATIC_PURCHASING_MODEL_FIELD_WEIGHT_DISTRIBUTION_TYPE = DistributionType.BOUNDED_MULTIMODAL_GAUSSIAN;
	public static final DistributionType STATIC_PURCHASING_MODEL_FIELD_VALUE_WEIGHT_DISTRIBUTION_TYPE = DistributionType.EXPONENTIAL;
	
	public static final List<Pair<Double, Double>> STATIC_FIELD_WEIGHT_GAUSSIANS = ImmutableList.of(Pair.create(0.15, 0.1), Pair.create(0.85, 0.1));
	public static final double STATIC_FIELD_WEIGHT_LOWERBOUND = 0.05;
	public static final double STATIC_FIELD_WEIGHT_UPPERBOUND = 0.95;
	
	public static final List<Pair<Double, Double>> STATIC_FIELD_VALUE_WEIGHT_GAUSSIANS = ImmutableList.of(Pair.create(0.15, 0.1), Pair.create(0.85, 0.1));
	public static final double STATIC_FIELD_VALUE_WEIGHT_LOWERBOUND = 0.05;
	public static final double STATIC_FIELD_VALUE_WEIGHT_UPPERBOUND = 0.95;
	
	public static final double STATIC_FIELD_WEIGHT_EXPONENTIAL = 0.25;
	public static final double STATIC_FIELD_VALUE_WEIGHT_EXPONENTIAL = 2.0;
	
	
	public static final String PRODUCT_QUANTITY = "size";
	public static final String PRODUCT_CATEGORY = "category";
	
	public static final double STOP_CATEGORY_WEIGHT = 0.01;
	
	public static final double TEMPERATURE_GAMMA = 0.5; // 2 / day
	public static final double WEATHER_TIMESTEP = 1.0; // days
	public static final double TEMPERATURE_PERIOD = 365.0; // days
	
	public static final double PRECIPITATION_A = 0.2;
	public static final double PRECIPITATION_B = 27.0;
	public static final double PRECIPITATION_TO_SNOWFALL = 10.0;
	
	public static final double WEATHER_SIMULATION_LENGTH_MULTIPLIER = 1.25;
	
	public static final double WIND_CHILL_PROBABILITY_A = 0.8;
	public static final double WIND_CHILL_PROBABILITY_B = 0.5;
	public static final double WIND_CHILL_PROBABILITY_C = 10.0; // F
	public static final double WIND_CHILL_PROBABILITY_D = 0.2;
	
	public static final double WIND_SPEED_PROBABILITY_A = -0.5;
	public static final double WIND_SPEED_PROBABILITY_B = 0.8;
	public static final double WIND_SPEED_PROBABILITY_C = 17.5; // mph
	public static final double WIND_SPEED_PROBABILITY_D = 1.0;
	
	public static final double SNOWFALL_PROBABILITY_A = -0.8;
	public static final double SNOWFALL_PROBABILITY_B = 10.0; 
	public static final double SNOWFALL_PROBABILITY_C = 0.75; // in
	public static final double SNOWFALL_PROBABILITY_D = 1.0;
	
	public static final double RAINFALL_PROBABILITY_A = -0.6;
	public static final double RAINFALL_PROBABILITY_B = 7.5;
	public static final double RAINFALL_PROBABILITY_C = 0.75; // in
	public static final double RAINFALL_PROBABILITY_D = 1.0;
}
