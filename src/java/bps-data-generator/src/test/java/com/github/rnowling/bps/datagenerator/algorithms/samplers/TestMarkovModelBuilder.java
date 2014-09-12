package com.github.rnowling.bps.datagenerator.algorithms.samplers;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.matchers.JUnitMatchers.*;

import com.github.rnowling.bps.datagenerator.algorithms.markovmodels.MarkovModel;
import com.github.rnowling.bps.datagenerator.algorithms.markovmodels.MarkovModelBuilder;

public class TestMarkovModelBuilder
{

	@Test
	public void testAddStateState()
	{
		MarkovModelBuilder<String> builder = MarkovModelBuilder.create();
		
		builder.addStartState("a", 1.0);
		
		MarkovModel<String> msm = builder.build();
		
		assertThat(msm.getStartWeights().keySet(), hasItem("a"));
		assertEquals((double) msm.getStartWeights().get("a"), (double) 1.0, 0.000001);
		
	}
	
	@Test
	public void testAddEdgeTransition()
	{
		MarkovModelBuilder<String> builder = MarkovModelBuilder.create();
		
		builder.addTransition("a", "b", 1.0);
		
		MarkovModel<String> msm = builder.build();
		
		assertThat(msm.getTransitionWeights().rowKeySet(), hasItem("a"));
		assertThat(msm.getTransitionWeights().columnKeySet(), hasItem("b"));
		assertEquals((double) msm.getTransitionWeights().get("a", "b"), (double) 1.0, 0.000001);	
	}
	
	@Test
	public void testBuildMSM()
	{
		MarkovModelBuilder<String> builder = MarkovModelBuilder.create();
		
		builder.addStartState("a", 1.0);
		builder.addTransition("a", "b", 1.0);
		builder.addTransition("a", "c", 1.0);
		
		MarkovModel<String> msm = builder.build();
		
		assertThat(msm.getStartWeights().keySet(), hasItem("a"));
		assertThat(msm.getTransitionWeights().rowKeySet(), hasItem("a"));
		assertThat(msm.getTransitionWeights().columnKeySet(), hasItem("b"));
		assertThat(msm.getTransitionWeights().columnKeySet(), hasItem("c"));
		assertEquals((double) msm.getTransitionWeights().get("a", "b"), (double) 1.0, 0.000001);
		assertEquals((double) msm.getTransitionWeights().get("a", "c"), (double) 1.0, 0.000001);
	}

}
