
var categories = [
	"keys", "wallets", "bags", "cards", "phones", "computers", "glasses",
	"jewelry", "clocks", "lamps", "cups", "clothes", "others"
];

function toCat(str) {
	switch (str) {
		case "keys":
			return "Keys";
		case "wallets":
			return "Wallets";
		case "bags":
			return "Bags and Backpacks";
		case "cards":
			return "Passports, Bank Cards, Driver's Licenses etc.";
		case "phones":
			return "Smartphones, Radios, etc.";
		case "computers":
			return "Computers, USB-Dongles, Computery Things";
		case "glasses":
			return "Glasses and Sunglasses";
		case "jewelry":
			return "Jewelry";
		case "clocks":
			return "Clocks and Watches";
		case "lamps":
			return "Lamps and Torches";
		case "cups":
			return "Cups and Bottles";
		case "clothes":
			return "Clothes";
	}
	return "Other Things";
}
