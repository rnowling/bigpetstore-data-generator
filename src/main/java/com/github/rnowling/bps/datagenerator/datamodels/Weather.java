package com.github.rnowling.bps.datagenerator.datamodels;

import java.io.Serializable;

public class Weather implements Serializable
{
	private static final long serialVersionUID = -6397575341624071821L;

	private final double time;
	private final double temperature;
	private final double precipitation;
	private final double windSpeed;
	private final double windChill;
	private final double rainFall;
	private final double snowFall;
	
	public Weather(double time, double temperature, double precipitation, double windSpeed,
			double windChill, double rainFall, double snowFall)
	{
		this.time = time;
		this.temperature = temperature;
		this.precipitation = precipitation;
		this.windSpeed = windSpeed;
		this.windChill = windChill;
		this.rainFall = rainFall;
		this.snowFall = snowFall;
	}

	public double getTime()
	{
		return time;
	}

	public double getTemperature()
	{
		return temperature;
	}

	public double getPrecipitation()
	{
		return precipitation;
	}

	public double getWindSpeed()
	{
		return windSpeed;
	}

	public double getWindChill()
	{
		return windChill;
	}

	public double getRainFall()
	{
		return rainFall;
	}

	public double getSnowFall()
	{
		return snowFall;
	}
}
