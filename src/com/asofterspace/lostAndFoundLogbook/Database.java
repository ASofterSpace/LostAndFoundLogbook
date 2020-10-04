/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.lostAndFoundLogbook;

import com.asofterspace.toolbox.coders.Base64Decoder;
import com.asofterspace.toolbox.io.BinaryFile;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.SimpleFile;
import com.asofterspace.toolbox.io.XML;
import com.asofterspace.toolbox.io.XmlElement;
import com.asofterspace.toolbox.io.XmlFile;
import com.asofterspace.toolbox.utils.Record;
import com.asofterspace.toolbox.utils.RecordKind;


public class Database {

	// UPDATE NEXT YEAR!
	private final static String THIS_YEAR = "2019";

	// UPDATE NEXT YEAR! - MUST INCLUDE THIS YEAR!
	private final static String[] PREVIOUS_YEARS = {"2017", "2018", "2019"};

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

	public void saveLostItem(Record item) {

		XmlElement lostItems = xmlRoot.getChild(LOST_ITEMS);
		XmlElement lostItem = null;
		String currentId = null;

		if (item.contains("editId")) {

			currentId = item.getString("editId");
			lostItem = lostItems.getChildWithChild(LOST_ITEM, "id", currentId);

		} else {

			lostItem = lostItems.createChild(LOST_ITEM);
			increaseMaxId();
			currentId = "" + maxid;
			lostItem.createChild("id").setInnerText(currentId);
		}

		// explicitly mention what we want to get to not save crap into the database
		item.removeAllExcept("what", "cat", "when", "where", "who", "contactonsite", "contactoffsite", "delivered");

		lostItem.addOrUpdateChildrenOf(item);

		saveDatabase();
	}

	public void saveFoundItem(Record item) {

		XmlElement foundItems = xmlRoot.getChild(FOUND_ITEMS);
		XmlElement foundItem = null;
		String currentId = null;

		if (item.contains("editId")) {

			currentId = item.getString("editId");
			foundItem = foundItems.getChildWithChild(FOUND_ITEM, "id", currentId);

		} else {

			foundItem = foundItems.createChild(FOUND_ITEM);
			increaseMaxId();
			currentId = "" + maxid;
			foundItem.createChild("id").setInnerText(currentId);
		}

		String picStrBase64 = item.getString("picture");
		if (picStrBase64 != null) {
			// the picStrBase64 should look like the following, e.g.:
			// data:image/jpeg;base64,
			// data:image/png,
			// (the ;base64 bit is optional)
			if (picStrBase64.contains(",")) {
				picStrBase64 = picStrBase64.substring(picStrBase64.indexOf(",") + 1);
			}
			String picStr = Base64Decoder.decode(picStrBase64);
			String picName = "pic" + currentId + ".jpg";
			BinaryFile picFile = new BinaryFile(dataDir, picName);
			picFile.saveContentStr(picStr);
			XmlElement picEl = foundItem.getChild("picture");
			if (picEl != null) {
				foundItem.removeChild(picEl);
			}
			foundItem.createChild("picture").setInnerText(picName);
		}

		// explicitly mention what we want to get to not save crap into the database
		item.removeAllExcept("what", "cat", "when", "where", "who", "curlocation", "delivered");

		XmlElement itemData = new XmlElement(item);
		foundItem.addOrUpdateChildrenOf(itemData);

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

	public Record getItems(Record request) {

		String getYear = null;
		if (request.getString("year") != null) {
			getYear = request.getString("year");
			if ("current".equals(getYear)) {
				getYear = THIS_YEAR;
			}
		}

		// get all the items
		if ("*".equals(request.getString("id"))) {

			Record result = new Record();

			Record lostItemsJson = new Record();
			lostItemsJson.makeArray();

			XmlElement lostItems = xmlRoot.getChild(LOST_ITEMS);

			for (XmlElement lostItem : lostItems.getChildren(LOST_ITEM)) {
				if (getYear != null) {
					String whenStr = lostItem.getChild("when").getInnerText();
					if (!whenStr.contains(getYear)) {
						continue;
					}
				}
				lostItemsJson.append(publish(lostItem));
			}

			lostItemsJson.reverse();

			result.set("lostItems", lostItemsJson);

			Record foundItemsJson = new Record();
			foundItemsJson.makeArray();

			XmlElement foundItems = xmlRoot.getChild(FOUND_ITEMS);

			for (XmlElement foundItem : foundItems.getChildren(FOUND_ITEM)) {
				if (getYear != null) {
					String whenStr = foundItem.getChild("when").getInnerText();
					if (!whenStr.contains(getYear)) {
						continue;
					}
				}
				foundItemsJson.append(publish(foundItem));
			}

			foundItemsJson.reverse();

			result.set("foundItems", foundItemsJson);

			return result;
		}

		// get a specific item
		String idToFind = request.getString("id");

		if (idToFind == null) {
			return null;
		}

		Record result = new Record();

		Record lostItemsJson = new Record();
		lostItemsJson.makeArray();

		XmlElement lostItems = xmlRoot.getChild(LOST_ITEMS);

		for (XmlElement lostItem : lostItems.getChildren(LOST_ITEM)) {
			if (idToFind.equals(lostItem.getChild("id").getInnerText())) {
				lostItemsJson.append(publish(lostItem));
			}
		}

		result.set("lostItems", lostItemsJson);

		Record foundItemsJson = new Record();
		foundItemsJson.makeArray();

		XmlElement foundItems = xmlRoot.getChild(FOUND_ITEMS);

		for (XmlElement foundItem : foundItems.getChildren(FOUND_ITEM)) {
			if (idToFind.equals(foundItem.getChild("id").getInnerText())) {
				foundItemsJson.append(publish(foundItem));
			}
		}

		result.set("foundItems", foundItemsJson);

		return result;
	}

	private Record publish(XmlElement xmlItem) {

		Record item = new XML(xmlItem);

		Record delivered = item.get("delivered");
		if (delivered != null) {
			delivered.convertTo(RecordKind.BOOLEAN);
		}

		return item;
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

	private void addYearToEntries(XmlElement parent) {

		for (XmlElement lostItem : parent.getChildNodes()) {
			boolean appendThisYear = true;
			XmlElement when = lostItem.getChild("when");
			if (when == null) {
				when = lostItem.createChild("when");
			}
			String whenStr = when.getInnerText();
			if (whenStr != null) {
				for (String year : PREVIOUS_YEARS) {
					if (whenStr.contains(year)) {
						appendThisYear = false;
					}
				}
			}
			if (appendThisYear) {
				if ((whenStr == null) || "".equals(whenStr)) {
					whenStr = THIS_YEAR;
				} else {
					whenStr = whenStr + " " + THIS_YEAR;
				}
				when.setInnerText(whenStr);
			}
		}

	}

	public void saveDatabase() {

		addYearToEntries(xmlFile.getRoot().getChild(LOST_ITEMS));
		addYearToEntries(xmlFile.getRoot().getChild(FOUND_ITEMS));

		xmlFile.getRoot().sortChildren(true);
		xmlFile.save();
	}
}
