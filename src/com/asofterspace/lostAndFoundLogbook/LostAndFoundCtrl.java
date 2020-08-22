/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.lostAndFoundLogbook;

import com.asofterspace.toolbox.configuration.ConfigFile;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.io.JSON;
import com.asofterspace.toolbox.io.JsonFile;
import com.asofterspace.toolbox.io.JsonParseException;
import com.asofterspace.toolbox.web.WebTemplateEngine;

import java.util.List;


public class LostAndFoundCtrl {

	public final static String DATA_DIR = "data";
	public final static String SERVER_DIR = "server";
	public final static String DEPLOYED_DIR = "deployed";

	private Server server;


	public void startup() throws JsonParseException {
		startup(false);
	}

	public void startupAsync() throws JsonParseException {
		startup(true);
	}

	public void startup(boolean async) throws JsonParseException {

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

		// automatically add specific project files to the server without having to list them all
		// manually in the webengine.json, and without letting them be templated by the WebTemplateEngine!
		// (as that would take a lot of time at startup each time...)
		List<String> whitelist = jsonConfig.getArrayAsStringList("files");
		boolean recursively = true;
		List<File> dataFiles = dataDir.getAllFiles(recursively);
		for (File dataFile : dataFiles) {
			whitelist.add(dataDir.getRelativePath(dataFile).replace('\\', '/'));
		}

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
