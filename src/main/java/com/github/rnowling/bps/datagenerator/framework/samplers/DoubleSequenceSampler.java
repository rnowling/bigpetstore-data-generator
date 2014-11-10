package com.github.rnowling.bps.datagenerator.framework.samplers;

public class DoubleSequenceSampler implements Sampler<Double>
{
	Double start;
	Double end;
	Double step;
	Double next;
	
	public DoubleSequenceSampler()
	{
		start = 0.0;
		end = null;
		step = 1.0;
		next = start;
	}
	
	public DoubleSequenceSampler(Double start)
	{
		this.start = start;
		end = null;
		step = 1.0;
		next = start;
	}
	
	public DoubleSequenceSampler(Double start, Double end)
	{
		this.start = start;
		this.end = end;
		step = 1.0;
		next = start;
	}
	
	public DoubleSequenceSampler(Double start, Double end, Double step)
	{
		this.start = start;
		this.end = end;
		this.step = step;
		next = start;
	}
	
	public Double sample() throws Exception
	{
		if(end == null || next < end)
		{
			Double current = next;
			next = current + step;
			return current;
		}
		
		throw new Exception("All values have been sampled");
	}
	
	
}
