package com.github.rnowling.bps.datagenerator.generators.purchasingprofile;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.datareaders.ProductsReader;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.markovmodels.MarkovModel;
import com.github.rnowling.bps.datagenerator.framework.samplers.UniformSampler;
import com.github.rnowling.bps.datagenerator.generators.purchasingprofile.ProductCategoryMarkovModelSampler;
import com.github.rnowling.bps.datagenerator.resources.Constants;

public class TestProductCategoryMarkovModelSampler
{

	@Test
	public void testSample() throws Exception
	{
		SeedFactory seedFactory = new SeedFactory(1245);
		
		ProductsReader reader = new ProductsReader(Constants.PRODUCTS_FILE);
		
		List<ProductCategory> productCategories = reader.readData();
		
		ProductCategory productCategory = productCategories.get(0);
		
		ProductCategoryMarkovModelSampler generator = new ProductCategoryMarkovModelSampler(productCategory, 
				new UniformSampler(seedFactory), new UniformSampler(seedFactory), new UniformSampler(seedFactory)
				);
		
		MarkovModel<Product> model = generator.sample();
		
		assertNotNull(model);
		assertNotNull(model.getStartWeights());
		assertNotNull(model.getTransitionWeights());
		assertTrue(model.getStartWeights().size() > 0);
		assertTrue(model.getTransitionWeights().size() > 0);
	}

}
