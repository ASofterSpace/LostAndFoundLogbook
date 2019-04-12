/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.lostAndFoundLogbook;

import com.asofterspace.toolbox.Utils;


public class Main {

	public final static String PROGRAM_TITLE = "LostAndFoundLogbook";
	public final static String VERSION_NUMBER = "0.0.0.1(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "10. April 2019";

	public static void main(String[] args) {

		// let the Utils know in what program it is being used
		Utils.setProgramTitle(PROGRAM_TITLE);
		Utils.setVersionNumber(VERSION_NUMBER);
		Utils.setVersionDate(VERSION_DATE);

		System.out.println("The " + Utils.getFullProgramIdentifierWithDate() + " has been started...");

		Server server = new Server();

		server.serve();

		System.out.println("The " + Utils.getFullProgramIdentifierWithDate() + " has been stopped; goodbye! :)");
	}
}
