package com.github.rnowling.bps.datagenerator.generators.transaction;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.markovmodels.MarkovModel;
import com.github.rnowling.bps.datagenerator.algorithms.markovmodels.MarkovModelBuilder;
import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.PurchasingProfile;
import com.github.rnowling.bps.datagenerator.datareaders.ProductCategoryBuilder;
import com.github.rnowling.bps.datagenerator.generators.purchasingprofile.PurchasingProfileBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TestTransactionPurchasesHiddenMarkovModel
{
	
	private List<Product> createProducts(String category)
	{
		List<Product> products = Lists.newArrayList();
		
		for(int i = 0; i < 10; i++)
		{
			Map<String, Object> fields = Maps.newHashMap();
			fields.put(Constants.PRODUCT_CATEGORY, category);
			fields.put(Constants.PRODUCT_QUANTITY, (double) (i + 1));
			Product product = new Product(fields);
			products.add(product);
		}
		
		return products;
	}
	
	private MarkovModel<Product> createMarkovModel(ProductCategory category)
	{
		MarkovModelBuilder<Product> markovBuilder = new MarkovModelBuilder<Product>();
		
		for(Product product1 : category.getProducts())
		{
			markovBuilder.addStartState(product1, 1.0);
			for(Product product2 : category.getProducts())
			{
				markovBuilder.addTransition(product1, product2, 1.0);
			}
		}
		
		return markovBuilder.build();
	}
	
	protected PurchasingProcesses createProcesses(ProductCategory dogFoodCategory,
			ProductCategory catFoodCategory, SeedFactory seedFactory)
	{	
		MarkovModel<Product> dogFoodModel = createMarkovModel(dogFoodCategory);
		MarkovModel<Product> catFoodModel = createMarkovModel(catFoodCategory);
		
		PurchasingProfileBuilder profileBuilder = new PurchasingProfileBuilder();
		profileBuilder.addProfile("dog food", dogFoodModel);
		profileBuilder.addProfile("cat food", catFoodModel);
		
		PurchasingProfile profile = profileBuilder.build();
		
		PurchasingProcessesBuilder builder = new PurchasingProcessesBuilder(seedFactory);
		builder.setPurchasingProfile(profile);
		
		return builder.build();
	}
	
	protected ProductCategory createCategory(String category)
	{
		List<Product> products = createProducts(category);
		
		ProductCategoryBuilder builder = new ProductCategoryBuilder();
		
		if(category.equals("dog food"))
		{
			builder.addApplicableSpecies(PetSpecies.DOG);
		}
		else
		{
			builder.addApplicableSpecies(PetSpecies.CAT);
		}
		
		builder.setAmountUsedPetPetAverage(1.0);
		builder.setAmountUsedPetPetVariance(1.0);
		builder.setDailyUsageRate(2.0);
		builder.setCategory(category);
		builder.setTriggerPurchaseRate(1.0 / 10.0);
		builder.setTriggerPurchaseRate(1.0 / 10.0);
		
		for(Product product : products)
		{
			builder.addProduct(product);
		}
		
		return builder.build();
	}

	protected TransactionPurchasesHiddenMarkovModel createHMM()
	{
		SeedFactory seedFactory = new SeedFactory(1234);
		
		ProductCategory dogFoodCategory = createCategory("dog food");
		ProductCategory catFoodCategory = createCategory("cat food");
		
		PurchasingProcesses processes = createProcesses(dogFoodCategory, catFoodCategory, seedFactory);
		
		CustomerTransactionParametersGenerator generator = new CustomerTransactionParametersGenerator(seedFactory);
		CustomerTransactionParameters parameters = generator.generate();
		
		CustomerInventoryBuilder inventoryBuilder = new CustomerInventoryBuilder(parameters, seedFactory);
		inventoryBuilder.addProductCategory(dogFoodCategory);
		inventoryBuilder.addProductCategory(catFoodCategory);
		CustomerInventory inventory = inventoryBuilder.build();
		
		TransactionPurchasesHiddenMarkovModel hmm = new TransactionPurchasesHiddenMarkovModel(processes,
				parameters, inventory, 0.0, seedFactory);
		
		return hmm;
	}
	
	@Test
	public void testChooseCategory()
	{
		TransactionPurchasesHiddenMarkovModel hmm = createHMM();
		
		String category = hmm.chooseCategory();
		
		assertNotNull(category);
		assertTrue(category.equals(TransactionPurchasesHiddenMarkovModel.STOP_STATE) ||
				category.equals("dog food") ||
				category.equals("cat food"));
	}
	
	@Test
	public void testChooseProduct()
	{
		TransactionPurchasesHiddenMarkovModel hmm = createHMM();
		
		Product product = hmm.chooseProduct("dog food");
		
		assertNotNull(product);
		assertTrue(product.getFieldValue(Constants.PRODUCT_CATEGORY).equals("dog food"));
		
		product = hmm.chooseProduct("cat food");
		
		assertNotNull(product);
		assertTrue(product.getFieldValue(Constants.PRODUCT_CATEGORY).equals("cat food"));
	}
	
	@Test
	public void testSample()
	{	
		TransactionPurchasesHiddenMarkovModel hmm = createHMM();
		
		Product product = hmm.sample();
		
		// first product should never be null
		assertNotNull(product);
		assertTrue(product.getFieldValue(Constants.PRODUCT_CATEGORY).equals("dog food") || 
				product.getFieldValue(Constants.PRODUCT_CATEGORY).equals("cat food"));
		
		for(int i = 0; i < 10; i++)
		{
			product = hmm.sample();
		}
		
	}

}
