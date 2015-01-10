package com.github.rnowling.bps.datagenerator.generators.weather;

import java.util.Collection;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.Weather;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.WeatherStationParameters;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.ExponentialSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;

public class WeatherSamplerBuilder
{
	
	private final WeatherStationParameters parameters;
	private final SeedFactory seedFactory;
	
	public WeatherSamplerBuilder(Collection<WeatherStationParameters> weatherParameters,
			ZipcodeRecord location, SeedFactory seedFactory)
	{
		parameters = findClosest(weatherParameters, location);
		this.seedFactory = seedFactory;
	}
	
	private WeatherStationParameters findClosest(Collection<WeatherStationParameters> weatherParameters,
			ZipcodeRecord location)
	{
		WeatherStationParameters closestStation = null;
		double minDist = Double.MAX_VALUE;
		
		for(WeatherStationParameters parameters : weatherParameters)
		{
			double dist = distance(parameters.getCoordinates(), location.getCoordinates());
			if (dist < minDist)
			{
				minDist = dist;
				closestStation = parameters;
			}
		}
		
		return closestStation;
	}
	
	private double distance(Pair<Double, Double> coordinates,
			Pair<Double, Double> otherCoords)
	{
		double dist = Math.sin(Math.toRadians(coordinates.getFirst())) *
				Math.sin(Math.toRadians(otherCoords.getFirst())) +
				Math.cos(Math.toRadians(coordinates.getFirst())) *
				Math.cos(Math.toRadians(otherCoords.getFirst())) *
				Math.cos(Math.toRadians(coordinates.getSecond() - otherCoords.getSecond()));
		dist = Math.toDegrees(Math.acos(dist)) * 69.09;
		
		return dist;		
	}
	
	private Sampler<Double> buildPrecipSampler()
	{
		return new ExponentialSampler(1.0 / parameters.getPrecipitationAverage(), seedFactory);
	}
	
	private Sampler<Double> buildTempSampler()
	{
		return new TemperatureSampler(parameters.getTemperatureAverage(),
				parameters.getTemperatureRealCoeff(), parameters.getTemperatureImagCoeff(),
				parameters.getTemperatureDerivStd(), seedFactory);
	}
	
	private Sampler<Double> buildWindSpeedSampler()
	{
		return new WindSpeedSampler(parameters.getWindSpeedRealCoeff(),
				parameters.getWindSpeedImagCoeff(),
				parameters.getWindSpeedK(),
				parameters.getWindSpeedTheta(), seedFactory);
	}
	
	public Sampler<Weather> build()
	{
		Sampler<Weather> sampler = new WeatherSampler(buildTempSampler(),
				buildPrecipSampler(), buildWindSpeedSampler(),
				new WindChillConditionalSampler(), new RainfallSnowfallConditionalSampler());
		
		return sampler;
	}
}
