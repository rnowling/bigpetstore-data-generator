package com.github.rnowling.bps.datagenerator.generators.purchasingprofile;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.algorithms.markovmodels.MarkovModel;
import com.github.rnowling.bps.datagenerator.algorithms.markovmodels.MarkovModelBuilder;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.PurchasingProfile;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TestPurchasingProfileBuilder
{

	@Test
	public void testGenerate() throws Exception
	{
		MarkovModelBuilder<Product> builder = MarkovModelBuilder.create();
		
		List<Product> products = Lists.newArrayList();
		for(int i = 0; i < 10; i++)
		{
			Map<String, Object> fields = Maps.newHashMap();
			fields.put("id", i);
			Product product = new Product(fields);
			builder.addStartState(product, 1.0);
		}
		
		for(Product product1 : products)
		{
			for(Product product2 : products)
			{
				builder.addTransition(product1, product2, 1.0);
			}
		}
		
		MarkovModel<Product> model = builder.build();
		
		PurchasingProfileBuilder profileBuilder = new PurchasingProfileBuilder();
		profileBuilder.addProfile("products", model);
		
		PurchasingProfile profile = profileBuilder.build();
		
		
		assertNotNull(profile);
		assertThat(profile.getProductCategories(), hasItem("products"));
		assertNotNull(profile.getProfile("products"));	
	}

}