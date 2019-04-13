/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.lostAndFoundLogbook;

import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.JSON;
import com.asofterspace.toolbox.io.JsonFile;
import com.asofterspace.toolbox.io.SimpleFile;
import com.asofterspace.toolbox.io.XmlElement;
import com.asofterspace.toolbox.io.XmlFile;


public class Database {

	private Directory dataDir;

	private XmlFile xmlFile;

	private XmlElement xmlRoot = null;

	private int maxid = 0;

	private static final String DB_FILE_NAME = "data.xml";

	private static final String MAX_ID = "maxid";

	private static final String LOST_ITEMS = "lostItems";
	private static final String LOST_ITEM = "lostItem";

	private static final String FOUND_ITEMS = "foundItems";
	private static final String FOUND_ITEM = "foundItem";


	public Database(Directory dataDir) {

		this.dataDir = dataDir;

		dataDir.create();

		loadDatabase();
	}

	public void saveLostItem(JSON item) {

		XmlElement lostItems = xmlRoot.getChild(LOST_ITEMS);

		XmlElement lostItem = lostItems.createChild(LOST_ITEM);

		increaseMaxId();

		lostItem.createChild("id").setInnerText(maxid);
		lostItem.createChild("what").setInnerText(item.getString("what"));
		lostItem.createChild("when").setInnerText(item.getString("when"));
		lostItem.createChild("where").setInnerText(item.getString("where"));
		lostItem.createChild("who").setInnerText(item.getString("who"));
		lostItem.createChild("contactonsite").setInnerText(item.getString("contactonsite"));
		lostItem.createChild("contactoffsite").setInnerText(item.getString("contactoffsite"));

		saveDatabase();
	}

	private void increaseMaxId() {

		maxid = maxid + 1;

		XmlElement maxidEl = xmlRoot.getChild(MAX_ID);

		if (maxidEl == null) {
			maxidEl = xmlRoot.createChild(MAX_ID);
		}

		maxidEl.setInnerText(maxid);
	}

	public void loadDatabase() {

		xmlFile = new XmlFile(dataDir, DB_FILE_NAME);

		if (xmlFile.exists()) {
			xmlRoot = xmlFile.getRoot();
		}

		if (xmlRoot == null) {
			SimpleFile outFile = new SimpleFile(dataDir, DB_FILE_NAME);
			outFile.saveContent(
				"<main>" +
				"<" + MAX_ID + ">0</" + MAX_ID + ">" +
				"<" + LOST_ITEMS + "></" + LOST_ITEMS + ">" +
				"<" + FOUND_ITEMS + "></" + FOUND_ITEMS + ">" +
				"</main>"
			);

			xmlFile = new XmlFile(dataDir, DB_FILE_NAME);
			xmlRoot = xmlFile.getRoot();
		}

		try {
			XmlElement maxidEl = xmlRoot.getChild(MAX_ID);
			if (maxidEl == null) {
				maxidEl = xmlRoot.createChild(MAX_ID);
			}
			maxid = Integer.parseInt(maxidEl.getInnerText());
		} catch (NumberFormatException e) {
		}
	}

	public void saveDatabase() {

		xmlFile.save();
	}
}
