package com.github.rnowling.bps.datagenerator.generators.transaction;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestTransactionTimePDF
{
	
	@Test
	public void testProbability() throws Exception
	{
		TransactionTimePDF pdf = new TransactionTimePDF();
		
		assertEquals(pdf.probability(0.5, 0.75), 0.0, 0.000001);
		assertEquals(pdf.probability(0.5, 0.5), 1.0, 0.000001);
		assertEquals(pdf.probability(0.75, 0.5), 1.0, 0.000001);
	}
	
	@Test
	public void testFixConditional() throws Exception
	{
		TransactionTimePDF pdf = new TransactionTimePDF();
		
		assertEquals(pdf.fixConditional(0.75).probability(0.5), 0.0, 0.000001);
		assertEquals(pdf.fixConditional(0.5).probability(0.5), 1.0, 0.000001);
		assertEquals(pdf.fixConditional(0.5).probability(0.75), 1.0, 0.000001);
	}

}
