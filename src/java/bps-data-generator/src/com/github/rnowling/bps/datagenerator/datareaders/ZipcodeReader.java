package com.github.rnowling.bps.datagenerator.datareaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;

public class ZipcodeReader
{
	File zipcodeIncomesFile = null;
	File zipcodePopulationFile = null;
	File zipcodeCoordinatesFile = null;
	
	public void setIncomesFile(String path)
	{
		this.zipcodeIncomesFile = new File(path);
	}
	
	public void setPopulationFile(String path)
	{
		this.zipcodePopulationFile = new File(path);
	}
	
	public void setCoordinatesFile(String path)
	{
		this.zipcodeCoordinatesFile = new File(path);
	}
	
	private Map<String, Double> readIncomeData(File path) throws FileNotFoundException
	{
		Scanner scanner = new Scanner(path);
		
		// skip headers
		scanner.nextLine();
		scanner.nextLine();
		
		Map<String, Double> entries = new HashMap<String, Double>();
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
		
		return entries;
	}
	
	private Map<String, Long> readPopulationData(File path) throws FileNotFoundException
	{
		Scanner scanner = new Scanner(path);
		
		// skip header
		scanner.nextLine();
		
		Map<String, Long> entries = new HashMap<String, Long>();
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
		
		return entries;
	}
	
	private Map<String, Pair<Double, Double>> readCoordinates(File path) throws FileNotFoundException
	{
		Scanner scanner = new Scanner(path);
		
		// skip header
		scanner.nextLine();
		
		Map<String, Pair<Double, Double>> entries = new HashMap<String, Pair<Double, Double>>();
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
		
		return entries;
	}
	
	public List<ZipcodeRecord> readData() throws FileNotFoundException
	{
		Map<String, Double> incomes = readIncomeData(this.zipcodeIncomesFile);
		Map<String, Long> populations = readPopulationData(this.zipcodePopulationFile);
		Map<String, Pair<Double, Double>> coordinates = readCoordinates(this.zipcodeCoordinatesFile);
		
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
		
		return Collections.unmodifiableList(table);
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		ZipcodeReader reader = new ZipcodeReader();
		reader.setCoordinatesFile("resources/zips.csv");
		reader.setIncomesFile("resources/ACS_12_5YR_S1903/ACS_12_5YR_S1903_with_ann.csv");
		reader.setPopulationFile("resources/population_data.csv");
		
		List<ZipcodeRecord> table = reader.readData();
		
		System.out.println(table.size());
	}
}
