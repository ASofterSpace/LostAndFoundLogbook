/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.lostAndFoundLogbook;

import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.web.WebServer;
import com.asofterspace.toolbox.web.WebServerRequestHandler;

import java.net.Socket;


public class Server extends WebServer {

	private Database db;


	public Server(Directory webRoot, Database db) {

		super(webRoot);

		this.db = db;
	}

	protected WebServerRequestHandler getHandler(Socket request) {
		return new ServerRequestHandler(this, request, webRoot, db);
	}

}
