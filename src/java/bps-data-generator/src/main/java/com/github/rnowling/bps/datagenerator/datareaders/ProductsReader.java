package com.github.rnowling.bps.datagenerator.datareaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

import com.github.rnowling.bps.datagenerator.datamodels.ProductCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ProductsReader
{
	File path;
	
	public ProductsReader(String path)
	{
		this.path = new File(path);
	}
	
	public List<ProductCategory> readData() throws IOException
	{
		Gson gson = new Gson();
		
		
		Reader reader = new BufferedReader(new FileReader(path));
		Type type = new TypeToken<List<ProductCategory>>(){}.getType();
		List<ProductCategory> productCategories = gson.fromJson(reader, type);
		
		reader.close();
		
		return productCategories;
		
	}
}
