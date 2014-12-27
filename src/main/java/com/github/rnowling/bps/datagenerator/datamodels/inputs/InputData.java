package com.github.rnowling.bps.datagenerator.datamodels.inputs;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class InputData implements Serializable
{
	private static final long serialVersionUID = 9078989799806707788L;
	
	List<ZipcodeRecord> zipcodeTable;
	Names names;
	Collection<ProductCategory> productCategories;
	List<WeatherStationParameters> weatherStationParameters;
	
	public InputData(List<ZipcodeRecord> zipcodeTable,
			Names names,
			Collection<ProductCategory> productCategories,
			List<WeatherStationParameters> weatherStationParameters)
	{
		this.zipcodeTable = Collections.unmodifiableList(zipcodeTable);
		this.names = names;
		this.productCategories = Collections.unmodifiableCollection(productCategories);
		this.weatherStationParameters = Collections.unmodifiableList(weatherStationParameters);
	}
	
	public List<ZipcodeRecord> getZipcodeTable()
	{
		return zipcodeTable;
	}
	
	public Names getNames()
	{
		return names;
	}
	
	public Collection<ProductCategory> getProductCategories()
	{
		return productCategories;
	}
	
	public List<WeatherStationParameters> getWeatherStationParameters()
	{
		return weatherStationParameters;
	}
	
	
}
