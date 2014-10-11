package com.github.rnowling.bps.datagenerator.statistics.samplers;

public class SequenceSampler implements Sampler<Integer>
{
	Integer start;
	Integer end;
	Integer step;
	Integer next;
	
	public SequenceSampler()
	{
		start = 0;
		end = null;
		step = 1;
		next = start;
	}
	
	public SequenceSampler(Integer start)
	{
		this.start = start;
		end = null;
		step = 1;
		next = start;
	}
	
	public SequenceSampler(Integer start, Integer end)
	{
		this.start = start;
		this.end = end;
		step = 1;
		next = start;
	}
	
	public SequenceSampler(Integer start, Integer end, Integer step)
	{
		this.start = start;
		this.end = end;
		this.step = step;
		next = start;
	}
	
	public Integer sample() throws Exception
	{
		if(end == null || next < end)
		{
			Integer current = next;
			next = current + step;
			return current;
		}
		
		throw new Exception("All values have been sampled");
	}
	
	
}
