package com.github.rnowling.bps.datagenerator.datareaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.resources.Constants;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class ZipcodeReader
{
	File zipcodeIncomesFile = null;
	File zipcodePopulationFile = null;
	File zipcodeCoordinatesFile = null;
	
	public void setIncomesFile(File path)
	{
		this.zipcodeIncomesFile = path;
	}
	
	public void setPopulationFile(File path)
	{
		this.zipcodePopulationFile = path;
	}
	
	public void setCoordinatesFile(File p)
	{
		this.zipcodeCoordinatesFile = p;
	}
	
	private ImmutableMap<String, Double> readIncomeData(File path) throws FileNotFoundException
	{
		Scanner scanner = new Scanner(path);
		
		// skip headers
		scanner.nextLine();
		scanner.nextLine();
		
		Map<String, Double> entries = Maps.newHashMap();
		while(scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			String[] cols = line.split(",");
			// zipcodes are in the form "ZCTA5 XXXXX"
			String zipcode = cols[2].split(" ")[1].trim();
			try
			{
				double medianHouseholdIncome = Integer.parseInt(cols[5].trim());
				entries.put(zipcode, medianHouseholdIncome);
			}
			catch(NumberFormatException e)
			{
				
			}
		}
		
		scanner.close();
		
		return ImmutableMap.copyOf(entries);
	}
	
	private ImmutableMap<String, Long> readPopulationData(File path) throws FileNotFoundException
	{
		Scanner scanner = new Scanner(path);
		
		// skip header
		scanner.nextLine();
		
		Map<String, Long> entries = Maps.newHashMap();
		while(scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			
			if(line.length() == 0)
				continue;
			
			String[] cols = line.split(",");
			
			String zipcode = cols[0].trim();
			Long population = Long.parseLong(cols[1].trim());
			
			if(entries.containsKey(zipcode))
			{
				entries.put(zipcode, Math.max(entries.get(zipcode), population));
			}
			else
			{
				entries.put(zipcode, population);
			}
		}
		
		scanner.close();
		
		return ImmutableMap.copyOf(entries);
	}
	
	private ImmutableMap<String, Pair<Double, Double>> readCoordinates(File path) throws FileNotFoundException
	{
		Scanner scanner = new Scanner(path);
		
		// skip header
		scanner.nextLine();
		
		Map<String, Pair<Double, Double>> entries = Maps.newHashMap();
		while(scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			
			String[] cols = line.split(", ");
			
			// remove quote marks
			String zipcode = cols[0].substring(1, cols[0].length() - 1);
			Double latitude = Double.parseDouble(cols[2].substring(1, cols[0].length() - 1));
			Double longitude = Double.parseDouble(cols[3].substring(1, cols[0].length() - 1));
			
			Pair<Double, Double> coords = new Pair<Double, Double>(latitude, longitude);

			entries.put(zipcode, coords);
		}
		
		scanner.close();
		
		return ImmutableMap.copyOf(entries);
	}
	
	public ImmutableList<ZipcodeRecord> readData() throws FileNotFoundException
	{
		ImmutableMap<String, Double> incomes = readIncomeData(this.zipcodeIncomesFile);
		ImmutableMap<String, Long> populations = readPopulationData(this.zipcodePopulationFile);
		ImmutableMap<String, Pair<Double, Double>> coordinates = readCoordinates(this.zipcodeCoordinatesFile);
		
		Set<String> zipcodeSubset = new HashSet<String>(incomes.keySet());
		zipcodeSubset.retainAll(populations.keySet());
		zipcodeSubset.retainAll(coordinates.keySet());
		
		List<ZipcodeRecord> table = new Vector<ZipcodeRecord>();
		for(String zipcode : zipcodeSubset)
		{
			ZipcodeRecord record = new ZipcodeRecord(zipcode, 
					coordinates.get(zipcode), incomes.get(zipcode),
					populations.get(zipcode));
			table.add(record);
		}
		
		return ImmutableList.copyOf(table);
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		ZipcodeReader reader = new ZipcodeReader();
		reader.setCoordinatesFile(Constants.COORDINATES_FILE);
		reader.setIncomesFile(Constants.ACS_ann);
		reader.setPopulationFile(Constants.POPULATION_FILE);
		
		List<ZipcodeRecord> table = reader.readData();
		
		System.out.println(table.size());
	}
}
