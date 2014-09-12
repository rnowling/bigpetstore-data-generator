package com.github.rnowling.bps.datagenerator.datareaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.Names;

public class NameReader
{
	File path;
	
	public NameReader(String path)
	{
		this.path = new File(path);
	}
	
	public Names readData() throws FileNotFoundException
	{
		Scanner scanner = new Scanner(path);
		
		List<Pair<String, Double>> firstNames = new Vector<Pair<String, Double>>();
		List<Pair<String, Double>> lastNames = new Vector<Pair<String, Double>>();
		
		while(scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			String[] cols = line.trim().split(",");
			
			String name = cols[0];
			double weight = Double.parseDouble(cols[5]);
			
			Pair<String, Double> pair = new Pair<String, Double>(name, weight);
			
			if(cols[4].equals("1"))
				firstNames.add(pair);
			if(cols[3].equals("1"))
				lastNames.add(pair);
		}
		
		scanner.close();
		
		return new Names(firstNames, lastNames);
		
	}
}
