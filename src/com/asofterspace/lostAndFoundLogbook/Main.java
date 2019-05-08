/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.lostAndFoundLogbook;

import com.asofterspace.toolbox.configuration.ConfigFile;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.JSON;
import com.asofterspace.toolbox.io.JsonFile;
import com.asofterspace.toolbox.io.XmlFile;
import com.asofterspace.toolbox.Utils;
import com.asofterspace.toolbox.web.WebTemplateEngine;

import java.util.ArrayList;
import java.util.List;


public class Main {

	public final static String PROGRAM_TITLE = "LostAndFoundLogbook";
	public final static String VERSION_NUMBER = "0.0.0.4(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "10. April 2019 - 8. May 2019";


	public static void main(String[] args) {

		// let the Utils know in what program it is being used
		Utils.setProgramTitle(PROGRAM_TITLE);
		Utils.setVersionNumber(VERSION_NUMBER);
		Utils.setVersionDate(VERSION_DATE);

		System.out.println("The " + Utils.getFullProgramIdentifierWithDate() + " has been started...");

		System.out.println("Loading the preexisting data about lost and found objects...");

		Directory dataDir = new Directory("data");

		Database db = new Database(dataDir);

		System.out.println("Templating the web application...");

		Directory origDir = new Directory("server");

		JsonFile jsonConfigFile = new JsonFile(origDir, "webengine.json");
		JSON jsonConfig = jsonConfigFile.getAllContents();

		WebTemplateEngine engine = new WebTemplateEngine(origDir, jsonConfig);

		Directory webRoot = new Directory("deployed");

		engine.compileTo(webRoot);

		System.out.println("Templating done, serving data now...");

		Server server = new Server(webRoot, db);

		List<String> whitelist = jsonConfig.getArrayAsStringList("files");

		server.setFileLocationWhitelist(whitelist);

		server.serve();

		System.out.println("The " + Utils.getFullProgramIdentifierWithDate() + " has been stopped; goodbye! :)");
	}
}
