package com.github.rnowling.bps.datagenerator.datareaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.Names;
import com.google.common.collect.Maps;

public class NameReader
{
	File path;
	
	public NameReader(File path)
	{
		this.path = path;
	}
	
	public Names readData() throws FileNotFoundException
	{
		Scanner scanner = new Scanner(path);
		
		Map<String, Double> firstNames = Maps.newHashMap();
		Map<String, Double> lastNames = Maps.newHashMap();
		
		while(scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			String[] cols = line.trim().split(",");
			
			String name = cols[0];
			double weight = Double.parseDouble(cols[5]);
			
			if(cols[4].equals("1"))
				firstNames.put(name, weight);
			if(cols[3].equals("1"))
				lastNames.put(name, weight);
		}
		
		scanner.close();
		
		return new Names(firstNames, lastNames);
		
	}
}
