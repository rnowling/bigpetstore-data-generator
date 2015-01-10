package com.github.rnowling.bps.datagenerator.generators.weather;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.Weather;
import com.github.rnowling.bps.datagenerator.framework.samplers.ConditionalSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;

public class WeatherSampler implements Sampler<Weather>
{
	private final Sampler<Double> tempSampler;
	private final Sampler<Double> precipSampler;
	private final Sampler<Double> windSpeedSampler;
	private final ConditionalSampler<Double, Pair<Double, Double>> windChillSampler;
	private final ConditionalSampler<Pair<Double, Double>, Pair<Double, Double>> rainfallSnowfallSampler;
	
	private double time;
	
	public WeatherSampler(Sampler<Double> tempSampler,
			Sampler<Double> precipSampler,
			Sampler<Double> windSpeedSampler,
			ConditionalSampler<Double, Pair<Double, Double>> windChillSampler,
			ConditionalSampler<Pair<Double, Double>, Pair<Double, Double>> rainfallSnowfallSampler)
	{
		this.tempSampler = tempSampler;
		this.precipSampler = precipSampler;
		this.windSpeedSampler = windSpeedSampler;
		this.windChillSampler = windChillSampler;
		this.rainfallSnowfallSampler = rainfallSnowfallSampler;
		
		time = 0.0;
	}
	
	public Weather sample() throws Exception
	{
		double temp = tempSampler.sample();
		double precip = precipSampler.sample();
		double windSpeed = windSpeedSampler.sample();
		
		double windChill = windChillSampler.sample(Pair.create(temp, windSpeed));
		Pair<Double, Double> rainfallSnowfall = rainfallSnowfallSampler.sample(Pair.create(temp, precip));
		
		Weather weather = new Weather(time, temp, precip, windSpeed, windChill, rainfallSnowfall.getFirst(),
				rainfallSnowfall.getSecond());
		
		time += Constants.WEATHER_TIMESTEP;
		
		return weather;
	}
}
