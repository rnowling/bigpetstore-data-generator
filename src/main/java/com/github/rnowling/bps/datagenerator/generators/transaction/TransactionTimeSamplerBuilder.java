package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.List;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.Weather;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.pdfs.ConditionalProbabilityDensityFunction;
import com.github.rnowling.bps.datagenerator.framework.pdfs.ProbabilityDensityFunction;
import com.github.rnowling.bps.datagenerator.framework.pdfs.SigmoidPDF;
import com.github.rnowling.bps.datagenerator.framework.samplers.ExponentialSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.StatefulMonteCarloSampler;

public class TransactionTimeSamplerBuilder
{
	private final SeedFactory seedFactory;
	private CustomerInventory customerInventory;
	private CustomerTransactionParameters transactionParameters;
	private final List<Weather> weatherTraj;
	
	public TransactionTimeSamplerBuilder(List<Weather> weatherTraj, SeedFactory seedFactory)
	{
		this.weatherTraj = weatherTraj;
		this.seedFactory = seedFactory;
	}
	
	public void setCustomerInventory(CustomerInventory inventory)
	{
		this.customerInventory = inventory;
	}
	
	public void setCustomerTransactionParameters(CustomerTransactionParameters parameters)
	{
		this.transactionParameters = parameters;
	}
	
	protected ConditionalProbabilityDensityFunction<Double, Double> buildTimePDF()
	{
		ProbabilityDensityFunction<Double> windChillPDF = new SigmoidPDF(Constants.WIND_CHILL_PROBABILITY_A,
				Constants.WIND_CHILL_PROBABILITY_B, Constants.WIND_CHILL_PROBABILITY_C, 
				Constants.WIND_CHILL_PROBABILITY_D);
		ProbabilityDensityFunction<Double> windSpeedPDF = new SigmoidPDF(Constants.WIND_SPEED_PROBABILITY_A,
				Constants.WIND_SPEED_PROBABILITY_B, Constants.WIND_SPEED_PROBABILITY_C,
				Constants.WIND_SPEED_PROBABILITY_D);
		ProbabilityDensityFunction<Double> rainfallPDF = new SigmoidPDF(Constants.RAINFALL_PROBABILITY_A,
				Constants.RAINFALL_PROBABILITY_B, Constants.RAINFALL_PROBABILITY_C, 
				Constants.RAINFALL_PROBABILITY_D);
		ProbabilityDensityFunction<Double> snowfallPDF = new SigmoidPDF(Constants.SNOWFALL_PROBABILITY_A,
				Constants.SNOWFALL_PROBABILITY_B, Constants.SNOWFALL_PROBABILITY_C,
				Constants.SNOWFALL_PROBABILITY_D);
		
		ProbabilityDensityFunction<Double> weatherPDF = 
				new TransactionTimeWeatherPDF(windChillPDF, windSpeedPDF, rainfallPDF, snowfallPDF,
				weatherTraj);
		
		return new TransactionTimePDF(
				new TransactionTimeArrowOfTimePDF(),
				weatherPDF);
	}
	
	public Sampler<Double> build()
	{
		double lambda = 1.0 / transactionParameters.getAverageTransactionTriggerTime();
		Sampler<Double> arrivalTimeSampler = new ExponentialSampler(lambda, seedFactory);
		Sampler<Double> proposedTimeSampler = new ProposedPurchaseTimeSampler(customerInventory,
				arrivalTimeSampler);
		
		return new StatefulMonteCarloSampler<Double>(proposedTimeSampler, 
				buildTimePDF(),
				Constants.TRANSACTION_START_TIME,
				seedFactory);
	}
}
