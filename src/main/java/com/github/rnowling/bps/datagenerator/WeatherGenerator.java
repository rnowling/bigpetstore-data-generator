package com.github.rnowling.bps.datagenerator;

import java.util.List;

import com.github.rnowling.bps.datagenerator.datamodels.Store;
import com.github.rnowling.bps.datagenerator.datamodels.Weather;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.generators.weather.WeatherSamplerBuilder;
import com.google.common.collect.Lists;

public class WeatherGenerator
{
	private final Sampler<Weather> weatherSampler;
	private final int weatherSimulationLength;
	
	public WeatherGenerator(InputData inputData, double simulationLength, Store store, SeedFactory seedFactory)
	{
		WeatherSamplerBuilder builder = new WeatherSamplerBuilder(inputData.getWeatherStationParameters(),
				store.getLocation(), seedFactory);
		weatherSampler = builder.build();
		
		weatherSimulationLength = (int) Math.ceil(simulationLength * Constants.WEATHER_SIMULATION_LENGTH_MULTIPLIER);
	}
	
	public List<Weather> generate() throws Exception
	{
		List<Weather> weatherSimulation = Lists.newArrayList();
		
		for(int i = 0; i < weatherSimulationLength; i++)
		{
			Weather weather = weatherSampler.sample();
			weatherSimulation.add(weather);
		}
		
		return weatherSimulation;
	}
}
