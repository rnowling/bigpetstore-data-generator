package com.github.rnowling.bps.datagenerator.datareaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

public class ProductsReader
{
	File path;
	
	public ProductsReader(String path)
	{
		this.path = new File(path);
	}
	
	protected Product parseProduct(Object productJson)
	{
		Map<String, Object> fields = (Map<String, Object>) productJson;
		Product product = new Product(fields);
		return product;
	}
	
	protected ProductCategory parseProductCategory(Object productCategoryObject) throws Exception
	{
		Map<String, Object> jsonProductCategory = (Map<String, Object>) productCategoryObject;

		ProductCategoryBuilder builder = new ProductCategoryBuilder();
		
		for(Map.Entry<String, Object> entry : jsonProductCategory.entrySet())
		{
			Object key = entry.getKey();
			Object value = entry.getValue();

			if(key.equals("category"))
			{
				builder.setCategory( (String) entry.getValue());
			}
			else if(key.equals("species"))
			{
				for(String species : (List<String>) value)
				{
					if(species.equals("dog"))
					{
						builder.addApplicableSpecies(PetSpecies.DOG);
					}
					else if(species.equals("cat"))
					{
						builder.addApplicableSpecies(PetSpecies.CAT);
					}
					else
					{
						throw new Exception("Invalid species " + species + " encountered when parsing product categories JSON.");
					}
				}
			}
			else if(key.equals("trigger_transaction"))
			{
				builder.setTriggerTransaction((Boolean) entry.getValue()); 
			}
			else if(key.equals("fields"))
			{
				for(String fieldName : (List<String>) value)
				{
					builder.addFieldName(fieldName);
				}
			}
			else if(key.equals("daily_usage_rate"))
			{
				builder.setDailyUsageRate((Double) value);
			}
			else if(key.equals("base_amount_used_average"))
			{
				builder.setAmountUsedPetPetAverage((Double) value);
			}
			else if(key.equals("base_amount_used_variance"))
			{
				builder.setAmountUsedPetPetVariance((Double) value);
			}
			else if(key.equals("transaction_trigger_rate"))
			{
				builder.setTriggerTransactionRate((Double) value);
			}
			else if(key.equals("transaction_purchase_rate"))
			{
				builder.setTriggerPurchaseRate((Double) value);
			}
			else if(key.equals("items"))
			{
				for(Object productJson : (List<Object>) value)
				{
					Product product = parseProduct(productJson);
					builder.addProduct(product);
				}
			}
			else
			{
				throw new Exception("Invalid field " + key + " encountered when parsing product categories JSON.");
			}
			
		}
		
		return builder.build();
	}
	
	public List<ProductCategory> readData() throws Exception
	{
		Gson gson = new Gson();
		
		
		Reader reader = new BufferedReader(new FileReader(path));
		Object json = gson.fromJson(reader, Object.class);
		
		List<Object> productCategoryObjects = (List<Object>) json;

		List<ProductCategory> productCategories = Lists.newArrayList();
		
		for(Object obj : productCategoryObjects)
		{
			ProductCategory productCategory = parseProductCategory(obj);
			productCategories.add(productCategory);
		}
		
		reader.close();
		
		return productCategories;
		
	}
}
