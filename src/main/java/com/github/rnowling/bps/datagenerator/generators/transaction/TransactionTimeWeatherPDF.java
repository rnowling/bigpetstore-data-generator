package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.List;

import com.github.rnowling.bps.datagenerator.datamodels.Weather;
import com.github.rnowling.bps.datagenerator.framework.pdfs.ProbabilityDensityFunction;

public class TransactionTimeWeatherPDF implements
		ProbabilityDensityFunction<Double>
{
	public final ProbabilityDensityFunction<Double> windChillPDF;
	public final ProbabilityDensityFunction<Double> windSpeedPDF;
	public final ProbabilityDensityFunction<Double> rainfallPDF;
	public final ProbabilityDensityFunction<Double> snowfallPDF;
	public final List<Weather> weatherTraj;
	
	public TransactionTimeWeatherPDF(ProbabilityDensityFunction<Double> windChillPDF, 
			ProbabilityDensityFunction<Double> windSpeedPDF,
			ProbabilityDensityFunction<Double> rainfallPDF,
			ProbabilityDensityFunction<Double> snowfallPDF,
			List<Weather> weatherTraj)
	{
		this.windChillPDF = windChillPDF;
		this.windSpeedPDF = windSpeedPDF;
		this.rainfallPDF = rainfallPDF;
		this.snowfallPDF = snowfallPDF;
		this.weatherTraj = weatherTraj;
	}
	
	protected Weather findWeatherRecord(Double time) throws IllegalArgumentException
	{
		for(int i = 0; i < weatherTraj.size() - 1; i++)
		{
			if(weatherTraj.get(i).getTime() >= time && 
					time < weatherTraj.get(i+1).getTime())
			{
				return weatherTraj.get(i);
			}
		}
		
		throw new IllegalArgumentException("No weather record for time " + time);
	}
	
	protected double probabilityWindChill(Weather weather)
	{
		return windChillPDF.probability(weather.getWindChill());
	}
	
	protected double probabilityWindSpeed(Weather weather)
	{
		return 1.0; // windSpeedPDF.probability(weather.getWindSpeed());
	}
	
	protected double probabilitySnowfall(Weather weather)
	{
		return snowfallPDF.probability(weather.getSnowFall());
	}
	
	protected double probabilityRainfall(Weather weather)
	{
		return 1.0; // rainfallPDF.probability(weather.getRainFall());
	}

	public double probability(Double time) throws IllegalArgumentException
	{
		Weather weather = findWeatherRecord(time);
		
		return Math.min(probabilityWindChill(weather), 
				Math.min(probabilityWindSpeed(weather),
						Math.min(probabilitySnowfall(weather),
								probabilityRainfall(weather))));
	}
	
}
