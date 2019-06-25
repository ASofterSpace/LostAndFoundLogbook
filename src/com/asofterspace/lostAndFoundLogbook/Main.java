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


public class Main {

	public final static String PROGRAM_TITLE = "LostAndFoundLogbook";
	public final static String VERSION_NUMBER = "0.0.0.8(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "10. April 2019 - 25. June 2019";


	public static void main(String[] args) {

		// let the Utils know in what program it is being used
		Utils.setProgramTitle(PROGRAM_TITLE);
		Utils.setVersionNumber(VERSION_NUMBER);
		Utils.setVersionDate(VERSION_DATE);

		if (args.length > 0) {
			if (args[0].equals("--test")) {
				System.exit(AllTests.run());
			}

			if (args[0].equals("--version")) {
				System.out.println(Utils.getFullProgramIdentifierWithDate());
				return;
			}

			if (args[0].equals("--version_for_zip")) {
				System.out.println("version " + Utils.getVersionNumber());
				return;
			}
		}

		System.out.println("The " + Utils.getFullProgramIdentifierWithDate() + " has been started...");

		LostAndFoundCtrl ctrl = new LostAndFoundCtrl();

		ctrl.startup();

		System.out.println("The " + Utils.getFullProgramIdentifierWithDate() + " has been stopped; goodbye! :)");
	}
}
