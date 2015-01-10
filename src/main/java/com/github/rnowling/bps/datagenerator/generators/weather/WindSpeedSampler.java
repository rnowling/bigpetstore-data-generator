package com.github.rnowling.bps.datagenerator.generators.weather;

import org.apache.commons.math3.distribution.GammaDistribution;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;

public class WindSpeedSampler implements Sampler<Double>
{
	private final double coeffReal;
	private final double coeffImag;
	private final GammaDistribution gamma;
	
	private double time;
	
	public WindSpeedSampler(double timeOffset, double windSpeedRealCoeff, double windSpeedImagCoeff,
			double windSpeedK, double windSpeedTheta, SeedFactory seedFactory)
	{
		coeffReal = windSpeedRealCoeff;
		coeffImag = windSpeedImagCoeff;
		
		gamma = new GammaDistribution(windSpeedK, windSpeedTheta);
		gamma.reseedRandomGenerator(seedFactory.getNextSeed());
		
		time = timeOffset;
	}
	
	public Double sample()
	{
		double windSpeed = Math.max(0.0, coeffReal * Math.cos(-2.0 * Math.PI * time / Constants.TEMPERATURE_PERIOD) +
				coeffImag * Math.sin(-2.0 * Math.PI * time / Constants.TEMPERATURE_PERIOD) + gamma.sample());
		
		time += Constants.WEATHER_TIMESTEP;
		
		return windSpeed;
	}
}
