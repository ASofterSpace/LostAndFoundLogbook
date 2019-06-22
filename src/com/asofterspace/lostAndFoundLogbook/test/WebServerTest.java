/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.lostAndFoundLogbook.test;

import com.asofterspace.lostAndFoundLogbook.LostAndFoundCtrl;
import com.asofterspace.toolbox.io.DefaultImageFile;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.io.JSON;
import com.asofterspace.toolbox.io.SimpleFile;
import com.asofterspace.toolbox.test.Test;
import com.asofterspace.toolbox.test.TestUtils;
import com.asofterspace.toolbox.utils.Image;
import com.asofterspace.toolbox.web.WebAccessor;
import com.asofterspace.toolbox.web.WebServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WebServerTest implements Test {

	private LostAndFoundCtrl ctrl;

	private String url;


	@Override
	public void runAll() {

		WebAccessor.clearCache();

		ctrl = new LostAndFoundCtrl();
		ctrl.startupAsync();

		url = "http://" + ctrl.getServer().getAddress() + ":" + ctrl.getServer().getPort() + "/";


		communicateWithServerTest();

		getItemsFromServerTest();

		getFileTest();


		ctrl.stop();
	}

	public void communicateWithServerTest() {

		TestUtils.start("Communicate with Server");

		String result = WebAccessor.get(url);

		if (!result.contains("<div id=\"headline\">Nowhere Lost and Found Logbook</div>")) {
			TestUtils.fail("We tried to request the index file from our own server... and did not get it!");
			return;
		}

		TestUtils.succeed();
	}

	public void getItemsFromServerTest() {

		TestUtils.start("Get Items from Server");

		// get all
		Map<String, String> jsonType = new HashMap<>();
		jsonType.put("Content-Type", "application/json");
		String result = WebAccessor.post(url + "items", "{\"id\": \"*\"}", jsonType);

		JSON jsonContent = new JSON(result);

		if (jsonContent == null) {
			TestUtils.fail("We wanted to get all items, but got null!");
			return;
		}

		if (jsonContent.getString("foundItems") == null) {
			TestUtils.fail("We wanted to get all items, but did not get them!");
			return;
		}

		TestUtils.succeed();
	}

	public void getFileTest() {

		TestUtils.start("Get File from Server");

		DefaultImageFile origImageFile = new DefaultImageFile(LostAndFoundCtrl.SERVER_DIR + "/logo.png");

		Image origImage = origImageFile.getImage();

		File gotFile = WebAccessor.getFile(url + "logo.png?v=blubb");

		DefaultImageFile gotImageFile = new DefaultImageFile(gotFile);

		Image gotImage = gotImageFile.getImage();

		if (!origImage.equals(gotImage)) {
			TestUtils.fail("We tried to request an image file from our own server... and did not get it!");
			return;
		}

		TestUtils.succeed();
	}

}
