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
package com.github.rnowling.bps.datagenerator.datareaders;

import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.Names;

public class TestNameReader
{
	private InputStream getResource(File filename) throws Exception
	{
		InputStream stream = getClass().getResourceAsStream("/input_data/" + filename);
		return new BufferedInputStream(stream);
	}
	
	@Test
	public void testRead() throws Exception
	{
		NameReader reader = new NameReader(getResource(Constants.NAMEDB_FILE));
		Names names = reader.readData();
		
		assertTrue(names.getFirstNames().size() > 0);
		assertTrue(names.getLastNames().size() > 0);
		
	}
}
