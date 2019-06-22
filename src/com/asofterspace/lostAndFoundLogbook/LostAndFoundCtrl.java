/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.lostAndFoundLogbook;

import com.asofterspace.lostAndFoundLogbook.test.AllTests;
import com.asofterspace.toolbox.configuration.ConfigFile;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.JSON;
import com.asofterspace.toolbox.io.JsonFile;
import com.asofterspace.toolbox.io.XmlFile;
import com.asofterspace.toolbox.Utils;
import com.asofterspace.toolbox.web.WebTemplateEngine;

import java.util.ArrayList;
import java.util.List;


public class LostAndFoundCtrl {

	public final static String DATA_DIR = "data";
	public final static String SERVER_DIR = "server";
	public final static String DEPLOYED_DIR = "deployed";

	private Server server;


	public void startup() {
		startup(false);
	}

	public void startupAsync() {
		startup(true);
	}

	public void startup(boolean async) {

		System.out.println("Loading the preexisting data about lost and found objects...");

		Directory dataDir = new Directory(DATA_DIR);

		Database db = new Database(dataDir);

		System.out.println("Templating the web application...");

		Directory origDir = new Directory(SERVER_DIR);

		JsonFile jsonConfigFile = new JsonFile(origDir, "webengine.json");
		JSON jsonConfig = jsonConfigFile.getAllContents();

		WebTemplateEngine engine = new WebTemplateEngine(origDir, jsonConfig);

		Directory webRoot = new Directory(DEPLOYED_DIR);

		engine.compileTo(webRoot);

		server = new Server(webRoot, db);

		List<String> whitelist = jsonConfig.getArrayAsStringList("files");

		server.setWhitelist(whitelist);

		System.out.println("Templating done, serving data now on " + server.getAddress() + ":" + server.getPort() + "...");

		server.serve(async);
	}

	public Server getServer() {
		return server;
	}

	public void stop() {
		server.stop();
	}

}
