/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.lostAndFoundLogbook;

import com.asofterspace.lostAndFoundLogbook.test.AllTests;
import com.asofterspace.toolbox.io.JsonParseException;
import com.asofterspace.toolbox.Utils;


public class Main {

	public final static String PROGRAM_TITLE = "LostAndFoundLogbook";
	public final static String VERSION_NUMBER = "0.0.1.0(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "10. April 2019 - 22. August 2020";


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

		try {
			ctrl.startup();

		} catch (JsonParseException e) {
			System.err.println("Loading the settings failed:");
			System.err.println(e);
			System.exit(1);
		}

		System.out.println("The " + Utils.getFullProgramIdentifierWithDate() + " has been stopped; goodbye! :)");
	}
}
