package com.github.rnowling.bps.datagenerator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.Names;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datareaders.NameReader;
import com.github.rnowling.bps.datagenerator.datareaders.ProductsReader;
import com.github.rnowling.bps.datagenerator.datareaders.ZipcodeReader;

public class DataLoader
{
	private InputStream getResource(File filename) throws Exception
	{
		InputStream stream = getClass().getResourceAsStream("/input_data/" + filename);
		return new BufferedInputStream(stream);
	}
	
	public InputData loadData() throws Exception
	{
		
		System.out.println("Reading zipcode data");
		ZipcodeReader zipcodeReader = new ZipcodeReader();
		zipcodeReader.setCoordinatesFile(getResource(Constants.COORDINATES_FILE));
		zipcodeReader.setIncomesFile(getResource(Constants.INCOMES_FILE));
		zipcodeReader.setPopulationFile(getResource(Constants.POPULATION_FILE));
		List<ZipcodeRecord> zipcodeTable = zipcodeReader.readData();
		System.out.println("Read " + zipcodeTable.size() + " zipcode entries");
		
		System.out.println("Reading name data");
		NameReader nameReader = new NameReader(getResource(Constants.NAMEDB_FILE));
		Names names = nameReader.readData();
		System.out.println("Read " + names.getFirstNames().size() + " first names and " + names.getLastNames().size() + " last names");
		
		System.out.println("Reading product data");
		ProductsReader reader = new ProductsReader(getResource(Constants.PRODUCTS_FILE));
		Collection<ProductCategory> productCategories = reader.readData();
		System.out.println("Read " + productCategories.size() + " product categories");
		
		InputData inputData = new InputData(zipcodeTable, names, productCategories);
		
		return inputData;
	}
}
