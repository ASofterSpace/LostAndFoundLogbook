/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.lostAndFoundLogbook;

import com.asofterspace.toolbox.coders.Base64Decoder;
import com.asofterspace.toolbox.io.BinaryFile;
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

	public Directory getDataDirectory() {
		return dataDir;
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

	public void saveFoundItem(JSON item) {

		XmlElement foundItems = xmlRoot.getChild(FOUND_ITEMS);

		XmlElement foundItem = foundItems.createChild(FOUND_ITEM);

		increaseMaxId();

		foundItem.createChild("id").setInnerText(maxid);
		foundItem.createChild("what").setInnerText(item.getString("what"));
		foundItem.createChild("when").setInnerText(item.getString("when"));
		foundItem.createChild("where").setInnerText(item.getString("where"));
		foundItem.createChild("who").setInnerText(item.getString("who"));

		String picStrBase64 = item.getString("picture");
		if (picStrBase64 != null) {
			// the picStrBase64 should look like the following, e.g.:
			// data:image/jpeg;base64,
			// data:image/png,
			// (the ;base64 bit is optional)
			if (picStrBase64.contains(",")) {
				picStrBase64 = picStrBase64.substring(picStrBase64.indexOf(",") + 1);
			}
			String picStr = Base64Decoder.decodeFromBase64(picStrBase64);
			String picName = "pic" + maxid + ".jpg";
			BinaryFile picFile = new BinaryFile(dataDir, picName);
			picFile.saveContentStr(picStr);
			foundItem.createChild("picture").setInnerText(picName);
		}

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

	public String getItems(JSON request) {

		// get all the items
		if ("*".equals(request.getString("id"))) {

			StringBuilder result = new StringBuilder();

			result.append("{");

			result.append("\"lostItems\":[");

			XmlElement lostItems = xmlRoot.getChild(LOST_ITEMS);
			String sep = "";

			for (XmlElement lostItem : lostItems.getChildren(LOST_ITEM)) {
				result.append(sep);
				sep = ",";
				result.append("{\"id\": ");
				result.append(JSON.escapeJSONstr(lostItem.getChild("id").getInnerText()));
				result.append(", \"what\": \"");
				result.append(JSON.escapeJSONstr(lostItem.getChild("what").getInnerText()));
				result.append("\", \"when\": \"");
				result.append(JSON.escapeJSONstr(lostItem.getChild("when").getInnerText()));
				result.append("\", \"where\": \"");
				result.append(JSON.escapeJSONstr(lostItem.getChild("where").getInnerText()));
				result.append("\", \"who\": \"");
				result.append(JSON.escapeJSONstr(lostItem.getChild("who").getInnerText()));
				result.append("\", \"contactonsite\": \"");
				result.append(JSON.escapeJSONstr(lostItem.getChild("contactonsite").getInnerText()));
				result.append("\", \"contactoffsite\": \"");
				result.append(JSON.escapeJSONstr(lostItem.getChild("contactoffsite").getInnerText()));
				result.append("\"}");
			}

			result.append("],");

			result.append("\"foundItems\":[");

			XmlElement foundItems = xmlRoot.getChild(FOUND_ITEMS);
			sep = "";

			for (XmlElement foundItem : foundItems.getChildren(FOUND_ITEM)) {
				result.append(sep);
				sep = ",";
				result.append("{\"id\": ");
				result.append(JSON.escapeJSONstr(foundItem.getChild("id").getInnerText()));
				result.append(", \"what\": \"");
				result.append(JSON.escapeJSONstr(foundItem.getChild("what").getInnerText()));
				result.append("\", \"when\": \"");
				result.append(JSON.escapeJSONstr(foundItem.getChild("when").getInnerText()));
				result.append("\", \"where\": \"");
				result.append(JSON.escapeJSONstr(foundItem.getChild("where").getInnerText()));
				result.append("\", \"who\": \"");
				result.append(JSON.escapeJSONstr(foundItem.getChild("who").getInnerText()));
				if (foundItem.getChild("picture") != null) {
					result.append("\", \"picture\": \"");
					result.append(JSON.escapeJSONstr(foundItem.getChild("picture").getInnerText()));
				}
				result.append("\"}");
			}

			result.append("]");

			result.append("}");

			return result.toString();
		}

		return null;
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
