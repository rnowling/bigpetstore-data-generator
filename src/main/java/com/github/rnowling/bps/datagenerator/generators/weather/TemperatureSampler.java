package com.github.rnowling.bps.datagenerator.generators.weather;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.GaussianSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;

public class TemperatureSampler implements Sampler<Double>
{
	final private Sampler<Double> R;
	final private double average;
	final private double coeffReal;
	final private double coeffImag;
	
	private double time;
	private double noise;
	
	public TemperatureSampler(double tempAverage, double tempRealCoeff, double tempImagCoeff,
			double tempSigma, SeedFactory seedFactory)
	{
		R = new GaussianSampler(0.0, tempSigma, seedFactory);
		
		this.average = tempAverage;
		this.coeffReal = tempRealCoeff;
		this.coeffImag = tempImagCoeff;

		time = 0.0;
		noise = 0.0;
	}
	
	public Double sample() throws Exception
	{
		double temp = average + coeffReal * Math.cos(-2.0 * Math.PI * time / Constants.TEMPERATURE_PERIOD) +
				coeffImag * Math.sin(-2.0 * Math.PI * time / Constants.TEMPERATURE_PERIOD) + noise;
		
		noise += -1.0 * noise * Constants.TEMPERATURE_GAMMA * Constants.WEATHER_TIMESTEP + 
				Math.sqrt(Constants.WEATHER_TIMESTEP) * R.sample();
		
		time += Constants.WEATHER_TIMESTEP;
		
		return temp;
	}
}
