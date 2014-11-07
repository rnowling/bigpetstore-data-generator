package com.github.rnowling.bps.datagenerator.generators.transaction;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.PurchasingProfile;
import com.github.rnowling.bps.datagenerator.datamodels.PurchasingProfileBuilder;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.markovmodels.MarkovModel;
import com.github.rnowling.bps.datagenerator.framework.markovmodels.MarkovModelBuilder;
import com.github.rnowling.bps.datagenerator.generators.transaction.PurchasingProcesses;
import com.github.rnowling.bps.datagenerator.generators.transaction.PurchasingProcessesBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TestPurchasingProcessesBuilder
{
	
	@Test
	public void testBuild() throws Exception
	{	
		MarkovModelBuilder<Product> markovBuilder = new MarkovModelBuilder<Product>();
		
		List<Product> products = Lists.newArrayList();
		
		for(int i = 0; i < 10; i++)
		{
			Map<String, Object> fields = Maps.newHashMap();
			fields.put(Constants.PRODUCT_CATEGORY, "dog food");
			fields.put(Constants.PRODUCT_QUANTITY, (double) (i + 1));
			Product product = new Product(fields);
			products.add(product);
		}
		
		for(Product product1 : products)
		{
			markovBuilder.addStartState(product1, 1.0);
			for(Product product2 : products)
			{
				markovBuilder.addTransition(product1, product2, 1.0);
			}
		}
		
		MarkovModel<Product> model = markovBuilder.build();
		
		PurchasingProfileBuilder profileBuilder = new PurchasingProfileBuilder();
		profileBuilder.addProfile("dog food", model);
		
		PurchasingProfile profile = profileBuilder.build();
		
		SeedFactory seedFactory = new SeedFactory(1234);
		
		PurchasingProcessesBuilder builder = new PurchasingProcessesBuilder(seedFactory);
		builder.setPurchasingProfile(profile);
		
		PurchasingProcesses processes = builder.build();
		
		Product product = processes.sample("dog food");
		
		assertNotNull(product);
		assertNotNull(product.getFieldValue(Constants.PRODUCT_CATEGORY));
		assertNotNull(product.getFieldValue(Constants.PRODUCT_QUANTITY));
		
		product = processes.sample("dog food");
		
		assertNotNull(product);
		assertNotNull(product.getFieldValue(Constants.PRODUCT_CATEGORY));
		assertNotNull(product.getFieldValue(Constants.PRODUCT_QUANTITY));
	}

}
