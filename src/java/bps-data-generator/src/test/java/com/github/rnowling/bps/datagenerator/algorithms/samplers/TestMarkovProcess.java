package com.github.rnowling.bps.datagenerator.algorithms.samplers;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.markovmodels.MarkovModel;
import com.github.rnowling.bps.datagenerator.algorithms.markovmodels.MarkovModelBuilder;
import com.github.rnowling.bps.datagenerator.algorithms.markovmodels.MarkovProcess;

public class TestMarkovProcess
{

	@Test
	public void test()
	{
		SeedFactory factory = new SeedFactory(1245);
		MarkovModelBuilder<String> builder = MarkovModelBuilder.create();
		
		builder.addStartState("a", 1.0);
		builder.addTransition("a", "b", 1.0);
		builder.addTransition("a", "c", 1.0);
		
		MarkovModel<String> msm = builder.build();
		MarkovProcess<String> process = MarkovProcess.create(msm, factory);
		
		String firstState = process.sample();
		assertEquals(firstState, "a");
		
		String secondState = process.sample();
		assertThat(Arrays.asList("b", "c"), hasItem(secondState));
	}

}
