/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rnowling.bps.datagenerator.generators.customer;

import com.github.rnowling.bps.datagenerator.datamodels.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.Store;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.framework.samplers.ConditionalSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;

public class CustomerSampler implements Sampler<Customer>
{
	private final Sampler<Integer> idSampler;
	private final Sampler<String> firstNameSampler;
	private final Sampler<String> lastNameSampler;
	private final Sampler<Store> storeSampler;
	private final ConditionalSampler<ZipcodeRecord, Store> locationSampler;
	
	
	public CustomerSampler(Sampler<Integer> idSampler, Sampler<String> firstNameSampler,
			Sampler<String> lastNameSampler, Sampler<Store> storeSampler,
			ConditionalSampler<ZipcodeRecord, Store> locationSampler)
	{
		this.idSampler = idSampler;
		this.firstNameSampler = firstNameSampler;
		this.lastNameSampler = lastNameSampler;
		this.storeSampler = storeSampler;
		this.locationSampler = locationSampler;
	}

	public Customer sample() throws Exception
	{
		Integer id = idSampler.sample();
		Pair<String, String> name = Pair.create(firstNameSampler.sample(),
				lastNameSampler.sample());
		Store store = storeSampler.sample();
		ZipcodeRecord location = locationSampler.sample(store);
		
		return new Customer(id, name, store, location);
	}

}
