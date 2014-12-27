package com.github.rnowling.bps.datagenerator.datareaders;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.WeatherStationParameters;
import com.google.common.collect.Lists;

public class WeatherParametersReader
{
	InputStream inputStream;
	
	public WeatherParametersReader(InputStream inputStream)
	{
		this.inputStream = inputStream;
	}
	
	private WeatherStationParameters parseLine(String line)
	{
		line = line.trim();
		String[] cols = line.split(",");
		
		String WBAN = cols[0];
		String city = cols[1];
		String state = cols[2];
		double latitude = Double.parseDouble(cols[3]);
		double longitude = Double.parseDouble(cols[4]);
		Pair<Double, Double> coordinates = Pair.create(latitude, longitude);
		double temperatureAverage = Double.parseDouble(cols[5]);
		double temperatureRealCoeff = Double.parseDouble(cols[6]);
		double temperatureImagCoeff = Double.parseDouble(cols[7]);
		double temperatureDerivStd = Double.parseDouble(cols[8]);
		double precipitationAverage = Double.parseDouble(cols[9]);
		double windSpeedRealCoeff = Double.parseDouble(cols[10]);
		double windSpeedImagCoeff = Double.parseDouble(cols[11]);
		double windSpeedK = Double.parseDouble(cols[12]);
		double windSpeedTheta = Double.parseDouble(cols[13]);
		
		return new WeatherStationParameters(WBAN, city, state, coordinates,
				temperatureAverage, temperatureRealCoeff, temperatureImagCoeff,
				temperatureDerivStd, precipitationAverage, windSpeedRealCoeff,
				windSpeedImagCoeff, windSpeedK, windSpeedTheta);
	}
	
	public List<WeatherStationParameters> readParameters()
	{
		Scanner scanner = new Scanner(inputStream);
		
		// skip header
		scanner.nextLine();
		
		List<WeatherStationParameters> parameterList = Lists.newArrayList();
		while(scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			WeatherStationParameters parameters = parseLine(line);
			parameterList.add(parameters);
		}
		
		scanner.close();
		
		return parameterList;
	}
}
