/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.lostAndFoundLogbook.test;

import com.asofterspace.toolbox.test.TestUtils;


public class AllTests {

	static final String TEST_DATA_PATH = "testdata/";


	public static int run() {

		TestUtils.startSuite();

		TestUtils.run(new WebServerTest());

		TestUtils.endSuite();

		if (TestUtils.allWereSuccessful()) {
			return 0;
		} else {
			return 1;
		}
	}
}
