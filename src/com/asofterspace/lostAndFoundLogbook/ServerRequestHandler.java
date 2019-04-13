/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.lostAndFoundLogbook;

import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.JSON;
import com.asofterspace.toolbox.web.WebServer;
import com.asofterspace.toolbox.web.WebServerAnswer;
import com.asofterspace.toolbox.web.WebServerAnswerInJson;
import com.asofterspace.toolbox.web.WebServerRequestHandler;

import java.io.IOException;
import java.net.Socket;


public class ServerRequestHandler extends WebServerRequestHandler {

	private Database db;


	public ServerRequestHandler(WebServer server, Socket request, Directory webRoot, Database db) {

		super(server, request, webRoot);

		this.db = db;
	}

	@Override
	protected void handlePost(String fileLocation) throws IOException {

		String jsonData = receiveJsonContent();

		if (jsonData == null) {
			respond(400);
			return;
		}

		// TODO :: catch some exceptions? (or make sure that none are thrown?)
		JSON json = new JSON(jsonData);

		WebServerAnswer answer = null;

		switch (fileLocation) {

			case "/saveLostItem":
				db.saveLostItem(json);
				break;

			case "/saveFoundItem":
				db.saveFoundItem(json);
				break;

			case "/items":
				String jsonAnswer = db.getItems(json);
				if (jsonAnswer == null) {
					respond(400);
					return;
				}
				answer = new WebServerAnswerInJson(jsonAnswer);
				break;

			default:
				respond(404);
				return;
		}

		if (answer == null) {
			answer = new WebServerAnswerInJson("{\"success\": true}");
		}

		respond(200, answer);
	}
}
