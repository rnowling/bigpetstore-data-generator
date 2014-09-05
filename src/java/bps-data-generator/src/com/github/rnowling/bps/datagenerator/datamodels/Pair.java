package com.github.rnowling.bps.datagenerator.datamodels;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class Pair<A, B>
{
	A first;
	B second;
	
	public Pair(A first, B second)
	{
		this.first = first;
		this.second = second;
	}

	public A getFirst()
	{
		return first;
	}

	public B getSecond()
	{
		return second;
	}
	
	public static <A, B> Pair<A, B> create(A first, B second)
	{
		return new Pair<A, B>(first, second);
	}
	
	public static <A, B> List<Pair<A, B>> create(Map<A, B> map)
	{
		List<Pair<A, B>> list = Lists.newArrayListWithExpectedSize(map.size());
		for(Map.Entry<A, B> entry : map.entrySet())
			list.add(Pair.create(entry.getKey(), entry.getValue()));
		return list;
	}
}
