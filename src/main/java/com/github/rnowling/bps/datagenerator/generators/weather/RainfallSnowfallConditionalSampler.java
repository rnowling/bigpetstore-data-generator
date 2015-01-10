package com.github.rnowling.bps.datagenerator.generators.weather;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.framework.samplers.ConditionalSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;

public class RainfallSnowfallConditionalSampler implements
		ConditionalSampler<Pair<Double, Double>, Pair<Double, Double>>
{
	public Pair<Double, Double> sample(Pair<Double, Double> conditional)
	{
		double temp = conditional.getFirst();
		double precipitation = conditional.getSecond();
		
		double fractionRain = 1.0 / (1.0 + Math.exp(- Constants.PRECIPITATION_A * (temp - Constants.PRECIPITATION_B)));
		
		double rainfall = fractionRain * precipitation;
		double snowfall = Constants.PRECIPITATION_TO_SNOWFALL * (1.0 - fractionRain) * precipitation;
		
		return Pair.create(rainfall, snowfall);
	}
	
	public Sampler<Pair<Double, Double>> fixConditional(final Pair<Double, Double> conditional)
	{
		return new Sampler<Pair<Double, Double>>()
		{
			public Pair<Double, Double> sample()
			{
				double temp = conditional.getFirst();
				double precipitation = conditional.getSecond();
				
				double fractionRain = 1.0 / (1.0 + Math.exp(- Constants.PRECIPITATION_A * (temp - Constants.PRECIPITATION_B)));
				
				double rainfall = fractionRain * precipitation;
				double snowfall = Constants.PRECIPITATION_TO_SNOWFALL * (1.0 - fractionRain) * precipitation;
				
				return Pair.create(rainfall, snowfall);
			}
		};
	}
}
