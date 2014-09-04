package com.github.rnowling.bps.datagenerator.datamodels.inputs;

import java.util.Collections;
import java.util.List;

public class InputData
{
	List<ZipcodeRecord> zipcodeTable;
	Names names;
	
	public InputData(List<ZipcodeRecord> zipcodeTable,
			Names names)
	{
		this.zipcodeTable = Collections.unmodifiableList(zipcodeTable);
		this.names = names;
	}
	
	public List<ZipcodeRecord> getZipcodeTable()
	{
		return zipcodeTable;
	}
	
	public Names getNames()
	{
		return names;
	}
	
	
}
