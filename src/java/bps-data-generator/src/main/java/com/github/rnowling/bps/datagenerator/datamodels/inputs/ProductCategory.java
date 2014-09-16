package com.github.rnowling.bps.datagenerator.datamodels.inputs;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;

public class ProductCategory
{
	String category;
	Set<PetSpecies> species;
	Set<String> fields;
	Boolean trigger_transaction;
	Double daily_usage_rate;
	Double base_amount_used_average;
	Double base_amount_used_variance;
	Double transaction_trigger_rate;
	Double transaction_purchase_rate;
	List<Map<String, Object>> items;
	
	public String getCategory() {
		return category;
	}
	public Set<PetSpecies> getSpecies() {
		return species;
	}
	public Set<String> getFields() {
		return fields;
	}
	public Boolean getTriggerTransaction() {
		return trigger_transaction;
	}
	public Double getDailyUsageRate() {
		return daily_usage_rate;
	}
	public Double getBaseAmountUsedAverage() {
		return base_amount_used_average;
	}
	public Double getBaseAmountUsedVariance() {
		return base_amount_used_variance;
	}
	public Double getTransactionTriggerRate() {
		return transaction_trigger_rate;
	}
	public Double getPurchaseTriggerRate() {
		return transaction_purchase_rate;
	}
	public List<Map<String, Object>> getItems() {
		return items;
	}
	
	
	
	
}
