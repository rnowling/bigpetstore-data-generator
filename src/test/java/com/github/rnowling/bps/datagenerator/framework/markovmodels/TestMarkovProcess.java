package com.github.rnowling.bps.datagenerator.framework.markovmodels;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.Arrays;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.markovmodels.MarkovModel;
import com.github.rnowling.bps.datagenerator.framework.markovmodels.MarkovModelBuilder;
import com.github.rnowling.bps.datagenerator.framework.markovmodels.MarkovProcess;

public class TestMarkovProcess
{

	@Test
	public void test() throws Exception
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
