package com.github.rnowling.bps.datagenerator.generators.transaction;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestTransactionTimeArrowOfTimePDF
{
	
	@Test
	public void testProbability() throws Exception
	{
		TransactionTimeArrowOfTimePDF pdf = new TransactionTimeArrowOfTimePDF();
		
		assertEquals(pdf.probability(0.5, 0.75), 0.0, 0.000001);
		assertEquals(pdf.probability(0.5, 0.5), 1.0, 0.000001);
		assertEquals(pdf.probability(0.75, 0.5), 1.0, 0.000001);
	}

}
