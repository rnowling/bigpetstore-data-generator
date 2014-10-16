package com.github.rnowling.bps.datagenerator;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.resources.Constants;

public class TestResources {

	@Test
	public void test() {
		Assert.assertTrue((Constants.COORDINATES_FILE.exists()));
	}
}
