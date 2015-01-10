package com.github.rnowling.bps.datagenerator.generators.weather;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.framework.samplers.ConditionalSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;

public class WindChillConditionalSampler implements ConditionalSampler<Double, Pair<Double, Double>>
{
	public Double sample(Pair<Double, Double> conditional)
	{
		double temp = conditional.getFirst();
		double windSpeed = conditional.getSecond();
		
		double v_16 = Math.pow(windSpeed, 0.16);
		double windChill = 35.74 + 0.6215 * temp - 35.74 * v_16 + 0.4275 * temp * v_16;
		
		return windChill;
	}
	
	public Sampler<Double> fixConditional(final Pair<Double, Double> conditional)
	{
		return new Sampler<Double>()
			{
				public Double sample()
				{
					double temp = conditional.getFirst();
					double windSpeed = conditional.getSecond();
					
					double v_16 = Math.pow(windSpeed, 0.16);
					double windChill = 35.74 + 0.6215 * temp - 35.74 * v_16 + 0.4275 * temp * v_16;
					
					return windChill;
				}
			};
	}
}
